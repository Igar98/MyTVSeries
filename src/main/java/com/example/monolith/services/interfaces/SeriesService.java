package com.example.monolith.services.interfaces;

import java.util.UUID;

import com.example.monolith.domain.Serie;

public interface SeriesService {
    
    @Internal("Used by other services to get the serie entity directly")
    Serie getSerieEntityById(UUID id);

    Void updateAverageRating(UUID serieId);
}
