package com.ayoam.customerservice.controller;

import com.ayoam.customerservice.dto.*;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<Customer>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest loginRequest){
        return customerService.loginCustomer(loginRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        customerService.forgotPasswordHandler(forgotPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/updatePassword")
    public ResponseEntity<Customer> updateCustomerPassword(@RequestBody UpdatePasswordDto passwordDto, HttpServletRequest request){
        return new ResponseEntity<Customer>(customerService.updateCustomerPassword(passwordDto,request),HttpStatus.OK);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExistance(@PathVariable String email){
        return customerService.checkEmailExistance(email);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException {
        return customerService.refreshToken(refreshTokenRequest);
    }


}
