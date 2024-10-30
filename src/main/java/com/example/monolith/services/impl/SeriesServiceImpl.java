package com.example.monolith.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.monolith.domain.Serie;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.services.interfaces.SeriesService;

@Service
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;

    public SeriesServiceImpl(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @Override
    public Serie getSerieEntityById(UUID id) {
         //TODO: Check if return null or ResourceNotFoundException
        return seriesRepository.findById(id).orElse(null);
    }

    @Override
    public Void updateAverageRating(UUID serieId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAverageRating'");
    }
    
}
