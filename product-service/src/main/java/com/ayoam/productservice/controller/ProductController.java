package com.ayoam.productservice.controller;

import com.ayoam.productservice.dto.AllProductsResponse;
import com.ayoam.productservice.dto.ProductDto;
import com.ayoam.productservice.model.Category;
import com.ayoam.productservice.model.Product;
import com.ayoam.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<AllProductsResponse> getProducts(@RequestParam Map<String,String> filters, HttpServletResponse response){
        return new ResponseEntity<AllProductsResponse>(productService.getAllProducts(filters,response),HttpStatus.OK);
    }

    //TODO:add photos section
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestPart(value = "productInfo",required = false) ProductDto productDto, @RequestPart("photo") List<MultipartFile> photosList){
//        return new ResponseEntity<Product>(productService.addProduct(productDto),HttpStatus.CREATED);
        return new ResponseEntity<Product>(productService.addProduct(productDto,photosList),HttpStatus.OK);
    }

    @DeleteMapping("/products/{idp}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long idp){
        productService.deleteProduct(idp);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/products/{idp}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto,@PathVariable Long idp){
        return new ResponseEntity<Product>(productService.updateProduct(productDto,idp),HttpStatus.OK);
    }

    @PutMapping("/products/{idp}/updatePhotos")
    public ResponseEntity<Product> updateProductPhotos(@RequestPart("photo") List<MultipartFile> photosList,@PathVariable Long idp){
        return new ResponseEntity<Product>(productService.updateProductPhotos(photosList,idp),HttpStatus.OK);
    }

    //find product by id
    @GetMapping("/products/{idp}")
    public ResponseEntity<Product> findProduct(@PathVariable Long idp){
        return new ResponseEntity<Product>(productService.findProduct(idp),HttpStatus.OK);
    }
}
