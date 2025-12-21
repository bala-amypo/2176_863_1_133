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
    public InventoryLevel createOrUpdateInventory(InventoryLevel inv) {
        if (inv.getQuantity() < 0) {
            throw new BadRequestException("Quantity must be >= 0");
        }

        InventoryLevel existing = inventoryLevelRepository.findByStoreAndProduct(inv.getStore(), inv.getProduct());
        if (existing != null) {
            existing.setQuantity(inv.getQuantity());
            return inventoryLevelRepository.save(existing);
        }

        return inventoryLevelRepository.save(inv);
    }

    @Override
    public List<InventoryLevel> getInventoryForStore(Long storeId) {
        return inventoryLevelRepository.findByStore_Id(storeId);
    }

    @Override
    public List<InventoryLevel> getInventoryForProduct(Long productId) {
        return inventoryLevelRepository.findByProduct_Id(productId);
    }

    @Override
    public InventoryLevel getInventory(Long storeId, Long productId) {
        List<InventoryLevel> inventories = inventoryLevelRepository.findByStore_Id(storeId);
        return inventories.stream()
                .filter(inv -> inv.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}