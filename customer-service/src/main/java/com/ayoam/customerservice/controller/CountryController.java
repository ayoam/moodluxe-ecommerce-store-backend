package com.ayoam.customerservice.controller;

import com.ayoam.customerservice.dto.GetCountriesResponse;
import com.ayoam.customerservice.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/countries")

public class CountryController {
    private CountryService countryService;

    public CountryController(CountryService countryService){
        this.countryService = countryService;
    }
    @GetMapping
    public ResponseEntity<?> getCountries(){
        return new ResponseEntity<GetCountriesResponse>(countryService.getCountries(), HttpStatus.OK);
    }

}
