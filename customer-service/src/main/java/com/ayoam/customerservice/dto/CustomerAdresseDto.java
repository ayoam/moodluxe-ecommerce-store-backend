package com.ayoam.customerservice.dto;

import com.ayoam.customerservice.model.Country;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
public class CustomerAdresseDto {
    private Long ida;
    @NotNull
    private String adresse;
    @NotNull
    private String city;
    @NotNull
    private int postalCode;
    @NotNull
    private String stateProvince;
    @NotNull
    private Long countryId ;
}
