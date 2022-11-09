package com.ayoam.customerservice.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordDto {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
