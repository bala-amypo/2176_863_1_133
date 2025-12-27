package com.example.demo.service;

import com.example.demo.entity.*;


public interface InventoryService{

InventoryLevel createOrUpdateInventory(Long storeId,Long productId,int quantity);

}