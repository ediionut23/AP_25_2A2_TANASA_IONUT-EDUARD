package org.example.solver;

import org.example.model.City;
import org.example.repository.Repository;
import org.example.factory.AbstractFactory;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.example.util.LoggerUtil;
import java.util.stream.Collectors;


public class CityConstrainsSolver {
    private static final Logger logger = LoggerUtil.getLogger();
    private final Repository<City, Long> cityRepository;

    public CityConstrainsSolver() {
        this.cityRepository = AbstractFactory.getCityRepository();
    }


    public List<City> findCitiesWithConstraints(char startingLetter, long minPopulation, long maxPopulation) {
        logger.log(Level.INFO, "Starting constraint solving for cities starting with letter: " + startingLetter);

        String pattern = startingLetter + "%";
        List<City> candidateCities = cityRepository.findByName(pattern);

        if (candidateCities == null || candidateCities.isEmpty()) {
            logger.log(Level.INFO, "No cities found starting with letter: " + startingLetter);
            return Collections.emptyList();
        }

        logger.log(Level.INFO, "Found " + candidateCities.size() + " cities starting with " + startingLetter);

        Map<Long, List<City>> citiesByCountry = candidateCities.stream()
                .collect(Collectors.groupingBy(city -> city.getCountry().getId()));

        Model model = new Model("City Selection Problem");

        Map<Long, BoolVar> cityVars = new HashMap<>();
        for (City city : candidateCities) {
            cityVars.put(city.getId(), model.boolVar("city_" + city.getId()));
        }

        for (Long countryId : citiesByCountry.keySet()) {
            List<City> citiesInCountry = citiesByCountry.get(countryId);
            if (citiesInCountry.size() > 1) {
                BoolVar[] cityVarsInCountry = citiesInCountry.stream()
                        .map(city -> cityVars.get(city.getId()))
                        .toArray(BoolVar[]::new);

                model.sum(cityVarsInCountry, "<=", 1).post();
            }
        }

        IntVar totalPopulation = model.intVar("totalPopulation", (int)minPopulation, (int)maxPopulation);
        IntVar[] populations = candidateCities.stream()
                .map(city -> model.intVar("pop_" + city.getId(), (int)city.getPopulation())
                        .mul(cityVars.get(city.getId())).intVar())
                .toArray(IntVar[]::new);

        model.sum(populations, "=", totalPopulation).post();

        IntVar nbSelectedCities = model.intVar("nbCities", 0, candidateCities.size());
        model.sum(cityVars.values().toArray(new BoolVar[0]), "=", nbSelectedCities).post();
        model.setObjective(Model.MAXIMIZE, nbSelectedCities);

        Solver solver = model.getSolver();
        List<City> selectedCities = new ArrayList<>();

        if (solver.solve()) {
            logger.log(Level.INFO, "Solution found!");

            for (City city : candidateCities) {
                if (cityVars.get(city.getId()).getValue() == 1) {
                    selectedCities.add(city);
                }
            }

            long actualTotalPopulation = selectedCities.stream().mapToLong(City::getPopulation).sum();
            logger.log(Level.INFO, "Selected " + selectedCities.size() + " cities with total population: " + actualTotalPopulation);
            for (City city : selectedCities) {
                logger.log(Level.INFO, "Selected city: " + city.getName() + ", Country: " +
                        city.getCountry().getName() + ", Population: " + city.getPopulation());
            }
        } else {
            logger.log(Level.INFO, "No solution found for the given constraints.");
        }

        return selectedCities;
    }
}