package com.example.monolith.web.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.model.SerieDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    
    private final SeriesService seriesService;
    
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping("/{serieId}")
    public ResponseEntity<SerieDto> getSerieById(@PathVariable UUID serieId) {
        return ResponseEntity.ok(seriesService.getSerieById(serieId));
    }

    @GetMapping
    public ResponseEntity<Page<SerieDto>> getAllSeries(
        @RequestParam(required = false) Integer pageNo,
        @RequestParam(required = false) Integer pageSize) {

        int page = pageNo != null ? pageNo : DEFAULT_PAGE_NUMBER;
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(seriesService.getAllSeries(pageable));
    }
    

    @PostMapping
    public ResponseEntity<SerieDto> createNewSerie(@Valid @RequestBody SerieDto serieDto) {
        return new ResponseEntity<>(seriesService.saveNewSerie(serieDto), HttpStatus.CREATED);
    }

    @PutMapping("/{serieId}")
    public ResponseEntity<SerieDto> updateSerie(
            @PathVariable UUID serieId,
            @Valid @RequestBody SerieDto serieDto) {
        return ResponseEntity.ok(seriesService.updateSerie(serieId, serieDto));
    }

    @DeleteMapping("/{serieId}")
    public ResponseEntity<Void> deleteSerie(@PathVariable UUID serieId) {
        seriesService.deleteSerie(serieId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<Page<SerieDto>> getSeriesRanking(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize) {
        
        int page = pageNo != null ? pageNo : DEFAULT_PAGE_NUMBER;
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(seriesService.getSeriesRanking(pageable));
    }
}
