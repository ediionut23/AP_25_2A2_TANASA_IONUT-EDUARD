package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class City {
    private int id;
    private int countryId;
    private String name;
    private boolean capital;
    private double latitude;
    private double longitude;

}
