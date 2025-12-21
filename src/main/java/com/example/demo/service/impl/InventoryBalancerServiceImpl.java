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

    private final TransferSuggestionRepository suggestionRepo;
    private final InventoryLevelRepository inventoryRepo;
    private final DemandForecastRepository forecastRepo;
    private final StoreRepository storeRepo;

    public InventoryBalancerServiceImpl(
            TransferSuggestionRepository suggestionRepo,
            InventoryLevelRepository inventoryRepo,
            DemandForecastRepository forecastRepo,
            StoreRepository storeRepo) {
        this.suggestionRepo = suggestionRepo;
        this.inventoryRepo = inventoryRepo;
        this.forecastRepo = forecastRepo;
        this.storeRepo = storeRepo;
    }

    public List<TransferSuggestion> generateSuggestions(Long productId) {
        List<InventoryLevel> inventories = inventoryRepo.findByProduct_Id(productId);
        if (inventories.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }

        List<TransferSuggestion> result = new ArrayList<>();

        for (InventoryLevel inv : inventories) {
            List<DemandForecast> forecasts =
                    forecastRepo.findByStoreAndProductAndForecastDateAfter(
                            inv.getStore(), inv.getProduct(), LocalDate.now());

            if (forecasts.isEmpty()) continue;

            if (inv.getQuantity() > forecasts.get(0).getPredictedDemand()) {
                for (InventoryLevel target : inventories) {
                    if (target.getQuantity() < forecasts.get(0).getPredictedDemand()) {
                        TransferSuggestion ts = new TransferSuggestion();
                        ts.setSourceStore(inv.getStore());
                        ts.setTargetStore(target.getStore());
                        ts.setProduct(inv.getProduct());
                        ts.setQuantity(5);
                        ts.setPriority("MEDIUM");
                        result.add(suggestionRepo.save(ts));
                    }
                }
            }
        }

        if (result.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }
        return result;
    }

    public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
        return suggestionRepo.findBySourceStoreId(storeId);
    }

    public TransferSuggestion getSuggestionById(Long id) {
        return suggestionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found"));
    }
}
