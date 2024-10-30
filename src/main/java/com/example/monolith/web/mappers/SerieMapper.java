package com.example.monolith.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.monolith.domain.Serie;
import com.example.monolith.web.model.SerieDto;

@Mapper
public interface SerieMapper {
    
    SerieDto serieToSerieDto(SerieDto serie);
    
    @Mapping(target = "ratings", ignore = true)
    Serie serieDtoToSerie(SerieDto serieDto);
}
