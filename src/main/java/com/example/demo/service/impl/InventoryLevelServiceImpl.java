package com.example.demo.service.impl;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.InventoryLevelRepository;
import com.example.demo.service.InventoryLevelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryLevelServiceImpl implements InventoryLevelService {

    private final InventoryLevelRepository inventoryLevelRepository;

    public InventoryLevelServiceImpl(InventoryLevelRepository inventoryLevelRepository) {
        this.inventoryLevelRepository = inventoryLevelRepository;
    }

    @Override
    public InventoryLevel createOrUpdateInventory(InventoryLevel inventoryLevel) {
        if (inventoryLevel.getQuantity() < 0) {
            throw new BadRequestException("Quantity must be >= 0");
        }
        return inventoryLevelRepository.save(inventoryLevel);
    }

    @Override
    public List<InventoryLevel> getInventoryForStore(Long storeId) {
        return inventoryLevelRepository.findByStore_Id(storeId);
    }

    @Override
    public List<InventoryLevel> getInventoryForProduct(Long productId) {
        return inventoryLevelRepository.findByProduct_Id(productId);
    }
}
