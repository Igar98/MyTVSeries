package com.example.monolith.web.model.responses;

import java.math.BigDecimal;
import java.util.Map;

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
    private Map<String, BigDecimal> ratings;
}
