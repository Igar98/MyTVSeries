package com.example.monolith.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monolith.services.interfaces.RatingsService;
import com.example.monolith.web.model.RatingDto;
import com.example.monolith.web.model.responses.SerieRatingListDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Ratings", description = "API endpoints for ratings management")
public class RatingsController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final RatingsService ratingsService;

    public RatingsController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @Operation(summary = "Create a new rating", description = "Creates a new rating for a serie")
    @ApiResponse(responseCode = "201", description = "Rating created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid rating data provided")
    @ApiResponse(responseCode = "409", description = "Rating already exists for this serie and user")
    @PostMapping
    public ResponseEntity<RatingDto> createNewRating(
            @Parameter(description = "Rating data to create") @Valid @RequestBody RatingDto ratingDto) {
        log.debug("Creating new rating: {}", ratingDto);
        RatingDto created = ratingsService.createNewRating(ratingDto);
        log.info("Created new rating with id: {}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all ratings", description = "Returns a list of all ratings grouped by series")
    @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully")
    @GetMapping
    public ResponseEntity<List<SerieRatingListDto>> getAllRatings(
            @Parameter(description = "Page number (0-based)") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer pageSize) {

        log.debug("Getting all ratings with pageNo: {} and pageSize: {}", pageNo, pageSize);
        int page = pageNo != null ? pageNo : DEFAULT_PAGE_NUMBER;
        int size = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        List<SerieRatingListDto> ratings = ratingsService.getAllSeriesWithRatings(pageable);
        log.debug("Retrieved {} rating groups", ratings.size());

        return ResponseEntity.ok(ratings);
    }
}
