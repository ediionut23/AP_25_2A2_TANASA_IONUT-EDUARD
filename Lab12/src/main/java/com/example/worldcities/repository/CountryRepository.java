package com.example.worldcities.repository;

import com.example.worldcities.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface CountryRepository extends JpaRepository<Country, Long> {
    @RestResource(path = "byCode")
    Country findByCode(@Param("code") String code);
} 