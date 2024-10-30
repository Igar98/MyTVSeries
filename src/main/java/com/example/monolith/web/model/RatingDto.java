package com.example.monolith.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

    private UUID id;

    @NotNull(message = "{rating.user.notNull}")
    private UUID userId;

    @NotNull(message = "{rating.serie.notNull}")
    private UUID serieId;

    @NotNull(message = "{rating.value.notNull}")
    @Digits(integer = 2, fraction = 1, message = "{rating.value.range}")
    @DecimalMin(value = "0.0", message = "{rating.value.range.min}")
    @DecimalMax(value = "10.0", message = "{rating.value.range.max}")
    private BigDecimal seriesRating;

    private LocalDateTime createdAt;

    // Aditional attributes to show more info in the list.
    private String username;
    private String serieTitle;
}
