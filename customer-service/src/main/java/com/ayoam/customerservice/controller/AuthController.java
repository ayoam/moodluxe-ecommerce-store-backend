package com.ayoam.customerservice.controller;

import com.ayoam.customerservice.dto.CustomerDto;
import com.ayoam.customerservice.dto.ForgotPasswordRequest;
import com.ayoam.customerservice.dto.LoginRequest;
import com.ayoam.customerservice.dto.UpdatePasswordDto;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/updatePassword")
    public ResponseEntity<Customer> updateCustomerPassword(@RequestBody UpdatePasswordDto passwordDto, HttpServletRequest request){
        return new ResponseEntity<Customer>(customerService.updateCustomerPassword(passwordDto,request),HttpStatus.OK);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExistance(@PathVariable String email){
        return customerService.checkEmailExistance(email);
    }
}
