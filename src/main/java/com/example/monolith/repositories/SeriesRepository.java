package com.example.monolith.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.monolith.domain.Serie;

public interface SeriesRepository extends JpaRepository<Serie, UUID> {

}
