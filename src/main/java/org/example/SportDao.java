package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportDao {

    private final Connection conn;

    public SportDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Sport sport) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sport (name, category) VALUES (?, ?)"
        );
        // ✅ ТҮЗЕТІЛДІ: sportName орнына getSportName()
        ps.setString(1, sport.getSportName());
        // ✅ ТҮЗЕТІЛДІ: isTeam орнына isTeam() геттері
        ps.setString(2, sport.isTeam() ? "Team" : "Individual");
        ps.executeUpdate();
    }

    public List<Sport> getAll() throws SQLException {
        List<Sport> list = new ArrayList<>();
        // Try-with-resources қолданған дұрыс (жақсы практика)
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sport")) {
            while (rs.next()) {
                list.add(new Sport(
                        rs.getString("name"),
                        "Team".equals(rs.getString("category"))
                ));
            }
        }
        return list;
    }

    public void updateCategory(int sportId, String newCategory) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE sport SET category = ? WHERE sport_id = ?")) {
            ps.setString(1, newCategory);
            ps.setInt(2, sportId);
            ps.executeUpdate();
        }
    }

    public void delete(int sportId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM sport WHERE sport_id = ?")) {
            ps.setInt(1, sportId);
            ps.executeUpdate();
        }
    }
}