package com.github.kettoleon.sweepstakes.client.apifootball.model.countries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CountriesResponse {

    private List<Country> response = new ArrayList<>();

    public Optional<Country> getCountry(String name) {
        return response.stream().filter(c -> c.getName().equalsIgnoreCase(name) || c.getName().replace('-', ' ').equalsIgnoreCase(name)).findFirst();
    }

}
