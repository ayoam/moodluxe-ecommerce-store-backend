package com.ayoam.productservice.service;

import com.ayoam.productservice.dto.AllBrandsResponse;
import com.ayoam.productservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BrandService {
    private BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
    public AllBrandsResponse getBrands() {
        AllBrandsResponse res = new AllBrandsResponse();
        res.setBrandList(brandRepository.findAll());
        return res;
    }


}



