package com.example.monolith.web.mappers;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
    @Mapping(target = "ratings", source = "ratings", qualifiedByName = "jsonToRatingsMap")
    public abstract SerieRatingListDto projectionToDto(SerieRatingsProjection projection);

    public abstract List<SerieRatingListDto> projectionsToDto(List<SerieRatingsProjection> projections);

    @Named("stringToPlatform")
    protected StreamingPlatformsEnum stringToPlatform(String platform) {
        return StreamingPlatformsEnum.valueOf(platform);
    }

    @Named("jsonToRatingsMap")
    protected Map<String, BigDecimal> jsonToRatingsMap(String ratingsJson) {
        try {
            // First parse to List<List<Object>>
            List<List<Object>> ratingsList = objectMapper.readValue(ratingsJson, 
                new TypeReference<List<List<Object>>>() {});
            
            // Make the list a map-like structure. We use LinkedHashMap to preserve insertion order,
            Map<String, BigDecimal> ratingsMap = new LinkedHashMap<>();
            for (List<Object> rating : ratingsList) {
                String username = (String) rating.get(0);
                // We know the second element is a number.
                BigDecimal value = new BigDecimal(rating.get(1).toString());
                ratingsMap.put(username, value);
            }
            return ratingsMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing ratings JSON", e);
        }
    }
}