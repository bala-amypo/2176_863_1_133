package com.example.demo.service.impl;

import com.example.demo.entity.InventoryLevel;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.InventoryLevelRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.InventoryLevelService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryLevelServiceImpl implements InventoryLevelService {

    private final InventoryLevelRepository inventoryRepo;
    private final StoreRepository storeRepo;
    private final ProductRepository productRepo;

    public InventoryLevelServiceImpl(
            InventoryLevelRepository inventoryRepo,
            StoreRepository storeRepo,
            ProductRepository productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.storeRepo = storeRepo;
        this.productRepo = productRepo;
    }

    @Override
   
public InventoryLevel createOrUpdateInventory(InventoryLevel inv) {

    if (inv.getQuantity() < 0) {
        throw new BadRequestException("Quantity must be >= 0");
    }

    Store store = storeRepo.findById(inv.getStore().getId())
            .orElseThrow(() -> new BadRequestException("Store not found"));

    Product product = productRepo.findById(inv.getProduct().getId())
            .orElseThrow(() -> new BadRequestException("Product not found"));

    return inventoryRepo.findByStoreAndProduct(store, product)
            .map(existing -> {
                existing.setQuantity(inv.getQuantity());
                return inventoryRepo.save(existing);
            })
            .orElseGet(() -> {
                inv.setStore(store);
                inv.setProduct(product);
                return inventoryRepo.save(inv);
            });
}


    @Override
    public List<InventoryLevel> getInventoryForStore(Long storeId) {
        return inventoryRepo.findByStore_Id(storeId);
    }

    @Override
    public List<InventoryLevel> getInventoryForProduct(Long productId) {
        return inventoryRepo.findByProduct_Id(productId);
    }
}
