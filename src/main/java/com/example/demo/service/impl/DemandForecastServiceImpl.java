package com.example.demo.service.impl;

import com.example.demo.entity.DemandForecast;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.DemandForecastRepository;
import com.example.demo.service.DemandForecastService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    private final DemandForecastRepository repo;

    public DemandForecastServiceImpl(DemandForecastRepository repo) {
        this.repo = repo;
    }

    public DemandForecast createForecast(DemandForecast forecast) {
        if (!forecast.getForecastDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Forecast date must be in the future");
        }
        if (forecast.getPredictedDemand() < 0) {
            throw new BadRequestException("Predicted demand must be >= 0");
        }
        return repo.save(forecast);
    }

    public List<DemandForecast> getForecastsForStore(Long storeId) {
        return repo.findByStore_Id(storeId);
    }
}
