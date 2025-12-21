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

    private final TransferSuggestionRepository transferSuggestionRepository;
    private final InventoryLevelRepository inventoryLevelRepository;
    private final DemandForecastRepository demandForecastRepository;
    private final StoreRepository storeRepository;

    public InventoryBalancerServiceImpl(TransferSuggestionRepository transferSuggestionRepository,
                                       InventoryLevelRepository inventoryLevelRepository,
                                       DemandForecastRepository demandForecastRepository,
                                       StoreRepository storeRepository) {
        this.transferSuggestionRepository = transferSuggestionRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.demandForecastRepository = demandForecastRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public List<TransferSuggestion> generateSuggestions(Long productId) {
        List<InventoryLevel> inventories = inventoryLevelRepository.findByProduct_Id(productId);
        if (inventories.isEmpty()) {
            return new ArrayList<>();
        }

        Product product = inventories.get(0).getProduct();
        if (!product.getActive()) {
            throw new BadRequestException("Product is inactive");
        }

        List<TransferSuggestion> suggestions = new ArrayList<>();
        boolean hasForecasts = false;

        for (InventoryLevel inventory : inventories) {
            List<DemandForecast> forecasts = demandForecastRepository.findByStoreAndProductAndForecastDateAfter(
                    inventory.getStore(), product, LocalDate.now());
            if (!forecasts.isEmpty()) {
                hasForecasts = true;
                break;
            }
        }

        if (!hasForecasts) {
            throw new BadRequestException("No forecast found");
        }

        // Simple balancing logic: find overstocked and understocked stores
        for (int i = 0; i < inventories.size(); i++) {
            for (int j = i + 1; j < inventories.size(); j++) {
                InventoryLevel source = inventories.get(i);
                InventoryLevel target = inventories.get(j);

                if (source.getQuantity() > target.getQuantity() + 10) {
                    int transferQty = (source.getQuantity() - target.getQuantity()) / 2;
                    TransferSuggestion suggestion = new TransferSuggestion(
                            source.getStore(), target.getStore(), product, transferQty, "MEDIUM");
                    suggestions.add(transferSuggestionRepository.save(suggestion));
                }
            }
        }

        return suggestions;
    }

    @Override
    public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
        return transferSuggestionRepository.findBySourceStoreId(storeId);
    }

    @Override
    public TransferSuggestion getSuggestionById(Long id) {
        return transferSuggestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found"));
    }
}