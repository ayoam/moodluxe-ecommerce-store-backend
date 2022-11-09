package com.ayoam.customerservice.converter;

import com.ayoam.customerservice.dto.CustomerDto;
import com.ayoam.customerservice.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter {
    public Customer customerDtoToCustomer(CustomerDto dto){
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setBirthDate(dto.getBirthDate());
        customer.setKeycloakId(dto.getKeycloakId());
        return customer;
    }

}
