package com.example.monolith.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.monolith.domain.AppUser;
import com.example.monolith.domain.Rating;
import com.example.monolith.domain.Serie;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.repositories.RatingRepository;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.repositories.UserRepository;
import com.example.monolith.services.impl.RatingServiceImpl;
import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.mappers.RatingMapper;
import com.example.monolith.web.model.RatingDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("Rating Service")
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating testRating;
    private RatingDto testRatingDto;
    private AppUser testUser;
    private Serie testSerie;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testRating = TestDataFactory.createRating();
        testRating.setId(testId);
        testRatingDto = TestDataFactory.createRatingDto();
        testRatingDto.setId(testId);
        testUser = TestDataFactory.createUser();
        testSerie = TestDataFactory.createSerie();
    }

    @Nested
    @DisplayName("Create Rating Operations")
    class createRatingOperations {

        @Test
        @DisplayName("should create rating with valid data")
        void createNewRating_Success() {
            // Arrange
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();
            when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
            when(seriesRepository.findById(any())).thenReturn(Optional.of(testSerie));
            when(ratingRepository.save(any())).thenReturn(testRating);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            // Act
            RatingDto result = ratingService.createNewRating(inputDto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getSeriesRating()).isEqualTo(inputDto.getSeriesRating());
            verify(ratingRepository).save(any());
        }

        @Test
        @DisplayName("should throw exception when user not found")
        void createNewRating_UserNotFound() {
            // Arrange
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, 
                () -> ratingService.createNewRating(inputDto));
            verify(ratingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Ratings Operations")
    class GetRatingsOperations {

        @Test
        @DisplayName("should get ratings by serie")
        void getRatingsBySerie_Success() {
            // Arrange
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Rating> ratingPage = new PageImpl<>(List.of(testRating));
            when(ratingRepository.findBySerieId(any(), any())).thenReturn(ratingPage);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            // Act
            Page<RatingDto> result = ratingService.getRatingsBySerie(testId, pageRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getSerieId()).isEqualTo(testRatingDto.getSerieId());
        }

        @Test
        @DisplayName("should get user ratings")
        void getUserRatings_Success() {
            // Arrange
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Rating> ratingPage = new PageImpl<>(List.of(testRating));
            when(ratingRepository.findByUserId(any(), any())).thenReturn(ratingPage);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            // Act
            Page<RatingDto> result = ratingService.getRatingsByUser(testId, pageRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getUserId()).isEqualTo(testRatingDto.getUserId());
        }
    }
}
