package com.example.monolith.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monolith.services.interfaces.RatingsService;
import com.example.monolith.web.model.RatingDto;
import com.example.monolith.web.model.SerieDto;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/ratings")
public class RatingsController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final RatingsService ratingsService;

    public RatingsController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @PostMapping("path")
    public ResponseEntity<RatingDto> createNewRating(@Valid @RequestBody RatingDto ratingDto) {
        return new ResponseEntity<>(ratingsService.createNewRating(ratingDto), HttpStatus.CREATED);
    }

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    } 
}
