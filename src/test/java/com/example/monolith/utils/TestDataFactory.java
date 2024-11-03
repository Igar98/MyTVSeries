package com.example.monolith.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import com.example.monolith.domain.AppUser;
import com.example.monolith.domain.Rating;
import com.example.monolith.domain.Serie;
import com.example.monolith.domain.enums.StreamingPlatformsEnum;
import com.example.monolith.web.model.AppUserDto;
import com.example.monolith.web.model.RatingDto;
import com.example.monolith.web.model.SerieDto;

public class TestDataFactory {

    /*  SERIES */
    public static Serie createSerie() {
        Serie serie = new Serie();
        serie.setId(UUID.randomUUID());
        serie.setTitle("Test Serie");
        serie.setStreamingPlatform(StreamingPlatformsEnum.NETFLIX);
        serie.setSynopsis("Test Synopsis");
        serie.setAvgRating(new BigDecimal("8.5"));
        serie.setCoverImageUrl("http://test-image.com/cover.jpg");
        serie.setRatings(new HashSet<>());
        return serie;
    }

    public static SerieDto createSerieDto() {
        return SerieDto.builder()
                .id(UUID.randomUUID())
                .title("Test Serie")
                .streamingPlatform(StreamingPlatformsEnum.NETFLIX)
                .synopsis("Test Synopsis")
                .avgRating(new BigDecimal("8.5"))
                .coverImageUrl("http://test-image.com/cover.jpg")
                .build();
    }

    public static SerieDto createSerieDtoWithoutId() {
        return SerieDto.builder()
                .title("Test Serie")
                .streamingPlatform(StreamingPlatformsEnum.NETFLIX)
                .synopsis("Test Synopsis")
                .avgRating(new BigDecimal("8.5"))
                .coverImageUrl("http://test-image.com/cover.jpg")
                .build();
    }

    public static AppUser createUser() {
        AppUser user = new AppUser();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");
        user.setRatings(new HashSet<>());
        return user;
    }

    /*  RATINGS */
    public static Rating createRating() {
        Rating rating = new Rating();
        rating.setId(UUID.randomUUID());
        rating.setUser(createUser());
        rating.setSerie(createSerie());
        rating.setSeriesRating(new BigDecimal("8.5"));
        rating.setCreatedAt(LocalDateTime.now());
        return rating;
    }

    public static RatingDto createRatingDto() {
        return RatingDto.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .serieId(UUID.randomUUID())
                .seriesRating(new BigDecimal("8.5"))
                .username("testUser")
                .serieTitle("Test Serie")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static RatingDto createRatingDtoWithoutId() {
        return RatingDto.builder()
                .userId(UUID.randomUUID())
                .serieId(UUID.randomUUID())
                .seriesRating(new BigDecimal("8.5"))
                .build();
    }

    public static RatingDto createRatingDtoWithoutId(UUID userId, UUID serieId) {
        return RatingDto.builder()
                .userId(userId)
                .serieId(serieId)
                .seriesRating(new BigDecimal("8.5"))
                .build();
    }

    /*  APP USERS */
    public static AppUserDto createAppUserDtoWithoutId() {
        return AppUserDto.builder()
                .username("testUser")
                .build();
    }
}
