package com.ayoam.productservice.persistance;

import com.ayoam.productservice.converter.ProductConverter;
import com.ayoam.productservice.kafka.publisher.ProductPublisher;
import com.ayoam.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;


public class ProductEntityListener {
    private ProductPublisher productPublisher;
    private ProductConverter productConverter;

    @Autowired
    public ProductEntityListener(ProductPublisher productPublisher, ProductConverter productConverter) {
        this.productPublisher = productPublisher;
        this.productConverter = productConverter;
    }

    public ProductEntityListener() {

    }

    @PostPersist
    public void productCreated(Product product){
        productPublisher.createProductEvent(productConverter.productToProductStockChangedEvent(product));
    }
    @PostUpdate
    public void productUpdated(Product product){
        productPublisher.updateProductEvent(productConverter.productToProductStockChangedEvent(product));
    }

    @PostRemove
    public void productDeleted(Product product){
        productPublisher.deleteProductEvent(productConverter.productToProductStockChangedEvent(product));
    }
}
