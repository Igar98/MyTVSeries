package com.example.monolith.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.UUID;

import com.example.monolith.domain.Serie;
import com.example.monolith.domain.enums.StreamingPlatformsEnum;
import com.example.monolith.web.model.SerieDto;

public class TestDataFactory {

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
}
