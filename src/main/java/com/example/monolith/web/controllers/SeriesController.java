package com.example.monolith.web.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.model.SerieDto;


@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {
    
    private final SeriesService seriesService;
    
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<List<SerieDto>> getSeriesRanking(Pageable pageable) {
        return ResponseEntity.ok(seriesService.getSeriesRanking(pageable));
    }
}
