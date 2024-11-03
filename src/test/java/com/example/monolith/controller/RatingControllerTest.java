package com.example.monolith.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.model.RatingDto;
import com.example.monolith.web.model.SerieDto;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Rating Controller")
class RatingControllerTest {

    private static final String BASE_URL = "/api/v1/ratings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RatingDto testRatingDto;
    private SerieDto testSerieDto;

    // @BeforeEach
    // void setUp() {
    //     testRatingDto = TestDataFactory.createRatingDtoWithoutId();
    //     testSerieDto = TestDataFactory.createSerieDtoWithoutId();
    // }

    // @Nested
    // @DisplayName("Rating Creation")
    // class CreateRating {

    //     @Test
    //     @DisplayName("should create rating with valid data")
    //     void withValidData() throws Exception {
    //         performPost(testRatingDto)
    //             .andExpect(status().isCreated())
    //             .andExpect(jsonPath("$.seriesRating", is(testRatingDto.getSeriesRating().doubleValue())))
    //             .andExpect(jsonPath("$.id", notNullValue()));
    //     }

    //     @Test
    //     @DisplayName("should return 400 with invalid rating value")
    //     void withInvalidRating() throws Exception {
    //         testRatingDto.setSeriesRating(new BigDecimal("11.0")); // Above maximum

    //         performPost(testRatingDto)
    //             .andExpect(status().isBadRequest())
    //             .andExpect(jsonPath("$.validationErrors.seriesRating").exists());
    //     }
    // }

    // @Nested
    // @DisplayName("Get Ratings By Serie")
    // class GetRatingsBySerie {

    //     @Test
    //     @DisplayName("should get ratings for a serie")
    //     void getRatings() throws Exception {
    //         // First create a rating
    //         String responseJson = performPost(testRatingDto)
    //             .andReturn()
    //             .getResponse()
    //             .getContentAsString();

    //         RatingDto savedRating = objectMapper.readValue(responseJson, RatingDto.class);

    //         // Then get ratings for the serie
    //         mockMvc.perform(get(BASE_URL + "/series/{serieId}", savedRating.getSerieId()))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.content", hasSize(greaterThan(0))))
    //             .andExpect(jsonPath("$.content[0].serieId", is(savedRating.getSerieId().toString())));
    //     }
    // }

    // // Helper methods
    // private ResultActions performPost(RatingDto ratingDto) throws Exception {
    //     return mockMvc.perform(post(BASE_URL)
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(objectMapper.writeValueAsString(ratingDto)));
    // }
}