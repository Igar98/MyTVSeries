package com.example.monolith.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.monolith.domain.Rating;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.exceptions.custom.ValidationException;
import com.example.monolith.repositories.RatingRepository;
import com.example.monolith.services.interfaces.AppUserService;
import com.example.monolith.services.interfaces.RatingsService;
import com.example.monolith.services.interfaces.SeriesService;
import com.example.monolith.web.mappers.RatingMapper;
import com.example.monolith.web.model.RatingDto;

import jakarta.transaction.Transactional;

@Service
public class RatingServiceImpl implements RatingsService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    private final AppUserService userService;
    private final SeriesService seriesService;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper,
            AppUserService appUserService,
            SeriesService seriesService) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.userService = appUserService;
        this.seriesService = seriesService;
    }

    @Override
    @Transactional
    public RatingDto createNewRating(RatingDto ratingDto) throws ValidationException {

        // Perform service-layer/business validation on the new rating.
        validateNewRating(ratingDto);

        // Map the RatingDto to a Rating entity and set the User and Serie.
        Rating newRating = ratingMapper.ratingDtoToRating(ratingDto);
        newRating.setUser(userService.getUserEntityById(ratingDto.getUserId()));
        newRating.setSerie(seriesService.getSerieEntityById(ratingDto.getSerieId()));

        // Save the new rating.
        Rating savedRating = ratingRepository.save(newRating);

        // Update the average rating for the serie.
        updateSerieAverageRating(savedRating.getSerie().getId());

        return ratingMapper.ratingToRatingDto(savedRating);
    }

    
    @Override
    public Page<RatingDto> getRatingsBySerie(UUID serieId, Pageable pageable) throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRatingsBySerie'");
    }

    @Override
    public Page<RatingDto> getRatingsByUser(UUID userId, Pageable pageable) throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRatingsByUser'");
    }

    /**
     * Calls the Series Service to update the average rating for a serie.
     * 
     * @param serieId Serie ID.
     */
    @Transactional
    public void updateSerieAverageRating(UUID serieId) {
        seriesService.updateAverageRating(serieId);
    }

    /*
     * Perform service-layer/business validation on the new rating.
     */
    private void validateNewRating(RatingDto ratingDto) throws ValidationException {

        Map<String, String> errors = new HashMap<>();

        // Check that the rating for this series does not exist.
        if (ratingRepository.findBySerieIdAndUserId(ratingDto.getSerieId(), ratingDto.getUserId()).isPresent()) {
            errors.put("rating", "Rating already exists for this serie.");
        }

        // Check that the Serie already exist.
        if (ratingRepository.findBySerieId(ratingDto.getSerieId(), Pageable.unpaged()).isEmpty()) {
            errors.put("serie", "Serie does not exist.");
        }

        // Check that the User already exist.
        if (ratingRepository.findByUserId(ratingDto.getUserId(), Pageable.unpaged()).isEmpty()) {
            errors.put("user", "User does not exist.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("New Ratings validation failed", errors);
        }
    }

}
