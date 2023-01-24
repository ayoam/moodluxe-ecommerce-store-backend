package com.ayoam.productservice.dto;

import com.ayoam.productservice.model.Brand;
import com.ayoam.productservice.model.Category;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDto {
    @NotNull
    private String libelle;
    @NotNull
    private String description;
    @NotNull
    private Double price;
    private Double compareToPrice=null;
    @NotNull
    private int quantity;
    @NotNull
    private Boolean active;
    @NotNull
    private List<Long> categoriesIdList;
    @NotNull
    private Long idBrand;
}
