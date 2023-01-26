package com.ayoam.productservice.service;

import com.ayoam.productservice.converter.ProductConverter;
import com.ayoam.productservice.dto.AllProductsResponse;
import com.ayoam.productservice.dto.OrderRequest;
import com.ayoam.productservice.dto.ProductDto;
import com.ayoam.productservice.event.StockReservedEvent;
import com.ayoam.productservice.model.Brand;
import com.ayoam.productservice.model.Category;
import com.ayoam.productservice.model.Photo;
import com.ayoam.productservice.model.Product;
import com.ayoam.productservice.query.ProductPredicateBuilder;
import com.ayoam.productservice.repository.BrandRepository;
import com.ayoam.productservice.repository.CategoryRepository;
import com.ayoam.productservice.repository.PhotoRepository;
import com.ayoam.productservice.repository.ProductRepository;
import com.google.common.collect.Iterators;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.querydsl.core.types.Predicate;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private ProductConverter productConverter;

    private PhotoRepository photoRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, ProductConverter productConverter, PhotoRepository photoRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productConverter = productConverter;
        this.photoRepository = photoRepository;
    }

    public AllProductsResponse getAllProducts(Map<String,String> filters, HttpServletResponse response){
        Sort sortBy = filters.get("sort")!=null ?
            switch (filters.get("sort")) {
                case "nameAsc" -> Sort.by(Sort.Direction.ASC, "libelle");
                case "nameDesc" -> Sort.by(Sort.Direction.DESC, "libelle");
                case "priceAsc" -> Sort.by(Sort.Direction.ASC, "price");
                case "priceDesc" -> Sort.by(Sort.Direction.DESC, "price");
                case "asc" -> Sort.by(Sort.Direction.ASC, "idp");
                case "desc" -> Sort.by(Sort.Direction.DESC, "idp");
                default -> Sort.by(Sort.Direction.ASC, "idp");
            }
            :
            Sort.by(Sort.Direction.ASC, "idp");


        Pageable pages = PageRequest.of(Integer.parseInt(filters.get("page")),Integer.parseInt(filters.get("limit")),sortBy);
        Predicate predicate = ProductPredicateBuilder.productFilters(filters);
        AllProductsResponse res = new AllProductsResponse();
        res.setProductList(productRepository.findAll(predicate,pages).getContent());

        int count= Iterators.size(productRepository.findAll(predicate).iterator());

        int minPrice = productRepository.minPrice();
        int maxPrice = productRepository.maxPrice();
        res.setMinPrice(minPrice);
        res.setMaxPrice(maxPrice);
        res.setTotalCount(count);

        return res;
    }

    public Product addProduct(ProductDto dto, List<MultipartFile> photoList) {
        Brand brand = brandRepository.findById(dto.getIdBrand()).orElse(null);
        List<Category> catList = new ArrayList<>();

        for(Long id:dto.getCategoriesIdList()){
            Category category = categoryRepository.findById(id).orElse(null);
            System.out.println(category==null);
            if(category==null){
                throw new IllegalArgumentException("Category id invalid!");
            }
            catList.add(category);
        }

        if(brand==null){
            throw new IllegalArgumentException("Brand id invalid!");
        }

//        Category c = new Category();
//        c.setName("hello");
//        categoryRepository.save(c);

        Product product = productConverter.dtoToProduct(dto);
        product.setCategoryList(catList);
        Category c = categoryRepository.findById(dto.getCategoriesIdList().get(0)).orElse(null);
//        System.out.println(c.getName());
//        product.addCategorie(c);
        product.setBrand(brand);
        
        //configure photos
        for(MultipartFile file:photoList){
            try{
                byte[] image = Base64.encodeBase64(file.getBytes());
                String encodedImage = new String(image);
                Photo photo = new Photo();
                photo.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                photo.setPhoto(encodedImage);
                photo = photoRepository.save(photo);
                product.addPhoto(photo);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long idp) {
        Product product = productRepository.findById(idp).orElse(null);
        if(product!=null){
            productRepository.delete(product);
        }
        else{
            throw new RuntimeException("invalid product!");
        }
        
    }

    public Product updateProduct(ProductDto dto,List<MultipartFile> photoList,Long idp) {
        Product product = productRepository.findById(idp).orElse(null);
        if(product==null){
            throw new RuntimeException("invalid product!");
        }
        Brand brand = brandRepository.findById(dto.getIdBrand()).orElse(null);

        List<Category> catList = new ArrayList<Category>();
        for(Long id:dto.getCategoriesIdList()){
            Category category = categoryRepository.findById(id).orElse(null);
            if(category==null){
                throw new IllegalArgumentException("Category id invalid!");
            }
            catList.add(category);
        }

        if(brand==null){
            throw new IllegalArgumentException("Brand id invalid!");
        }

        Product newModifiedProduct = productConverter.dtoToProduct(dto);
        newModifiedProduct.setCategoryList(catList);
        newModifiedProduct.setBrand(brand);
        newModifiedProduct.setIdp(idp);
        newModifiedProduct.setPhotoList(new ArrayList<>());

        //update photos
        for(MultipartFile file:photoList){
            try{
                byte[] image = Base64.encodeBase64(file.getBytes());
                String encodedImage = new String(image);
                Photo photo = new Photo();
                photo.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                photo.setPhoto(encodedImage);
                photo = photoRepository.save(photo);
                newModifiedProduct.addPhoto(photo);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return productRepository.save(newModifiedProduct);

    }

    public void updateProductsStock(StockReservedEvent stockReservedEvent){
        for(OrderRequest order:stockReservedEvent.getProductsList()){
            Product product = productRepository.findById(order.getProductId()).orElse(null);
            if(product==null){
                throw new RuntimeException("product "+order.getProductId()+" doesn't exist!");
            }
            product.setQuantity(product.getQuantity()-order.getQuantity());
            product.setOrdersCount(product.getOrdersCount()+order.getQuantity());
            productRepository.save(product);
        }
    }

    public Product updateProductPhotos(List<MultipartFile> photoList, Long idp) {
        Product product= productRepository.findById(idp).orElse(null);

        if(product==null){
            throw new RuntimeException("invalid product!");
        }

        for(Photo photo:product.getPhotoList()){
            product.getPhotoList().remove(photo);
        }

        product.getPhotoList().removeAll(product.getPhotoList());

        for(MultipartFile file:photoList){
            try{
                byte[] image = Base64.encodeBase64(file.getBytes());
                String encodedImage = new String(image);
                Photo photo = new Photo();
                photo.setPhoto(encodedImage);
                photo = photoRepository.save(photo);
                product.addPhoto(photo);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return productRepository.save(product);
    }

    public Product findProduct(Long idp) {
        Product product= productRepository.findById(idp).orElse(null);

        if(product==null){
            throw new RuntimeException("invalid product!");
        }

        return product;
    }


    public Long getProductsTotal() {
        return productRepository.productsTotal();
    }
    public List<Product> getTopSellingItems() {
        List<Product> products = productRepository.findTopSellingItems();
        if(products.size()>=5)
            return products.subList(0,5);
        else
            return products;
    }
}
