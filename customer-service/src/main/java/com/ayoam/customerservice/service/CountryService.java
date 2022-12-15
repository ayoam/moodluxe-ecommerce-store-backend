package com.ayoam.customerservice.service;

import com.ayoam.customerservice.dto.GetCountriesResponse;
import com.ayoam.customerservice.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;




@Service
public class CountryService {
    private CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }
    public GetCountriesResponse getCountries() {
        GetCountriesResponse res = new GetCountriesResponse();
        res.setCountries(countryRepository.findAll());
        return res;
    }

}
