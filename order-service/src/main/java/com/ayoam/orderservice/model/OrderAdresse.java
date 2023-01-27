package com.ayoam.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "order_adresse")
public class OrderAdresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ida;
    @NotNull
    @JsonProperty("HomeAdresse")
    private String adresse;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String city;
    @NotNull
    private int postalCode;
    @NotNull
    private String stateProvince;
    private String country;

    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }

    public String getFormattedAdresse(){
        return this.getFullName()+" \n "+this.getAdresse() +" \n "+this.getCity() +","+ this.getPostalCode()+" \n "+this.getStateProvince()+" , "+this.getCountry();
    }
}
