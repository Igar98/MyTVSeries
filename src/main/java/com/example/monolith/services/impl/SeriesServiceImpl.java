package com.example.monolith.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.monolith.domain.Rating;
import com.example.monolith.domain.Serie;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.mappers.SerieMapper;
import com.example.monolith.web.model.SerieDto;

import jakarta.transaction.Transactional;

@Service
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final SerieMapper seriesMapper;

    public SeriesServiceImpl(SeriesRepository seriesRepository, SerieMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    @Override
    public SerieDto getSerieById(UUID id) throws ResourceNotFoundException {
        return seriesMapper.serieToSerieDto(findSerieOrThrow(id));
    }

    @Override
    public Serie getSerieEntityById(UUID id) throws ResourceNotFoundException {
        return findSerieOrThrow(id);
    }

    @Override
    @Transactional
    public SerieDto saveNewSerie(SerieDto serieDto) {
        Serie serieToSave = seriesMapper.serieDtoToSerie(serieDto);
        serieToSave.setRatings(new HashSet<>());

        return seriesMapper.serieToSerieDto(seriesRepository.save(serieToSave));
    }

    @Override
    @Transactional
    public SerieDto updateSerie(UUID id, SerieDto serieDto) throws ResourceNotFoundException {

        /*
         * 
         * Serie existingSerie = findSerieOrThrow(id);
         * 
         * // Update fields while preserving ratings and avgRating
         * existingSerie.setTitle(serieDto.getTitle());
         * existingSerie.setStreamingPlatform(serieDto.getStreamingPlatform());
         * existingSerie.setSynopsis(serieDto.getSynopsis());
         * existingSerie.setCoverImageUrl(serieDto.getCoverImageUrl());
         * 
         * return seriesMapper.serieToSerieDto(seriesRepository.save(existingSerie));
         */

         //TODO: Check if this works properly.
        Serie existingSerie = findSerieOrThrow(id);
        seriesMapper.updateSerieFromDto(serieDto, existingSerie);
        return seriesMapper.serieToSerieDto(seriesRepository.save(existingSerie));
    }

    @Override
    @Transactional
    public void deleteSerie(UUID id) throws ResourceNotFoundException {
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serie not found with ID: " + id);
        }
        seriesRepository.deleteById(id);
    }

    @Override
    public Page<SerieDto> getSeriesRanking(Pageable pageable) {
        Page<Serie> series = seriesRepository.getSeriesRanking(pageable);
        return series.map(seriesMapper::serieToSerieDto);
    }

    @Override
    @Transactional
    //TODO: Review this method
    public Void updateAverageRating(UUID serieId) throws ResourceNotFoundException {
        Serie serie = findSerieOrThrow(serieId);
        
        if (serie.getRatings().isEmpty()) {
            serie.setAvgRating(BigDecimal.ZERO);
        } else {
            // Calculate new average rating
            BigDecimal avgRating = serie.getRatings().stream()
                .map(Rating::getSeriesRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(serie.getRatings().size()), 1, RoundingMode.HALF_UP);
            
            serie.setAvgRating(avgRating);
        }
        
        seriesRepository.save(serie);
        return null;
    }

    /*
     * Helper method to check if a serie exists in the database.
     * If not, it throws a ResourceNotFoundException.
     * If it exists, it returns the serie.
     */
    private Serie findSerieOrThrow(UUID id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serie not found with ID: " + id));
    }
}
