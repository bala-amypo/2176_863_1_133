package com.example.demo.service.impl;

import com.example.demo.entity.Store;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store createStore(Store store) {
        if (storeRepository.findByStoreName(store.getStoreName()) != null) {
            throw new BadRequestException("Store name already exists");
        }
        return storeRepository.save(store);
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store updateStore(Long id, Store update) {
        Store store = getStoreById(id);
        store.setStoreName(update.getStoreName());
        store.setRegion(update.getRegion());
        store.setAddress(update.getAddress());
        return storeRepository.save(store);
    }

    public void deactivateStore(Long id) {
        Store store = getStoreById(id);
        store.setActive(false);
        storeRepository.save(store);
    }
}
