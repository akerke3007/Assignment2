package org.example.repository;

import org.example.exception.DatabaseException;
import org.example.exception.EntityNotFoundException;
import org.example.domain.Sport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSportRepository implements SportRepository {

    private final Connection conn;

    public JdbcSportRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Sport sport) {
        String sql = "INSERT INTO sport (name, category) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sport.getSportName());
            ps.setString(2, sport.isTeam() ? "Team" : "Individual");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting sport", e);
        }
    }

    @Override
    public List<Sport> getAll() {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT sport_id, name, category FROM sport ORDER BY sport_id";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("sport_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                boolean isTeam = category.equalsIgnoreCase("Team");

                sports.add(new Sport(id, name, isTeam));
            }

            return sports;

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching sports", e);
        }
    }

    @Override
    public void update(int sportId, Sport sport) {
        String sql = "UPDATE sport SET name = ?, category = ? WHERE sport_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sport.getSportName());
            ps.setString(2, sport.isTeam() ? "Team" : "Individual");
            ps.setInt(3, sportId);

            int rows = ps.executeUpdate();
            if (rows == 0) throw new EntityNotFoundException("Sport with id " + sportId + " not found");

        } catch (SQLException e) {
            throw new DatabaseException("Error while updating sport", e);
        }
    }

    @Override
    public void delete(int sportId) {
        String sql = "DELETE FROM sport WHERE sport_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sportId);

            int rows = ps.executeUpdate();
            if (rows == 0) throw new EntityNotFoundException("Sport with id " + sportId + " not found");

        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting sport", e);
        }
    }
}
