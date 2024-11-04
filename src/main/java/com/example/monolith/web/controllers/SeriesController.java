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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/series")
@Tag(name = "Series", description = "API endpoints for series management")
public class SeriesController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @Operation(summary = "Get a serie by ID", description = "Returns a single serie identified by its ID")
    @ApiResponse(responseCode = "200", description = "Serie found successfully")
    @ApiResponse(responseCode = "404", description = "Serie not found")
    @GetMapping("/{serieId}")
    public ResponseEntity<SerieDto> getSerieById(
            @Parameter(description = "ID of the serie to retrieve") @PathVariable UUID serieId) {
        log.debug("Getting serie with id: {}", serieId);
        SerieDto serie = seriesService.getSerieById(serieId);
        log.debug("Retrieved serie: {}", serie);
        return ResponseEntity.ok(serie);
    }

    @Operation(summary = "Get all series", description = "Returns a paginated list of all series")
    @ApiResponse(responseCode = "200", description = "Series retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<SerieDto>> getAllSeries(
            @Parameter(description = "Page number (0-based)") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer pageSize) {

        log.debug("Getting all series with pageNo: {} and pageSize: {}", pageNo, pageSize);
        int page = pageNo != null ? pageNo : DEFAULT_PAGE_NUMBER;
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        Page<SerieDto> series = seriesService.getAllSeries(pageable);
        log.debug("Retrieved {} series", series.getTotalElements());

        return ResponseEntity.ok(series);
    }

    @Operation(summary = "Create a new serie", description = "Creates a new serie with the provided information")
    @ApiResponse(responseCode = "201", description = "Serie created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid serie data provided")
    @PostMapping
    public ResponseEntity<SerieDto> createNewSerie(
            @Parameter(description = "Serie data to create") @Valid @RequestBody SerieDto serieDto) {
        log.debug("Creating new serie: {}", serieDto);
        SerieDto created = seriesService.saveNewSerie(serieDto);
        log.info("Created new serie with id: {}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing serie", description = "Updates a serie identified by its ID with new information")
    @ApiResponse(responseCode = "200", description = "Serie updated successfully")
    @ApiResponse(responseCode = "404", description = "Serie to update not found")
    @ApiResponse(responseCode = "400", description = "Invalid serie data provided")
    @PutMapping("/{serieId}")
    public ResponseEntity<SerieDto> updateSerie(
            @Parameter(description = "ID of the serie to update") @PathVariable UUID serieId,
            @Parameter(description = "Updated serie data") @Valid @RequestBody SerieDto serieDto) {
        log.debug("Updating serie with id: {} with data: {}", serieId, serieDto);
        SerieDto updated = seriesService.updateSerie(serieId, serieDto);
        log.info("Updated serie with id: {}", serieId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a serie", description = "Deletes a serie identified by its ID")
    @ApiResponse(responseCode = "204", description = "Serie deleted successfully")
    @ApiResponse(responseCode = "404", description = "Serie to delete not found")
    @DeleteMapping("/{serieId}")
    public ResponseEntity<Void> deleteSerie(
            @Parameter(description = "ID of the serie to delete") @PathVariable UUID serieId) {
        log.debug("Deleting serie with id: {}", serieId);
        seriesService.deleteSerie(serieId);
        log.info("Deleted serie with id: {}", serieId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get series ranking", description = "Returns a paginated list of series ordered by their rating")
    @ApiResponse(responseCode = "200", description = "Ranking retrieved successfully")
    @GetMapping("/ranking")
    public ResponseEntity<Page<SerieDto>> getSeriesRanking(
            @Parameter(description = "Page number (0-based)") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer pageSize) {

        log.debug("Getting series ranking with pageNo: {} and pageSize: {}", pageNo, pageSize);
        int page = pageNo != null ? pageNo : DEFAULT_PAGE_NUMBER;
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        Page<SerieDto> ranking = seriesService.getSeriesRanking(pageable);
        log.debug("Retrieved ranking with {} series", ranking.getTotalElements());

        return ResponseEntity.ok(ranking);
    }
}