package com.example.monolith.web.model.responses;

import java.math.BigDecimal;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieRatingListDto {
    private String serieName;
    private StreamingPlatformsEnum platform;
    private String ratingAuthor;
    private BigDecimal personalRating;
}
