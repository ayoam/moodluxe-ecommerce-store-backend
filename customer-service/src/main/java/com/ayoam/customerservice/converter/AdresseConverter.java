package com.ayoam.customerservice.converter;

import com.ayoam.customerservice.dto.CustomerAdresseDto;
import com.ayoam.customerservice.model.CustomerAdresse;
import org.springframework.stereotype.Component;

@Component
public class AdresseConverter {
    public CustomerAdresse adresseDtoToAdresse(CustomerAdresseDto dto){
        CustomerAdresse adresse = new CustomerAdresse();
        adresse.setAdresse(dto.getAdresse());
        adresse.setCity(dto.getCity());
        adresse.setPostalCode(dto.getPostalCode());
        adresse.setStateProvince(dto.getStateProvince());

        return adresse;
    }
}
