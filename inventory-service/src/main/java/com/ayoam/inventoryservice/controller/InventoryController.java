package com.ayoam.inventoryservice.controller;

import com.ayoam.inventoryservice.dto.InventoryResponse;
import com.ayoam.inventoryservice.dto.OrderRequest;
import com.ayoam.inventoryservice.dto.CheckInventoryResponse;
import com.ayoam.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/inventories/checkInventory")
    public ResponseEntity<List<InventoryResponse>> checkInventory(@RequestBody List<OrderRequest> productsList){
        return new ResponseEntity<List<InventoryResponse>>(inventoryService.checkInventory(productsList), HttpStatus.OK);
    }

    @PostMapping("/inventories/reserveStock")
    public ResponseEntity<?> reserveStock(@RequestBody List<OrderRequest> productsList){
        inventoryService.reserveStock(productsList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
