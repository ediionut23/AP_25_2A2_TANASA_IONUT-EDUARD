package com.example.worldcities.repository;

import com.example.worldcities.model.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContinentRepository extends JpaRepository<Continent, Long> {
} 