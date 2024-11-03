package com.example.monolith.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.monolith.domain.Rating;
import com.example.monolith.repositories.projections.SerieRatingsProjection;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    public Optional<Rating> findBySerieIdAndUserId(UUID serieId, UUID userId);

    public Page<Rating> findBySerieId(UUID serieId, Pageable pageable);

    public Page<Rating> findByUserId(UUID userId, Pageable pageable);

    //TODO: Change to the postgresql when launching to prod
    // @Query(nativeQuery = true, value = """
    //     SELECT 
    //         s.title as serie_name,
    //         s.streaming_platform as platform,
    //         jsonb_object_agg(u.username, r.series_rating) as ratings_map
    //     FROM series s
    //     JOIN ratings r ON s.id = r.series_id
    //     JOIN users u ON r.user_id = u.id
    //     GROUP BY s.title, s.streaming_platform
    //     ORDER BY s.title ASC
    // """)
    @Query(nativeQuery = true, value = """
        SELECT 
            s.title as serie_name,
            s.streaming_platform as platform,
            LISTAGG(
                CONCAT('{"', u.username, '":"', CAST(r.series_rating AS VARCHAR), '"}'),
                ','
            ) WITHIN GROUP (ORDER BY u.username) as ratings_map
        FROM series s
        JOIN ratings r ON s.id = r.series_id
        JOIN users u ON r.user_id = u.id
        GROUP BY s.title, s.streaming_platform
        ORDER BY s.title ASC
    """)
    List<SerieRatingsProjection> findAllSeriesRatingsGrouped(Pageable pageable);
}
