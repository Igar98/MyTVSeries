package com.example.monolith.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.monolith.domain.Rating;
import com.example.monolith.web.model.RatingDto;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "serieId", source = "serie.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "serieTitle", source = "serie.title")
    RatingDto ratingToRatingDto(Rating rating);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "serie", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Rating ratingDtoToRating(RatingDto ratingDto);
}
