package com.example.monolith.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.example.monolith.utils.PostgresTestContainerBase;
import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.model.SerieDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-IT.properties")
@Sql(scripts = "classpath:scripts/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:scripts/insert-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Series Controller")
class SeriesControllerTest extends PostgresTestContainerBase {

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
                @DisplayName("should return only series with ratings")
                void getInitialRanking() throws Exception {
                        // Get and verify ranking based on initial test data
                        mockMvc.perform(get(BASE_URL + "/ranking"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content", hasSize(1))) // Only one series with rating
                                        .andExpect(jsonPath("$.content[0].title", is("Test Serie")))
                                        .andExpect(jsonPath("$.content[0].avgRating", is(8.5)));
                }
        }

        // Helper methods
        private ResultActions performPost(SerieDto serieDto) throws Exception {
                return mockMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(serieDto)));
        }
}