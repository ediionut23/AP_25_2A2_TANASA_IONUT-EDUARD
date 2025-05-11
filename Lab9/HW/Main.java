package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            ContinentDAO continentDAO = new ContinentDAO();
            CountryDAO countryDAO = new CountryDAO();
            CityDAO cityDAO = new CityDAO();

            continentDAO.create("Europe");
            Integer europeId = continentDAO.findByName("Europe");
            countryDAO.create("France", "FR", europeId);
            Integer franceId = countryDAO.findByName("France");
            cityDAO.create("Paris", franceId, true, 48.8566, 2.3522);

            System.out.println("France ID: " + franceId);
            System.out.println("Continent for France: " + continentDAO.findById(europeId));

            CityImporter importer = new CityImporter();
            importer.importFromCSV("src/main/resources/concap.csv", continentDAO, countryDAO, cityDAO);

            City city1 = cityDAO.findByName("Paris");
            City city2 = cityDAO.findByName("Jerusalem");
            City city3 = cityDAO.findByName("Kabul");
            City city4 = cityDAO.findByName("Jakarta");
            City city5 = cityDAO.findByName("Tokyo");
            City city6 = cityDAO.findByName("Nairobi");

            if (city1 != null && city2 != null) {
                double dist = GeoUtils.haversine(city1.getLatitude(), city1.getLongitude(),
                        city2.getLatitude(), city2.getLongitude());
                System.out.printf("Distanța dintre %s și %s este %.2f km%n", city1.getName(), city2.getName(), dist);
            }

            if (city3 != null && city4 != null) {
                double dist = GeoUtils.haversine(city3.getLatitude(), city3.getLongitude(),
                        city4.getLatitude(), city4.getLongitude());
                System.out.printf("Distanța dintre %s și %s este %.2f km%n", city3.getName(), city4.getName(), dist);
            }

            if (city5 != null && city6 != null) {
                double dist = GeoUtils.haversine(city5.getLatitude(), city5.getLongitude(),
                        city6.getLatitude(), city6.getLongitude());
                System.out.printf("Distanța dintre %s și %s este %.2f km%n", city5.getName(), city6.getName(), dist);
            }

            if (city1 != null && city3 != null) {
                double dist = GeoUtils.haversine(city1.getLatitude(), city1.getLongitude(),
                        city3.getLatitude(), city3.getLongitude());
                System.out.printf("Distanța dintre %s și %s este %.2f km%n", city1.getName(), city3.getName(), dist);
            }

            if (city4 != null && city6 != null) {
                double dist = GeoUtils.haversine(city4.getLatitude(), city4.getLongitude(),
                        city6.getLatitude(), city6.getLongitude());
                System.out.printf("Distanța dintre %s și %s este %.2f km%n", city4.getName(), city6.getName(), dist);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
