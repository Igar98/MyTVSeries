package com.example.monolith.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.monolith.domain.Serie;
import com.example.monolith.exceptions.custom.ResourceNotFoundException;
import com.example.monolith.repositories.SerieViewsRepository;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.services.impl.SeriesServiceImpl;
import com.example.monolith.utils.TestDataFactory;
import com.example.monolith.web.mappers.SerieMapper;
import com.example.monolith.web.mappers.SerieViewMapper;
import com.example.monolith.web.model.SerieDto;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    //TODO: Review the implementation of this test.

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private SerieMapper serieMapper;

    @Mock
    private SerieViewsRepository serieViewsRepository;

    @Mock
    private SerieViewMapper serieViewMapper;

    @InjectMocks
    private SeriesServiceImpl seriesService;

    private Serie testSerie;
    private SerieDto testSerieDto;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testSerie = TestDataFactory.createSerie();
        testSerie.setId(testId);
        testSerieDto = TestDataFactory.createSerieDto();
        testSerieDto.setId(testId);
    }

    @Test
    @DisplayName("Should get serie by ID when it exists")
    void getSerieById_Success() {
        when(seriesRepository.findById(testId)).thenReturn(Optional.of(testSerie));
        when(serieMapper.serieToSerieDto(testSerie)).thenReturn(testSerieDto);

        SerieDto result = seriesService.getSerieById(testId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo(testSerieDto.getTitle());
        verify(seriesRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when serie doesn't exist")
    void getSerieById_NotFound() {
        when(seriesRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seriesService.getSerieById(testId));
        verify(seriesRepository).findById(testId);
        verify(serieMapper, never()).serieToSerieDto(any());
    }

    @Test
    @DisplayName("Should save new serie successfully")
    void saveNewSerie_Success() {
        SerieDto inputDto = TestDataFactory.createSerieDtoWithoutId();
        when(serieMapper.serieDtoToSerie(inputDto)).thenReturn(testSerie);
        when(seriesRepository.save(any(Serie.class))).thenReturn(testSerie);
        when(serieMapper.serieToSerieDto(testSerie)).thenReturn(testSerieDto);

        SerieDto result = seriesService.saveNewSerie(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo(testSerieDto.getTitle());
        verify(seriesRepository, times(1)).save(any(Serie.class));
    }

    @Test
    @DisplayName("Should update serie successfully when it exists")
    void updateSerie_Success() {
        when(seriesRepository.findById(testId)).thenReturn(Optional.of(testSerie));
        when(seriesRepository.save(any(Serie.class))).thenReturn(testSerie);
        when(serieMapper.serieToSerieDto(testSerie)).thenReturn(testSerieDto);

        SerieDto result = seriesService.updateSerie(testId, testSerieDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        verify(seriesRepository, times(1)).save(any(Serie.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent serie")
    void updateSerie_NotFound() {
        when(seriesRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> seriesService.updateSerie(testId, testSerieDto));
        verify(seriesRepository, never()).save(any(Serie.class));
    }

    @Test
    @DisplayName("Should get series ranking successfully")
    void getSeriesRanking_Success() {

        //TODO:FIX IT
        // Pageable pageable = PageRequest.of(0, 10);
        // Page<Serie> seriePage = new PageImpl<>(List.of(testSerie));
        
        // when(serieViewsRepository.findSeriesRanking(pageable)).thenReturn(seriePage);
        // when(serieViewMapper.seriesViewToSerieDto(testSerie)).thenReturn(testSerieDto);

        // Page<SerieDto> result = seriesService.getSeriesRanking(pageable);

        // assertThat(result).isNotNull();
        // assertThat(result.getContent()).hasSize(1);
        // assertThat(result.getContent().get(0).getId()).isEqualTo(testId);
        // verify(serieViewsRepository, times(1)).findSeriesRanking(pageable);
        // verify(serieViewMapper, times(1)).seriesViewToSerieDto(pageable);
    }

    @Test
    @DisplayName("Should delete serie successfully when it exists")
    void deleteSerie_Success() {
        when(seriesRepository.existsById(testId)).thenReturn(true);
        doNothing().when(seriesRepository).deleteById(testId);

        seriesService.deleteSerie(testId);

        verify(seriesRepository, times(1)).deleteById(testId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent serie")
    void deleteSerie_NotFound() {
        // Arrange
        when(seriesRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> seriesService.deleteSerie(testId));
        verify(seriesRepository, never()).deleteById(any());
    }
}