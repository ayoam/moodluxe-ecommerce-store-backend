package com.ayoam.productservice.controller;

import com.ayoam.productservice.dto.AllBrandsResponse;
import com.ayoam.productservice.service.BrandService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandController {

    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }
    @GetMapping("brands")
    public ResponseEntity<?> getAllBrands(){
        return new ResponseEntity<AllBrandsResponse>(brandService.getBrands(), HttpStatus.OK);
    }
}
