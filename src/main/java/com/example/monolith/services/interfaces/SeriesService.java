package com.example.monolith.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.monolith.domain.Serie;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.web.model.SerieDto;

public interface SeriesService {

    /**
     * Get a serie by its id.
     * 
     * @param id Serie id.
     * @return SerieDto.
     * @throws ResourceNotFoundException If the serie is not found.
     */
    SerieDto getSerieById(UUID id) throws ResourceNotFoundException;

    /**
     * Get a serie entity by its id.
     * Internal use only.
     * 
     * @param id Serie id.
     * @return Serie entity.
     * @throws ResourceNotFoundException If the serie is not found.
     */
    @Internal("Used by other services to get the serie entity directly")
    Serie getSerieEntityById(UUID id) throws ResourceNotFoundException;

    /**
     * Get all series.
     * 
     * @param pageable Pageable object.
     * @return Paged list of SerieDto.
     */
    public Page<SerieDto> getAllSeries(Pageable pageable);

    /**
     * Save a new serie.
     * 
     * @param serieDto SerieDto object.
     * @return SerieDto.
     */
    SerieDto saveNewSerie(SerieDto serieDto);

    /**
     * Update a serie.
     * 
     * @param id       Serie id.
     * @param serieDto SerieDto object.
     * @return SerieDto.
     * @throws ResourceNotFoundException If the serie is not found.
     */
    SerieDto updateSerie(UUID id, SerieDto serieDto) throws ResourceNotFoundException;

    /**
     * Delete a serie.
     * 
     * @param id Serie id.
     * @throws ResourceNotFoundException If the serie is not found.
     */
    void deleteSerie(UUID id) throws ResourceNotFoundException;

    /**
     * Get the series ranking.
     * 
     * @param pageable Pageable object.
     * @return Paged list of SerieDto.
     */
    Page<SerieDto> getSeriesRanking(Pageable pageable);

    /**
     * Check if a serie exists by its id.
     * 
     * @param id Serie id.
     * @return True if the serie exists, false otherwise.
     */
    public boolean existsById(UUID id);
}
