package com.ayoam.productservice.controller;

import com.ayoam.productservice.dto.AllCategoriesResponse;
import com.ayoam.productservice.model.Category;
import com.ayoam.productservice.model.Product;
import com.ayoam.productservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/products/categories")
    public ResponseEntity<?> getAllCategories(){
        return new ResponseEntity<AllCategoriesResponse>(categoryService.getCategories(), HttpStatus.OK);
    }

    @PostMapping("/products/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category category){
        return new ResponseEntity<Category>(categoryService.addCategory(category),HttpStatus.CREATED);
    }

    @PutMapping("/products/categories/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Long id){
        return new ResponseEntity<Category>(categoryService.updateCategory(category,id),HttpStatus.OK);
    }

    @DeleteMapping("/products/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
