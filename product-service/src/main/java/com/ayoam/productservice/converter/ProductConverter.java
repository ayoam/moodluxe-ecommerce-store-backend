package com.ayoam.productservice.converter;

import com.ayoam.productservice.dto.ProductDto;
import com.ayoam.productservice.event.ProductStockChangedEvent;
import com.ayoam.productservice.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductDto productToDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setLibelle(product.getLibelle());
        dto.setPrice(product.getPrice());
        dto.setCompareToPrice(product.getCompareToPrice());
        dto.setActive(product.getActive());
        dto.setQuantity(product.getQuantity());
        dto.setDescription(product.getDescription());

        return dto;
    }

    public Product dtoToProduct(ProductDto dto){
        Product product = new Product();
        product.setLibelle(dto.getLibelle());
        product.setPrice(dto.getPrice());
        product.setCompareToPrice(dto.getCompareToPrice());
        product.setActive(dto.getActive());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());

        return product;
    }

    public ProductStockChangedEvent productToProductStockChangedEvent(Product product){
        ProductStockChangedEvent dto= new ProductStockChangedEvent();
        dto.setProductId(product.getIdp());
        dto.setQuantity(product.getQuantity());
        return dto;
    }
}
