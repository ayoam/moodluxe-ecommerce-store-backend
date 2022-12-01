package com.ayoam.productservice.dto;

import com.ayoam.productservice.model.Brand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AllBrandsResponse {
    @JsonProperty("data")
    private List<Brand> brandList;
}
