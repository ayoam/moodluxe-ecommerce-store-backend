package com.ayoam.productservice.controller;

import com.ayoam.productservice.dto.AllProductsResponse;
import com.ayoam.productservice.dto.ProductDto;
import com.ayoam.productservice.model.Product;
import com.ayoam.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<AllProductsResponse> getAllProducts(@RequestParam Map<String,String> filters, HttpServletResponse response){
        return new ResponseEntity<AllProductsResponse>(productService.getAllProducts(filters,response),HttpStatus.OK);
    }

    //TODO:add photos section
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/products")
    public ResponseEntity<Product> addProduct(@RequestPart(value = "productInfo",required = false) ProductDto productDto, @RequestPart("photo") List<MultipartFile> photosList){
//        return new ResponseEntity<Product>(productService.addProduct(productDto),HttpStatus.CREATED);
        return new ResponseEntity<Product>(productService.addProduct(productDto,photosList),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/{idp}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long idp){
        productService.deleteProduct(idp);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{idp}")
    public ResponseEntity<?> updateProduct(@PathVariable Long idp,@RequestPart(value = "productInfo",required = false) ProductDto productDto, @RequestPart("photo") List<MultipartFile> photosList){
        return new ResponseEntity<Product>(productService.updateProduct(productDto,photosList,idp),HttpStatus.OK);
    }

    //find product by id
    @GetMapping("/products/{idp}")
    public ResponseEntity<Product> findProduct(@PathVariable Long idp){
        return new ResponseEntity<Product>(productService.findProduct(idp),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products/productsTotal")
    public ResponseEntity<Long> getProductsTotal(){
        return new ResponseEntity<Long>(productService.getProductsTotal(),HttpStatus.OK);
    }
    @GetMapping("/products/topSellingItems")
    public ResponseEntity<List<Product>> getTopSellingItems() {
        List<Product> products = productService.getTopSellingItems();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
