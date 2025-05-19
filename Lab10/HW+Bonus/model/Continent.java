package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continents")
@NamedQuery(name = "Continent.findByName",
        query = "SELECT c FROM Continent c WHERE c.name LIKE :name")
@Getter
@Setter
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

    @OneToMany(mappedBy = "continent", cascade = CascadeType.ALL)
    private List<Country> countries = new ArrayList<>();

}
