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
        customer.setGender(dto.getGender());
        customer.setKeycloakId(dto.getKeycloakId());
        return customer;
    }

    public CustomerDto customerToCustomerDto(Customer customer){
        CustomerDto dto = new CustomerDto();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setBirthDate(customer.getBirthDate());
        dto.setGender(customer.getGender());
        dto.setKeycloakId(customer.getKeycloakId());
        dto.setIdc(customer.getIdc());
        dto.setCartId(customer.getCartId());
        return dto;
    }
}
