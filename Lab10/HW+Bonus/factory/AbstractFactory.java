package org.example.factory;

import org.example.repository.Repository;
import org.example.repository.JdbcCityRepository;
import org.example.repository.JpaCityRepository;
import org.example.model.City;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.InputStream;
import java.util.Properties;

public class AbstractFactory {

    private static final String JDBC_MODE = "jdbc";
    private static final String JPA_MODE = "jpa";

    public static Repository<City, Long> getCityRepository() {
        String mode = getConfigMode();
        if (JDBC_MODE.equals(mode)) {
            return new JdbcCityRepository(getJdbcConnection());
        } else if (JPA_MODE.equals(mode)) {
            return new JpaCityRepository();
        }
        throw new IllegalArgumentException("Invalid data access mode");
    }

    private static String getConfigMode() {
        try (InputStream input = AbstractFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }
            prop.load(input);
            return prop.getProperty("data.access.mode");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Connection getJdbcConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
