package com.example.monolith.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.model.RatingDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = {
    "classpath:scripts/cleanup.sql",
    "classpath:scripts/insert-test-data.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Rating Controller")
class RatingControllerTest {

    private static final String BASE_URL = "/api/v1/ratings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${test.serie.id}")
    private UUID testSerieId;

    @Value("${test.user.id}")
    private UUID testUserId;

    private RatingDto testRatingDto;

    @BeforeEach
    void setUp() {
        testRatingDto = TestDataFactory.createRatingDtoWithoutId();
        testRatingDto.setSerieId(testSerieId);
        testRatingDto.setUserId(testUserId);
    }

    @Nested
    @DisplayName("Rating Creation")
    class CreateRating {

        @Test
        @DisplayName("should create rating with valida data and update serie average")
        void withValidData() throws Exception {
            // when
            ResultActions response = mockMvc.perform(post("/api/v1/ratings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testRatingDto)));

            // then
            response.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.serieId").value(testSerieId.toString()))
                    .andExpect(jsonPath("$.userId").value(testUserId.toString()));

            // verify serie average was updated
            mockMvc.perform(get("/api/v1/series/{id}", testSerieId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.avgRating").exists())
                    .andExpect(jsonPath("$.avgRating").value(testRatingDto.getSeriesRating()));
        }

        @Test
        @DisplayName("should return 400 with invalid rating value")
        void withInvalidRating() throws Exception {
            testRatingDto.setSeriesRating(new BigDecimal("11.0")); // Above maximum

            performPost(testRatingDto)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors.seriesRating").exists());
        }
    }

    @Nested
    @DisplayName("Get All Ratings")
    class GetAllRatings {

        @Test
        @DisplayName("should get all ratings")
        void getAllRatings() throws Exception {
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThan(0))));
        }
    }

    // Helper methods
    private ResultActions performPost(RatingDto ratingDto) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDto)));
    }
}