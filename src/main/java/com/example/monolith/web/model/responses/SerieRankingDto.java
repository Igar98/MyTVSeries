package com.example.monolith.web.model.responses;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieRankingDto {
    private UUID id;
    private String title;
    private StreamingPlatformsEnum streamingPlatform;
    private String synopsis;
    private String coverImageUrl;
    private BigDecimal avgRating;
}