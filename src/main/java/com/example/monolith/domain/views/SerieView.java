package com.example.monolith.domain.views;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "series_with_ratings")
public class SerieView {
    
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "streaming_platform", nullable = false, length = 50)
    private StreamingPlatformsEnum streamingPlatform;

    @Column(columnDefinition = "TEXT", length = 1000)
    private String synopsis;

    @Column(name = "cover_image_url", length = 255)
    private String coverImageUrl;

    @Column(name = "avg_rating", precision = 3, scale = 1)
    private BigDecimal avgRating;
}