package com.example.monolith.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.monolith.domain.views.SerieView;

public interface SerieViewsRepository extends JpaRepository<SerieView, UUID> {
    
    @Query("SELECT sv FROM SerieView sv WHERE sv.avgRating > 0 ORDER BY sv.avgRating DESC")
    Page<SerieView> findSeriesRanking(Pageable pageable);
}
