package com.example.demo.controller;

import com.example.demo.entity.TransferSuggestion;
import com.example.demo.service.InventoryBalancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class TransferSuggestionController {

    private final InventoryBalancerService inventoryBalancerService;

    public TransferSuggestionController(InventoryBalancerService inventoryBalancerService) {
        this.inventoryBalancerService = inventoryBalancerService;
    }

    @PostMapping("/generate/{productId}")
    public ResponseEntity<List<TransferSuggestion>> generateSuggestions(@PathVariable Long productId) {
        List<TransferSuggestion> suggestions = inventoryBalancerService.generateSuggestions(productId);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<TransferSuggestion>> getSuggestionsForStore(@PathVariable Long storeId) {
        List<TransferSuggestion> suggestions = inventoryBalancerService.getSuggestionsForStore(storeId);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferSuggestion> getSuggestionById(@PathVariable Long id) {
        TransferSuggestion suggestion = inventoryBalancerService.getSuggestionById(id);
        return ResponseEntity.ok(suggestion);
    }
}