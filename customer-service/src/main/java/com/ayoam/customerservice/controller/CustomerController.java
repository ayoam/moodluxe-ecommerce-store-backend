package com.ayoam.customerservice.controller;

import com.ayoam.customerservice.dto.*;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<getAllCustomersResponse> getAllCustomers(){
        return new ResponseEntity<getAllCustomersResponse>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<Customer>(customerService.createCustomer(customerDto),HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest loginRequest){
        return customerService.loginCustomer(loginRequest);
    }

    @PostMapping("/auth/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        customerService.forgotPasswordHandler(forgotPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/customers/{id}/updateDetails")
    public ResponseEntity<Customer> updateCustomerDetails(@RequestBody CustomerDetailsDto customerDetailsDto, @PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.updateCustomerDetails(customerDetailsDto,id),HttpStatus.OK);
    }

    @PutMapping("/customers/{id}/updateAdresse")
    public ResponseEntity<Customer> updateCustomerAdresse(@RequestBody CustomerAdresseDto adresseDto, @PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.updateCustomerAdresse(adresseDto,id),HttpStatus.OK);
    }

    @PutMapping("/auth/updatePassword")
    public ResponseEntity<Customer> updateCustomerPassword(@RequestBody UpdatePasswordDto passwordDto, HttpServletRequest request){
        return new ResponseEntity<Customer>(customerService.updateCustomerPassword(passwordDto,request),HttpStatus.OK);
    }

}
