package org.example;

import java.sql.*;

public class CityDAO {

    public void create(String name, int countryId, boolean capital, double latitude, double longitude) throws SQLException {
        String sql = "INSERT INTO cities (name, country_id, capital, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, countryId);
            stmt.setBoolean(3, capital);
            stmt.setDouble(4, latitude);
            stmt.setDouble(5, longitude);
            stmt.executeUpdate();
        }
    }

    public City findByName(String name) throws SQLException {
        String sql = "SELECT * FROM cities WHERE name = ?";
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                City city = new City();
                city.setId(rs.getInt("id"));
                city.setCountryId(rs.getInt("country_id"));
                city.setName(rs.getString("name"));
                city.setCapital(rs.getBoolean("capital"));
                city.setLatitude(rs.getDouble("latitude"));
                city.setLongitude(rs.getDouble("longitude"));
                return city;
            }
        }
        return null;
    }
}
