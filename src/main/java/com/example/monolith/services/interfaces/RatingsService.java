package com.example.monolith.services.interfaces;

import com.example.monolith.exceptions.custom.ValidationException;
import com.example.monolith.web.model.RatingDto;

public interface RatingsService {
    
    //TODO: Check JavaDoc
    /**
     * Create a new rating.
     * 
     * @param ratingDto RatingDto.
     * @return RatingDto.
     * @throws ValidationException ValidationException.
     */
    RatingDto createNewRating(RatingDto ratingDto) throws ValidationException;
}
