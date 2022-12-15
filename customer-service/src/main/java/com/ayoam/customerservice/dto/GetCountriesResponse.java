package com.ayoam.customerservice.dto;

import com.ayoam.customerservice.model.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetCountriesResponse {
    @JsonProperty("data")
    private List<Country> countries;
}
