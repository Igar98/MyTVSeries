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
import com.example.monolith.exceptions.custom.ValidationException;
import com.example.monolith.repositories.RatingRepository;
import com.example.monolith.services.impl.AppUserServiceImpl;
import com.example.monolith.services.impl.RatingsServiceImpl;
import com.example.monolith.services.impl.SeriesServiceImpl;
import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.mappers.RatingMapper;
import com.example.monolith.web.model.RatingDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("Rating Service")
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private AppUserServiceImpl userService;

    @Mock
    private SeriesServiceImpl seriesService;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingsServiceImpl ratingService;

    private Rating testRating;
    private RatingDto testRatingDto;
    private AppUser testUser;
    private Serie testSerie;
    private UUID testId;

    private PageRequest pageRequest;
    private Page<Rating> ratingPage;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testRating = TestDataFactory.createRating();
        testRating.setId(testId);
        testRatingDto = TestDataFactory.createRatingDto();
        testRatingDto.setId(testId);
        testUser = TestDataFactory.createUser();
        testSerie = TestDataFactory.createSerie();

        pageRequest = PageRequest.of(0, 10);
        ratingPage = new PageImpl<>(List.of(testRating));
    }

    @Nested
    @DisplayName("Create Rating Operations")
    class createRatingOperations {

        @BeforeEach
        void setUp() {
            when(ratingRepository.findBySerieIdAndUserId(any(), any())).thenReturn(Optional.empty());
            when(seriesService.existsById(any())).thenReturn(true);
            when(userService.existsById(any())).thenReturn(true);
        }

        @Test
        @DisplayName("should create rating with valid data")
        void createNewRating_Success() {
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();

            when(ratingMapper.ratingDtoToRating(any())).thenReturn(testRating);
            when(userService.getUserEntityById(any())).thenReturn(testUser);
            when(seriesService.getSerieEntityById(any())).thenReturn(testSerie);
            when(ratingRepository.save(any())).thenReturn(testRating);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            RatingDto result = ratingService.createNewRating(inputDto);

            assertThat(result).isNotNull();
            assertThat(result.getSeriesRating()).isEqualTo(inputDto.getSeriesRating());

            verify(ratingRepository, times(1)).findBySerieIdAndUserId(any(), any());
            verify(seriesService, times(1)).existsById(any());
            verify(userService, times(1)).existsById(any());
            verify(userService, times(1)).getUserEntityById(any());
            verify(seriesService, times(1)).getSerieEntityById(any());
            verify(ratingRepository, times(1)).save(any());
            verify(ratingMapper, times(1)).ratingToRatingDto(any());
        }

        @Test
        @DisplayName("should throw ValidationException when rating already exists")
        void createNewRating_AlreadyExists() {
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();
            when(ratingRepository.findBySerieIdAndUserId(any(), any()))
                .thenReturn(Optional.of(testRating));

            ValidationException exception = assertThrows(ValidationException.class,
                () -> ratingService.createNewRating(inputDto));

            assertThat(exception.getErrors())
                .containsKey("rating")
                .containsValue("Rating already exists for this serie.");

            verify(ratingRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw ValidationException when serie doesn't exist")
        void createNewRating_SerieNotExists() {
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();
            when(seriesService.existsById(any())).thenReturn(false);

            ValidationException exception = assertThrows(ValidationException.class,
                () -> ratingService.createNewRating(inputDto));

            assertThat(exception.getErrors())
                .containsKey("serie")
                .containsValue("Serie does not exist.");

            verify(ratingRepository, never()).save(any());
            verify(seriesService, times(1)).existsById(any());
        }
        
        @Test
        @DisplayName("should throw ValidationException when user doesn't exist")
        void createNewRating_UserNotExists() {
            RatingDto inputDto = TestDataFactory.createRatingDtoWithoutId();
            when(userService.existsById(any())).thenReturn(false);

            ValidationException exception = assertThrows(ValidationException.class,
            () -> ratingService.createNewRating(inputDto));
            
            assertThat(exception.getErrors())
            .containsKey("user")
            .containsValue("User does not exist.");
            
            verify(ratingRepository, never()).save(any());
            verify(userService, times(1)).existsById(any());
        }
    }

    @Nested
    @DisplayName("Get Ratings Operations")
    class GetRatingsOperations {

        @Test
        @DisplayName("should get ratings by serie")
        void getRatingsBySerie_Success() {
            when(seriesService.existsById(any())).thenReturn(true);
            when(ratingRepository.findBySerieId(any(), any())).thenReturn(ratingPage);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            Page<RatingDto> result = ratingService.getRatingsBySerie(testId, pageRequest);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getSerieId()).isEqualTo(testRatingDto.getSerieId());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when serie not found")
        void getRatingsBySerie_SerieNotFound() {
            when(seriesService.existsById(any())).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                () -> ratingService.getRatingsBySerie(testId, pageRequest));

            verify(ratingRepository, never()).findBySerieId(any(), any());
        }

        @Test
        @DisplayName("should get user ratings")
        void getUserRatings_Success() {

            when(userService.existsById(any())).thenReturn(true);
            when(ratingRepository.findByUserId(any(), any())).thenReturn(ratingPage);
            when(ratingMapper.ratingToRatingDto(any())).thenReturn(testRatingDto);

            Page<RatingDto> result = ratingService.getRatingsByUser(testId, pageRequest);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getUserId()).isEqualTo(testRatingDto.getUserId());

            verify(userService, times(1)).existsById(any());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when user not found")
        void getUserRatings_UserNotFound() {
            when(userService.existsById(any())).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                () -> ratingService.getRatingsByUser(testId, pageRequest));

            verify(ratingRepository, never()).findByUserId(any(), any());
        }
    }
}
