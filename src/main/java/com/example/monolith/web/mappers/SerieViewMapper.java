package com.example.monolith.web.mappers;

import org.mapstruct.Mapper;

import com.example.monolith.domain.views.SerieView;
import com.example.monolith.web.model.SerieDto;

@Mapper(componentModel = "spring")
public interface SerieViewMapper {
    
    SerieDto seriesViewToSerieDto(SerieView seriesView);
}
