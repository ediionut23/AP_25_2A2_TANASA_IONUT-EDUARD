package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "continents")
@NamedQuery(name = "Continent.findByName",
        query = "SELECT c FROM Continent c WHERE c.name LIKE :name")

public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Continent() {}

    public Continent(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
