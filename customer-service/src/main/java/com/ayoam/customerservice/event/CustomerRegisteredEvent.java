package com.ayoam.customerservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerRegisteredEvent {
    private String email;
    private String fullName;
    private String confirmationToken;
}
