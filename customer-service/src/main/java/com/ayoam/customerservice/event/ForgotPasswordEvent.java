package com.ayoam.customerservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotPasswordEvent {
    private String email;
    private String resetPasswordToken;
}
