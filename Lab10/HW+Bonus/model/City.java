package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cities")
@NamedQuery(name = "City.findByName",
        query = "SELECT c FROM City c WHERE c.name LIKE :name")
@Getter
@Setter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private boolean capital;
    private double latitude;
    private double longitude;

    private long population;

    public City() {}

    public City(String name, Country country, boolean capital, double latitude, double longitude, long population) {
        this.name = name;
        this.country = country;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
    }


}
