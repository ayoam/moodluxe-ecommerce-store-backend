package com.ayoam.productservice.service;

import com.ayoam.productservice.converter.ProductConverter;
import com.ayoam.productservice.dto.AllCategoriesResponse;
import com.ayoam.productservice.model.Category;
import com.ayoam.productservice.model.Product;
import com.ayoam.productservice.repository.BrandRepository;
import com.ayoam.productservice.repository.CategoryRepository;
import com.ayoam.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class CategoryService {
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(BrandRepository brandRepository, CategoryRepository categoryRepository, ProductConverter productConverter) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    public AllCategoriesResponse getCategories() {
        AllCategoriesResponse res = new AllCategoriesResponse();
        res.setCategoryList(categoryRepository.findAll());
        return res;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category addCategory(Category category) {
        if(categoryRepository.findByNameIgnoreCase(category.getName()).orElse(null)!=null){
            throw new RuntimeException("category already exist");
        }
        return categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(Category category,Long idc) {
        Category cat = categoryRepository.findById(idc).orElse(null);
        if(cat==null){
            throw new RuntimeException("category not found!");
        }
        if(categoryRepository.findByNameIgnoreCase(category.getName()).orElse(null)!=null && category.getName()!=cat.getName()){
            throw new RuntimeException("category already exist");
        }
        cat.setName(category.getName());
        return categoryRepository.save(cat);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long idc) {
        Category cat = categoryRepository.findById(idc).orElse(null);
        if(cat==null){
            throw new RuntimeException("category not found!");
        }

        //remove categorie from all products
        for(Product p:cat.getProductsList()){
            p.getCategoryList().remove(cat);
        }
        cat.setProductsList(new ArrayList<>());
        categoryRepository.delete(cat);
    }
}
