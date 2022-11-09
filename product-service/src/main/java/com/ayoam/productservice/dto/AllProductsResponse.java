package com.ayoam.productservice.dto;

import com.ayoam.productservice.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AllProductsResponse {
    @JsonProperty("data")
    private List<Product> productList;
}
