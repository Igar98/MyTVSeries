package com.example.monolith.services.impl;

import java.util.UUID;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.monolith.domain.Serie;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.repositories.SerieViewsRepository;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.mappers.SerieMapper;
import com.example.monolith.web.mappers.SerieViewMapper;
import com.example.monolith.web.model.SerieDto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final SerieMapper seriesMapper;

    private final SerieViewsRepository serieViewsRepository;
    private final SerieViewMapper serieViewMapper;

    @Override
    public SerieDto getSerieById(UUID id) throws ResourceNotFoundException {
        return seriesMapper.serieToSerieDto(findSerieOrThrow(id));
    }

    @Override
    public Serie getSerieEntityById(UUID id) throws ResourceNotFoundException {
        return findSerieOrThrow(id);
    }

    @Override
    public Page<SerieDto> getAllSeries(Pageable pageable) {
        return seriesRepository.findAll(pageable)
                .map(seriesMapper::serieToSerieDto);
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
        return serieViewsRepository.findSeriesRanking(pageable)
                .map(serieViewMapper::seriesViewToSerieDto);
    }

    @Override
    public boolean existsById(UUID id) {
        return seriesRepository.existsById(id);
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
