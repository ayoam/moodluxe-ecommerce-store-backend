package com.ayoam.customerservice.controller;

import com.ayoam.customerservice.dto.*;
import com.ayoam.customerservice.model.Customer;
import com.ayoam.customerservice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController()
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<getAllCustomersResponse> getAllCustomers(@RequestParam Map<String,String> filters){
        return new ResponseEntity<getAllCustomersResponse>(customerService.getAllCustomers(filters), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/{id}/updateDetails")
    public ResponseEntity<Customer> updateCustomerDetails(@RequestBody CustomerDetailsDto customerDetailsDto, @PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.updateCustomerDetails(customerDetailsDto,id),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping("/{id}/updateAdresse")
    public ResponseEntity<Customer> updateCustomerAdresse(@RequestBody CustomerAdresseDto adresseDto, @PathVariable Long id){
        return new ResponseEntity<Customer>(customerService.updateCustomerAdresse(adresseDto,id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customersTotal")
    public ResponseEntity<Long> getCustomersTotal(){
        return new ResponseEntity<Long>(customerService.getCustomersTotal(),HttpStatus.OK);
    }

}
