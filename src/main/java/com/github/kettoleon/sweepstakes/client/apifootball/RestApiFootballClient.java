package com.github.kettoleon.sweepstakes.client.apifootball;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesEntry;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class RestApiFootballClient implements ApiFootballClient {

    public static final DateTimeFormatter CACHE_FILE_NAME_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
    private String rapidApiKey;
    private String rapidApiHost;
    private final int leagueId;
    private final int seasonId;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private LeagueResponse cachedLeague;
    private CountriesResponse cachedCountries;
    private FixturesResponse inMemoryFixturesCache;
    private LocalDateTime lastCacheTime = LocalDateTime.now().minusDays(1);

    private int requestsToday = 100;
    private int maxRequestsPerDay = 100;

    public RestApiFootballClient(int leagueId, int seasonId, String rapidApiHost, String rapidApiKey) {
        log.info("Setting up Rest API Football Client...");
        this.leagueId = leagueId;
        this.seasonId = seasonId;
        this.rapidApiHost = rapidApiHost;
        this.rapidApiKey = rapidApiKey;
        restTemplate = new RestTemplateBuilder().build();
        try {
            cachedCountries = objectMapper.readValue(ResourceUtils.getURL("classpath:countries.json"), CountriesResponse.class);
            cachedLeague = objectMapper.readValue(ResourceUtils.getURL("classpath:league.json"), LeagueResponse.class);
            inMemoryFixturesCache = objectMapper.readValue(ResourceUtils.getURL("classpath:fixtures.json"), FixturesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadLastCacheFromFilesystemIfExists();
        updateInMemoryFixturesCacheIfNeeded();

    }

    private void loadLastCacheFromFilesystemIfExists() {
        try {
            getLastCache().ifPresent(lc -> {
                try {
                    lastCacheTime = fileNameToLocalDateTime(lc);
                    inMemoryFixturesCache = objectMapper.readValue(lc, FixturesResponse.class);
                    log.info("Loaded last cache from file system. Last cache time: {} File: {}", lastCacheTime, lc.getAbsoluteFile().getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Throwable e) {
            log.error("Unable to load cached information about fixtures from file system.", e);
        }
    }

    @Override
    public FixturesResponse getFixtures() {
        updateInMemoryFixturesCacheIfNeeded();
        return inMemoryFixturesCache;
    }

    private void updateInMemoryFixturesCacheIfNeeded() {
        try {
            if (LocalDateTime.now().isBefore(cachedLeague.getSeasonStart()) || LocalDateTime.now().isAfter(cachedLeague.getSeasonEnd())) {
                // Updating cache once a day before the league starts or ends
                if (LocalDateTime.now().isAfter(lastCacheTime.plusDays(1).truncatedTo(ChronoUnit.DAYS))) {
                    log.info("Updating fixtures cache once a day. Last cache time: {}", lastCacheTime);
                    updateInMemoryFixturesCache();
                }
            } else {
                if (LocalDateTime.now().isAfter(getNextUpdateTime())) {
                    // Otherwise, during matches, estimated remaining minutes of playtime of the day / remaining requests
                    updateInMemoryFixturesCache();
                }
            }
        } catch (Throwable e) {
            log.error("Unable to update cached information about fixtures.", e);
        }
    }

    public LocalDateTime getNextUpdateTime() {
        if(aMatchShouldBeInProgress()) {
            double remainingLiveMinutesToday = estimateRemainingMinutes();
            int timeToLiveInMinutes = (int) Math.max(1, Math.ceil(remainingLiveMinutesToday / (maxRequestsPerDay - requestsToday)));
            LocalDateTime nextUpdateTime = lastCacheTime.plusMinutes(timeToLiveInMinutes);
            log.info("Updating fixtures cache during a match. Last cache time: {}, TTL: {}min, LiveMinutesLeft: {}min, RequestsLeft: {}", lastCacheTime, timeToLiveInMinutes, remainingLiveMinutesToday, maxRequestsPerDay - requestsToday);
            return nextUpdateTime;
        }else{
            return inMemoryFixturesCache.getResponse().stream()
                    .map(FixturesEntry::getTime)
                    .filter(t -> t.isAfter(LocalDateTime.now())).min(Comparator.naturalOrder()).orElse(lastCacheTime.plusDays(1));
        }
    }

    private int estimateRemainingMinutes() {
        int c = 0;
        for (FixturesEntry fe : inMemoryFixturesCache.getResponse()) {
            if (fe.isToday()) {
                if (!fe.isFinished()) {
                    c += 120;
                }
                if (fe.getFixture().getStatus().isScheduledOrLive()) {
                    c -= Duration.between(fe.getTime(), LocalDateTime.now()).toMinutes();
                }
            }
        }
        return Math.max(0, c);
    }

    private boolean aMatchShouldBeInProgress() {
        for (FixturesEntry fe : inMemoryFixturesCache.getResponse()) {
            if (LocalDateTime.now().isAfter(fe.getTime()) && fe.getFixture().getStatus().isScheduledOrLive()) {
                return true;
            }
        }
        return false;
    }

    private void updateInMemoryFixturesCache() {
        log.info("Updating fixtures data from rest api...");
        if (requestsToday >= maxRequestsPerDay) {
            log.warn("Reached the limit of requests for today, won't make new requests until tomorrow. Requests: {}, Limit: {}", requestsToday, maxRequestsPerDay);
            return;
        }
        HttpEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.GET, getHeaders(), String.class, getUriVariables());
        LocalDateTime newRequestTime = LocalDateTime.now();
        try {
            File file = new File(getCacheFolder(), newRequestTime.format(CACHE_FILE_NAME_DATE_FORMAT) + ".json");
            String body = response.getBody();
            Files.writeString(file.toPath(), body);
            log.info("New fixtures data stored at {}", file.getAbsoluteFile().getAbsolutePath());
            inMemoryFixturesCache = objectMapper.readValue(body, FixturesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (changingDayFromLastCache()) {
            requestsToday = 1;
        } else {
            requestsToday++;
        }
        lastCacheTime = newRequestTime;
    }

    private Map<String, Object> getUriVariables() {
        return Map.of("league", leagueId + "", "season", seasonId + "");
    }

    private String getUrl() {
        return "https://" + rapidApiHost + "/v3/fixtures?league={league}&season={season}";
    }

    private HttpEntity<?> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", rapidApiKey);
        headers.add("X-Rapidapi-Host", rapidApiHost);
        HttpEntity<?> requestEntity = new RequestEntity<>(null, headers, HttpMethod.GET, null, null);
        return requestEntity;
    }

    private boolean changingDayFromLastCache() {
        return lastCacheTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
    }

    @Override
    public CountriesResponse getCountries() {
        //Countries won't change during the world cup, so it is safe to always load it from the file and keep it in memory.
        return cachedCountries;
    }

    @Override
    public LeagueResponse getLeague() {
        //The league won't change during the world cup, so it is safe to always load it from the file and keep it in memory.
        return cachedLeague;
    }


    private File getCacheFolder() {
        return getCacheFolder(LocalDateTime.now(), true).orElseThrow();
    }

    private Optional<File> getYesterdaysCacheFolder() {
        return getCacheFolder(LocalDateTime.now().minusDays(1), false);
    }

    private static Optional<File> getCacheFolder(LocalDateTime time, boolean create) {
        File cache = new File("cache", time.format(DateTimeFormatter.ISO_LOCAL_DATE));
        if (!cache.exists()) {
            if (create && cache.mkdirs()) {
                return Optional.of(cache);
            }
            return Optional.empty();
        }
        return Optional.of(cache);
    }

    private Optional<File> getLastCache() {
        File[] files = getCacheFolder().listFiles(fn -> !fn.getName().startsWith("."));

        requestsToday = files.length;

        if (files.length == 0) {
            Optional<File> yesterdaysCacheFolder = getYesterdaysCacheFolder();

            if (yesterdaysCacheFolder.isEmpty()) {
                return Optional.empty();
            } else {
                files = yesterdaysCacheFolder.orElseThrow().listFiles(fn -> !fn.getName().startsWith("."));
            }
        }

        return Arrays.stream(files)
                .sorted(Comparator.comparing((File f) -> fileNameToLocalDateTime(f)).reversed())
                .findFirst();

    }

    private LocalDateTime fileNameToLocalDateTime(File f) {
        return LocalDateTime.parse(StringUtils.substringBeforeLast(f.getName(), ".json"), CACHE_FILE_NAME_DATE_FORMAT);
    }

    public LocalDateTime getLastCacheTime() {
        return lastCacheTime;
    }
}
