package com.example.monolith.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.monolith.domain.Serie;
import com.example.monolith.web.model.SerieDto;

public interface SeriesService {
    
    @Internal("Used by other services to get the serie entity directly")
    Serie getSerieEntityById(UUID id);

    Void updateAverageRating(UUID serieId);

    /**
     * Get the series ranking.
     * @param pageable Pageable object.
     * @return List of SerieDto.
     */
    List<SerieDto> getSeriesRanking (Pageable pageable);
}
