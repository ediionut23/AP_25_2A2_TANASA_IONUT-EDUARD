package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            ContinentDAO continentDAO = new ContinentDAO();
            CountryDAO countryDAO = new CountryDAO();

            continentDAO.create("Europe");

            Integer europeId = continentDAO.findByName("Europe");
            System.out.println("Europe ID: " + europeId);

            countryDAO.create("France", "FR", europeId);

            Integer franceId = countryDAO.findByName("France");
            System.out.println("France ID: " + franceId);

            String continentName = continentDAO.findById(europeId);
            System.out.println("Continent name for ID " + europeId + ": " + continentName);

               String countryName = countryDAO.findById(franceId);
            System.out.println("Country name for ID " + franceId + ": " + countryName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
