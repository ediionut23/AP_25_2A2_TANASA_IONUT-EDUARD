package org.example;

import java.sql.*;

public class CountryDAO {
    public void create(String name, String code, int continentId) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "INSERT INTO countries (name, code, continent_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, code);
            stmt.setInt(3, continentId);
            stmt.executeUpdate();
        }
    }

    public String findById(int id) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "SELECT name FROM countries WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    public Integer findByName(String name) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "SELECT id FROM countries WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return null;
    }
}
