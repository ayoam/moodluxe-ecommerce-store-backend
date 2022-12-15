package com.ayoam.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotNull
    @JsonProperty("refresh_token")
    private String refreshToken;
}
