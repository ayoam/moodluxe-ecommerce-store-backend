package com.ayoam.customerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name="customer_adresse")
public class CustomerAdresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ida;
    @NotNull
    @JsonProperty("HomeAdresse")
    private String adresse;
    @NotNull
    private String city;
    @NotNull
    private int postalCode;
    @NotNull
    private String stateProvince;
    @ManyToOne
    private Country country;
}
