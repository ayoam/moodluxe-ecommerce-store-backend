package com.ayoam.inventoryservice.service;

import com.ayoam.inventoryservice.dto.OrderRequest;
import com.ayoam.inventoryservice.dto.CheckInventoryResponse;
import com.ayoam.inventoryservice.dto.InventoryResponse;
import com.ayoam.inventoryservice.event.ProductStockChangedEvent;
import com.ayoam.inventoryservice.event.StockReservedEvent;
import com.ayoam.inventoryservice.kafka.publisher.InventoryPublisher;
import com.ayoam.inventoryservice.model.Inventory;
import com.ayoam.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    private InventoryRepository inventoryRepository;
    private WebClient.Builder webClientBuiler;
    private InventoryPublisher inventoryPublisher;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, WebClient.Builder webClientBuiler,InventoryPublisher inventoryPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.webClientBuiler = webClientBuiler;
        this.inventoryPublisher=inventoryPublisher;
    }

    public List<InventoryResponse> checkInventory(List<OrderRequest> productsList) {
//        check inventory
        List<InventoryResponse> inventoryResponseList = new ArrayList<>();
        for(OrderRequest req:productsList){
            InventoryResponse inventoryResponse = new InventoryResponse();
            Inventory product = inventoryRepository.findByProductId(req.getProductId()).orElse(null);

            if(product==null){
                throw  new RuntimeException("product "+req.getProductId()+" not found!");
            }

            inventoryResponse.setProductId(req.getProductId());
            inventoryResponse.setInStock(req.getQuantity() <= product.getQuantity());

            inventoryResponseList.add(inventoryResponse);
        }


        return inventoryResponseList;
    }

    public void reserveStock(List<OrderRequest> productsList) {
        for(OrderRequest req:productsList){
            Inventory product = inventoryRepository.findByProductId(req.getProductId()).orElse(null);

            if(product==null){
                throw new RuntimeException("product "+req.getProductId()+" not found!");
            }

            if(product.getQuantity()-req.getQuantity()<0){
                throw new RuntimeException("product "+req.getProductId()+" not in stock!");
            }
            product.setQuantity(product.getQuantity()-req.getQuantity());
            System.out.println(product.getQuantity());
            inventoryRepository.save(product);
        }

        // update products in product service ==> KAFKA
        inventoryPublisher.reserveStockEvent(new StockReservedEvent(productsList));
    }

    public void addProductToInventory(ProductStockChangedEvent product) {
        Inventory inventory = new Inventory();
        inventory.setProductId(product.getProductId());
        inventory.setQuantity(product.getQuantity());
        inventoryRepository.save(inventory);
    }

    public void updateInventory(ProductStockChangedEvent product) {
        Inventory inventory = inventoryRepository.findByProductId(product.getProductId()).orElse(null);
        if(inventory==null){
            throw new RuntimeException("product not found!");
        }
        inventory.setQuantity(product.getQuantity());
        inventoryRepository.save(inventory);
    }

    public void deleteInventory(ProductStockChangedEvent product) {
        Inventory inventory = inventoryRepository.findByProductId(product.getProductId()).orElse(null);
        if(inventory==null){
            throw new RuntimeException("product not found!");
        }
        inventoryRepository.delete(inventory);
    }
}
