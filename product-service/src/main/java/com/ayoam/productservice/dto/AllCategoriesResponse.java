package com.ayoam.productservice.dto;

import com.ayoam.productservice.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AllCategoriesResponse {
    @JsonProperty("data")
    private List<Category> categoryList;
}
