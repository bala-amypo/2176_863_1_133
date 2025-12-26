package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.InventoryBalancerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryBalancerServiceImpl implements InventoryBalancerService {

    private final TransferSuggestionRepository transferRepo;
    private final InventoryLevelRepository inventoryRepo;
    private final DemandForecastRepository forecastRepo;
    private final StoreRepository storeRepo;

    public InventoryBalancerServiceImpl(
            TransferSuggestionRepository transferRepo,
            InventoryLevelRepository inventoryRepo,
            DemandForecastRepository forecastRepo,
            StoreRepository storeRepo) {

        this.transferRepo = transferRepo;
        this.inventoryRepo = inventoryRepo;
        this.forecastRepo = forecastRepo;
        this.storeRepo = storeRepo;
    }

    @Override
    public List<TransferSuggestion> generateSuggestions(Long productId) {
        List<InventoryLevel> inventories = inventoryRepo.findByProduct_Id(productId);

        if (inventories.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }

        List<TransferSuggestion> suggestions = new ArrayList<>();

        for (InventoryLevel source : inventories) {
            for (InventoryLevel target : inventories) {
                if (!source.getStore().getId().equals(target.getStore().getId())
                        && source.getQuantity() > target.getQuantity()) {

                    TransferSuggestion ts = new TransferSuggestion();
                    ts.setProduct(source.getProduct());
                    ts.setSourceStore(source.getStore());
                    ts.setTargetStore(target.getStore());
                    ts.setSuggestedQuantity(5);
                    ts.setReason("Auto balance");

                    suggestions.add(transferRepo.save(ts));
                }
            }
        }
        return suggestions;
    }

    @Override
    public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
        return transferRepo.findBySourceStore_Id(storeId);
    }

    @Override
    public TransferSuggestion getSuggestionById(Long id) {
        return transferRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found"));
    }
}
