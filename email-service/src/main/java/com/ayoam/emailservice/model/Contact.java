package com.ayoam.emailservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity(name="contact")
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idc;
    private String username;
    private String email;
    private String message;
    private Date date;
    @Column(name = "read", nullable = false)
    private boolean read;


}
