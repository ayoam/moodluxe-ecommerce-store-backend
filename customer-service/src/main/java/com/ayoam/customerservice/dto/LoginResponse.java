package com.ayoam.customerservice.dto;

import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LoginResponse {
    @NotNull
    @JsonProperty("access_token")
    private String accessToken;

    @NotNull
    @JsonProperty("refresh_token")
    private String refreshToken;

    @NotNull
    @JsonProperty("userInfo")
    private UserInfo userInfo;
}
