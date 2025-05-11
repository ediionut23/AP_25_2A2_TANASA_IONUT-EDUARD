package org.example;

import java.io.BufferedReader;
import java.io.FileReader;

public class CityImporter {
    public void importFromCSV(String filePath, ContinentDAO continentDAO, CountryDAO countryDAO, CityDAO cityDAO) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                String countryName = parts[0].trim();
                String capitalName = parts[1].trim();
                String capitalLatStr = parts[2].trim();
                String capitalLongStr = parts[3].trim();
                String countryCode = parts[4].trim();
                String continentName = parts[5].trim();

                if (capitalLatStr.isEmpty() || capitalLongStr.isEmpty() || countryCode.isEmpty()) {
                    continue;
                }

                double latitude = Double.parseDouble(capitalLatStr);
                double longitude = Double.parseDouble(capitalLongStr);

                Integer continentId = continentDAO.findByName(continentName);
                if (continentId == null) {
                    continentDAO.create(continentName);
                    continentId = continentDAO.findByName(continentName);
                }

                Integer countryId = countryDAO.findByName(countryName);
                if (countryId == null && !countryCode.isEmpty()) {
                    countryDAO.create(countryName, countryCode, continentId);
                    countryId = countryDAO.findByName(countryName);
                }

                if (countryId != null && capitalName.length() > 0) {
                    cityDAO.create(capitalName, countryId, true, latitude, longitude);
                }
            }

            System.out.println("Import completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
