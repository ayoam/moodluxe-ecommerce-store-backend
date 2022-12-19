package com.ayoam.customerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long customerId =null;
    private String firstName;
    private String lastName;
    private String email;
    private String keycloakId;
    private Long cartId = null;
    private List<String> roles;

    public UserInfo(String firstName, String lastName, String email, String keycloakId, List<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.keycloakId = keycloakId;
        this.roles = roles;
    }
}
