package com.example.monolith.web.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerieDto {
    
    private UUID id;

    @NotBlank(message = "{serie.title.notBlank}")
    @Size(min = 1, max = 100, message = "{serie.title.size}")
    private String title;

    @NotNull(message = "{serie.platform.notNull}")
    private StreamingPlatformsEnum streamingPlatform;

    @Size(max = 1000, message = "{serie.synopsis.size}")
    private String synopsis;

    @URL(message = "{serie.coverImage.url}")
    @Size(max = 255, message = "{serie.coverImage.size}")
    private String coverImageUrl;

    @Column(name = "avg_rating", precision = 3, scale = 1)
    private BigDecimal avgRating;
}
