package com.example.worldcities.repository;

import com.example.worldcities.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
} 