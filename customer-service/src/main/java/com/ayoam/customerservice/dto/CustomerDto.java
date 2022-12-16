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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;
    @NotNull
    private String gender;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String keycloakId;

    private Long cartId;

    @JsonProperty("customerAdresse")
    private CustomerAdresseDto adresseDto;
}
