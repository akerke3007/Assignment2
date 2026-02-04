package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SportclubDao {
    private final Connection conn;

    public SportclubDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Sportclub club) throws SQLException {
        String sql = "INSERT INTO sports_club (name, city, founded_year) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, club.getClubName());
            ps.setString(2, club.getCity());
            ps.setInt(3, club.getFoundedYear());
            ps.executeUpdate();
        }
    }

    public List<Sportclub> getAll() throws SQLException {
        List<Sportclub> list = new ArrayList<>();
        String sql = "SELECT * FROM sports_club";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Sportclub(
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("founded_year")
                ));
            }
        }
        return list;
    }

    public void updateCity(int clubId, String newCity) throws SQLException {
        String sql = "UPDATE sports_club SET city = ? WHERE club_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newCity);
            ps.setInt(2, clubId);
            ps.executeUpdate();
        }
    }

    public void delete(int clubId) throws SQLException {
        String sql = "DELETE FROM sports_club WHERE club_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clubId);
            ps.executeUpdate();
        }
    }
}