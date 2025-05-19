package org.example.repository;

import org.example.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCityRepository implements Repository<City, Long> {
    private Connection connection;

    public JdbcCityRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(City entity) {
        String query = "INSERT INTO cities (name, country_id, capital, latitude, longitude, population) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setLong(2, entity.getCountry().getId());
            stmt.setBoolean(3, entity.isCapital());
            stmt.setDouble(4, entity.getLatitude());
            stmt.setDouble(5, entity.getLongitude());
            stmt.setLong(6, entity.getPopulation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public City findById(Long id) {
        String query = "SELECT * FROM cities WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new City(rs.getString("name"), null, rs.getBoolean("capital"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getLong("population"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<City> findByName(String pattern) {
        String query = "SELECT * FROM cities WHERE name LIKE ?";
        List<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cities.add(new City(rs.getString("name"), null, rs.getBoolean("capital"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getLong("population")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
