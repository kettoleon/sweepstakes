package com.github.kettoleon.sweepstakes.client.apifootball;

import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.Country;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.localdev.MockedApiFootballClient;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadLeagueIcons {

    public static void main(String[] args) {
        ApiFootballClient apiFootballClient = new MockedApiFootballClient(1, 2022);
        CountriesResponse countries = apiFootballClient.getCountries();
        FixturesResponse fixtures = apiFootballClient.getFixtures();
        fixtures.getResponse().forEach(fe -> {
            downloadLogoIfNeeded("teams", fe.getTeams().getHome().getLogo());
            downloadLogoIfNeeded("teams", fe.getTeams().getAway().getLogo());
            countries.getCountry(fe.getTeams().getHome().getName()).map(Country::getFlag).ifPresentOrElse(DownloadLeagueIcons::downloadCountryFlagIfNeeded, () -> {
                throw new RuntimeException("Could not match " + fe.getTeams().getHome().getName());
            });
            countries.getCountry(fe.getTeams().getAway().getName()).map(Country::getFlag).ifPresentOrElse(DownloadLeagueIcons::downloadCountryFlagIfNeeded, () -> {
                throw new RuntimeException("Could not match " + fe.getTeams().getAway().getName());
            });
        });
        downloadLogoIfNeeded("leagues", apiFootballClient.getLeague().getResponse().get(0).getLeague().getLogo());

    }

    private static void downloadCountryFlagIfNeeded(String logo) {
        downloadLogoIfNeeded("countries", logo);
    }

    private static void downloadLogoIfNeeded(String folder, String logo) {
        try {
            URL url = new URL(logo);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            String downloadFileName = "./src/main/resources/static/" + folder + "/" + StringUtils.substringAfterLast(url.getPath(), "/");
            if (!new File(downloadFileName).exists()) {
                System.out.println("Downloading " + logo);
                FileOutputStream fileOutputStream = new FileOutputStream(downloadFileName);
                FileChannel fileChannel = fileOutputStream.getChannel();
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
