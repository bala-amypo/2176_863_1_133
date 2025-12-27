package com.example.demo.service;


public interface InventoryService{

InventoryLevel createOrUpdateInventory(Long storeId,Long productId,int quantity);

}