package com.example.demo.controller;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.service.InventoryLevelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryLevelService inventoryLevelService;

    public InventoryController(InventoryLevelService inventoryLevelService) {
        this.inventoryLevelService = inventoryLevelService;
    }

    @PostMapping
    public InventoryLevel createOrUpdate(@RequestBody InventoryLevel inventoryLevel) {
        return inventoryLevelService.createOrUpdateInventory(inventoryLevel);
    }

    @GetMapping("/store/{storeId}")
    public List<InventoryLevel> getInventoryForStore(@PathVariable Long storeId) {
        return inventoryLevelService.getInventoryForStore(storeId);
    }

    @GetMapping("/product/{productId}")
    public List<InventoryLevel> getInventoryForProduct(@PathVariable Long productId) {
        return inventoryLevelService.getInventoryForProduct(productId);
    }
}
