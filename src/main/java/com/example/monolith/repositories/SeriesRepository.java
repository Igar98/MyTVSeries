package com.example.monolith.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.monolith.domain.Serie;

public interface SeriesRepository extends JpaRepository<Serie, UUID> {

    @Query("SELECT s FROM Serie s WHERE s.avgRating IS NOT NULL ORDER BY s.avgRating DESC")
    Page<Serie> getSeriesRanking(Pageable pageable);
}
