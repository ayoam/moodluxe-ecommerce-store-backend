package com.ayoam.customerservice.dto;

import com.ayoam.customerservice.model.CustomerAdresse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@NoArgsConstructor
public class CustomerDto {
    private Long idc;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private Date birthDate;
    @NotNull
    private String password;

    private String keycloakId;

    @JsonProperty("customerAdresse")
    private CustomerAdresseDto adresseDto;
}
