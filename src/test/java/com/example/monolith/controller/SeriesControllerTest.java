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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
@DisplayName("Series Controller")
@Sql(scripts = {
        "classpath:scripts/cleanup.sql",
        "classpath:scripts/insert-test-data.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class SeriesControllerTest {

        private static final String BASE_URL = "/api/v1/series";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private SerieDto testSerieDto;

        @Value("${test.user.id}")
        private UUID testUserId;

        @BeforeEach
        void setUp() {
                testSerieDto = TestDataFactory.createSerieDtoWithoutId();
        }

        @Nested
        @DisplayName("Serie Creation")
        class CreateSerie {

                @Test
                @DisplayName("should create serie with valid data")
                void withValidData() throws Exception {
                        performPost(testSerieDto)
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.title", is(testSerieDto.getTitle())))
                                        .andExpect(jsonPath("$.id", notNullValue()));
                }

                @Test
                @DisplayName("should return 400 with empty title")
                void withEmptyTitle() throws Exception {
                        testSerieDto.setTitle("");

                        performPost(testSerieDto)
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.validationErrors.title").exists());
                }

                @Test
                @DisplayName("should return 400 with null platform")
                void withNullPlatform() throws Exception {
                        testSerieDto.setStreamingPlatform(null);

                        performPost(testSerieDto)
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.validationErrors.streamingPlatform").exists());
                }
        }

        @Nested
        @DisplayName("Serie Retrieval")
        class GetSerie {

                private SerieDto savedSerie;

                @BeforeEach
                void setUp() throws Exception {
                        String responseJson = performPost(testSerieDto)
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();

                        savedSerie = objectMapper.readValue(responseJson, SerieDto.class);
                }

                @Test
                @DisplayName("should get serie by ID when exists")
                void existingSerie() throws Exception {
                        mockMvc.perform(get(BASE_URL + "/{id}", savedSerie.getId()))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.title", is(testSerieDto.getTitle())))
                                        .andExpect(jsonPath("$.id", is(savedSerie.getId().toString())));
                }

                @Test
                @DisplayName("should return 404 when serie not found")
                void nonExistingSerie() throws Exception {
                        mockMvc.perform(get(BASE_URL + "/{id}", java.util.UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Serie Update")
        class UpdateSerie {

                private SerieDto savedSerie;

                @BeforeEach
                void setUp() throws Exception {
                        String responseJson = performPost(testSerieDto)
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();

                        savedSerie = objectMapper.readValue(responseJson, SerieDto.class);
                }

                @Test
                @DisplayName("should update serie with valid data")
                void withValidData() throws Exception {
                        savedSerie.setTitle("Updated Title");

                        mockMvc.perform(put(BASE_URL + "/{id}", savedSerie.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(savedSerie)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.title", is("Updated Title")))
                                        .andExpect(jsonPath("$.id", is(savedSerie.getId().toString())));
                }

                @Test
                @DisplayName("should return 404 when updating non-existing serie")
                void nonExistingSerie() throws Exception {
                        mockMvc.perform(put(BASE_URL + "/{id}", java.util.UUID.randomUUID())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(savedSerie)))
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
        @DisplayName("Serie Deletion")
        class DeleteSerie {

                private SerieDto savedSerie;

                @BeforeEach
                void setUp() throws Exception {
                        String responseJson = performPost(testSerieDto)
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();

                        savedSerie = objectMapper.readValue(responseJson, SerieDto.class);
                }

                @Test
                @DisplayName("should delete existing serie")
                void existingSerie() throws Exception {
                        mockMvc.perform(delete(BASE_URL + "/{id}", savedSerie.getId()))
                                        .andExpect(status().isNoContent());

                        mockMvc.perform(get(BASE_URL + "/{id}", savedSerie.getId()))
                                        .andExpect(status().isNotFound());
                }

                @Test
                @DisplayName("should return 404 when deleting non-existing serie")
                void nonExistingSerie() throws Exception {
                        mockMvc.perform(delete(BASE_URL + "/{id}", java.util.UUID.randomUUID()))
                                        .andExpect(status().isNotFound());
                }
        }

        @Nested
@DisplayName("Series Ranking")
class SeriesRanking {

    @Test
    @DisplayName("should return ordered ranking")
    void getRanking() throws Exception {
        // Create first serie
        SerieDto firstSerie = TestDataFactory.createSerieDtoWithoutId();
        firstSerie.setTitle("First Serie");
        ResultActions firstSerieResponse = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstSerie)))
                .andExpect(status().isCreated());
        
        SerieDto savedFirstSerie = objectMapper.readValue(
            firstSerieResponse.andReturn().getResponse().getContentAsString(),
            SerieDto.class
        );

        // Create second serie
        SerieDto secondSerie = TestDataFactory.createSerieDtoWithoutId();
        secondSerie.setTitle("Second Serie");
        ResultActions secondSerieResponse = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondSerie)))
                .andExpect(status().isCreated());

        SerieDto savedSecondSerie = objectMapper.readValue(
            secondSerieResponse.andReturn().getResponse().getContentAsString(),
            SerieDto.class
        );

        // Add ratings to series
        RatingDto firstRating = TestDataFactory.createRatingDtoWithoutId(testUserId, savedFirstSerie.getId());
        firstRating.setSeriesRating(new BigDecimal("8.5"));
        mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRating)))
                .andExpect(status().isCreated());

        RatingDto secondRating = TestDataFactory.createRatingDtoWithoutId(testUserId, savedSecondSerie.getId());
        secondRating.setSeriesRating(new BigDecimal("9.0"));
        mockMvc.perform(post("/api/v1/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRating)))
                .andExpect(status().isCreated());

        // Get and verify ranking
        mockMvc.perform(get(BASE_URL + "/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3))) // Including the test serie
                .andExpect(jsonPath("$.content[0].title", is("Second Serie")))
                .andExpect(jsonPath("$.content[0].avgRating", is(9.0)))
                .andExpect(jsonPath("$.content[1].title", is("First Serie")))
                .andExpect(jsonPath("$.content[1].avgRating", is(8.5)));
    }
}

        // Helper methods
        private ResultActions performPost(SerieDto serieDto) throws Exception {
                return mockMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(serieDto)));
        }
}