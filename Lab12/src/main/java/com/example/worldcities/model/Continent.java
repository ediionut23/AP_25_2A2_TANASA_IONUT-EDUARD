package com.example.worldcities.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "continents")
@Data
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
} 