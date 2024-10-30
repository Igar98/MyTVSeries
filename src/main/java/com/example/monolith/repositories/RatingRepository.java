package com.example.monolith.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.monolith.domain.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    public Optional<Rating> findBySerieIdAndUserId(UUID serieId, UUID userId);

    public List<Rating> findBySerieId(UUID serieId);

    public List<Rating> findByUserId(UUID userId);
}
