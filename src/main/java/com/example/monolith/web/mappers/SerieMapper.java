package com.example.monolith.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.monolith.domain.Serie;
import com.example.monolith.web.model.SerieDto;

//nullValuePropertyMappingStrategy will prevent null values from being mapped
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SerieMapper {
    
    @Mapping(target = "avgRating", ignore = true)
    SerieDto serieToSerieDto(Serie serie);
    
    @Mapping(target = "ratings", ignore = true)
    Serie serieDtoToSerie(SerieDto serieDto);

    /**
     * Updates an existing Serie entity with data from SerieDto
     * Ignores null values and preserves ratings and ID
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    void updateSerieFromDto(SerieDto serieDto, @MappingTarget Serie serie);
}
