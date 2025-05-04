package org.example;

import java.sql.*;

public class ContinentDAO {
    public void create(String name) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "INSERT INTO continents (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public String findById(int id) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "SELECT name FROM continents WHERE id = ?";
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
        String sql = "SELECT id FROM continents WHERE name = ?";
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
