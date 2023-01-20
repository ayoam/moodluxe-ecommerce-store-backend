package com.ayoam.customerservice.dto;

import com.ayoam.customerservice.model.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class getAllCustomersResponse {
    @JsonProperty("data")
    private List<Customer> customerList;
    private int totalCount=0;
    public getAllCustomersResponse(List<Customer> customerList){
        this.customerList=customerList;
    }
}
