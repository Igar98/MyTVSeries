package com.example.monolith.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.exceptions.custom.ValidationException;
import com.example.monolith.web.model.RatingDto;

public interface RatingsService {

    /**
     * Create a new rating.
     * 
     * @param ratingDto RatingDto.
     * @return RatingDto.
     * @throws ValidationException ValidationException.
     */
    RatingDto createNewRating(RatingDto ratingDto) throws ValidationException;

    /**
     * Get all ratings for a serie.
     * 
     * @param serieId  Serie ID.
     * @param pageable Pageable.
     * @return Page of RatingDto.
     * @throws ResourceNotFoundException ResourceNotFoundException.
     */
    Page<RatingDto> getRatingsBySerie(UUID serieId, Pageable pageable) throws ResourceNotFoundException;

    /**
     * Get all ratings for a user.
     * 
     * @param userId  User ID.
     * @param pageable Pageable.
     * @return Page of RatingDto.
     * @throws ResourceNotFoundException ResourceNotFoundException.
     */
    Page<RatingDto> getRatingsByUser(UUID userId, Pageable pageable) throws ResourceNotFoundException;
}
