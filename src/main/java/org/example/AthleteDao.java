package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AthleteDao implements IRepository<Athlete> {

    private final Connection conn;

    public AthleteDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Athlete athlete, int sportId, int clubId) throws SQLException {
        String sql = "INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Encapsulation: Getter-лерді қолданамыз
            ps.setString(1, athlete.getName());
            ps.setInt(2, athlete.getAge());
            ps.setInt(3, sportId);
            ps.setInt(4, clubId);
            ps.setInt(5, athlete.getExperience());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Athlete> getAll() throws Exception {
        List<Athlete> list = new ArrayList<>();
        String sql = "SELECT a.name, a.age, a.experience, s.name AS sport_name " +
                "FROM athlete a " +
                "LEFT JOIN sport s ON a.sport_id = s.sport_id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Athlete athlete = new Athlete.Builder()
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .setSport(new Sport(rs.getString("sport_name"), false))
                        .setExperience(rs.getInt("experience"))
                        .build();
                list.add(athlete);
            }
        } catch (SQLException e) {

            throw new DatabaseException("Error while fetching athletes from database", e);
        }
        return list;
    }


    public void updateExperience(String name, int newExperience) throws SQLException {
        String sql = "UPDATE athlete SET experience = ? WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newExperience);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }


    public void delete(String name) throws EntityNotFoundException, SQLException {
        String sql = "DELETE FROM athlete WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                // Талап 4: Атлет табылмаса арнайы қате шығару
                throw new EntityNotFoundException("Athlete with name " + name + " not found to delete.");
            }
        }
    }
}