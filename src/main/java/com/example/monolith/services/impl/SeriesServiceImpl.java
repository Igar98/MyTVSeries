package com.example.monolith.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.monolith.domain.Serie;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.mappers.SerieMapper;
import com.example.monolith.web.model.SerieDto;

@Service
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final SerieMapper seriesMapper;

    public SeriesServiceImpl(SeriesRepository seriesRepository, SerieMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    @Override
    public Serie getSerieEntityById(UUID id) {
        // TODO: Check if return null or ResourceNotFoundException
        return seriesRepository.findById(id).orElse(null);
    }

    @Override
    public Void updateAverageRating(UUID serieId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAverageRating'");
    }

    @Override
    public List<SerieDto> getSeriesRanking(Pageable pageable) {
        return seriesRepository.findAll().stream()
                .map(seriesMapper::serieToSerieDto)
                .sorted(Comparator.comparing(SerieDto::getAvgRating).reversed())
                .collect(Collectors.toList());
    }

}
