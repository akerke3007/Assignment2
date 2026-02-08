package org.example.repository;

import org.example.exception.DatabaseException;
import org.example.domain.Sportclub;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcClubRepository implements ClubRepository {

    private final Connection conn;

    public JdbcClubRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Sportclub club) {
        String sql = "INSERT INTO sports_club (name, city, founded_year) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, club.getClubName());
            ps.setString(2, club.getCity());
            ps.setInt(3, club.getFoundedYear());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting club", e);
        }
    }

    @Override
    public List<Sportclub> getAll() {
        List<Sportclub> list = new ArrayList<>();
        String sql = "SELECT name, city, founded_year FROM sports_club ORDER BY name";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Sportclub(
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getInt("founded_year")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching clubs", e);
        }
    }
}
