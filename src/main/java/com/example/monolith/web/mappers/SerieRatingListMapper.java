package com.example.monolith.web.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;
import com.example.monolith.repositories.projections.SerieRatingsProjection;
import com.example.monolith.web.model.responses.SerieRatingListDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring")
public abstract class SerieRatingListMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "platform", source = "platform", qualifiedByName = "stringToPlatform")
    @Mapping(target = "ratings", source = "ratingsMap", qualifiedByName = "jsonToRatingsMap")
    public abstract SerieRatingListDto projectionToDto(SerieRatingsProjection projection);

    public abstract List<SerieRatingListDto> projectionsToDto(List<SerieRatingsProjection> projections);

    @Named("stringToPlatform")
    protected StreamingPlatformsEnum stringToPlatform(String platform) {
        return StreamingPlatformsEnum.valueOf(platform);
    }

    @Named("jsonToRatingsMap")
    protected Map<String, BigDecimal> jsonToRatingsMap(String ratingsJson) {
        try {
            return objectMapper.readValue(ratingsJson, 
                new TypeReference<Map<String, BigDecimal>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing ratings JSON", e);
        }
    }
}