package org.example.repository;

import org.example.exception.DatabaseException;
import org.example.exception.EntityNotFoundException;
import org.example.domain.Athlete;
import org.example.domain.Sport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAthleteRepository implements AthleteRepository {

    private final Connection conn;

    public JdbcAthleteRepository(Connection conn) {
        this.conn = conn;
    }

    // ================= CREATE =================
    @Override
    public void insert(Athlete athlete) {
        String clubName = athlete.getClubName();

        String sportName = athlete.getSport().getSportName();
        String category = athlete.getSport().isTeam() ? "Team" : "Individual";

        int sportId = getOrCreateSport(sportName, category);

        Integer clubId = null;
        if (clubName != null && !clubName.isBlank()) {
            clubId = getOrCreateClub(clubName);
        }

        String sql = """
            INSERT INTO athlete (name, age, sport_id, club_id, experience)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, athlete.getName());
            ps.setInt(2, athlete.getAge());
            ps.setInt(3, sportId);

            if (clubId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, clubId);

            ps.setInt(5, athlete.getExperience());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting athlete", e);
        }
    }

    // ================= READ =================
    @Override
    public List<Athlete> getAll() {
        List<Athlete> list = new ArrayList<>();

        String sql = """
            SELECT
                a.athlete_id,
                a.name,
                a.age,
                a.experience,
                s.name AS sport_name,
                s.category AS sport_category,
                c.name AS club_name
            FROM athlete a
            LEFT JOIN sport s ON a.sport_id = s.sport_id
            LEFT JOIN sports_club c ON a.club_id = c.club_id
            ORDER BY a.athlete_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String sportName = rs.getString("sport_name");
                String sportCategory = rs.getString("sport_category");
                boolean isTeam = sportCategory != null && sportCategory.equalsIgnoreCase("Team");

                // ✅ FIX #5: если sport_name == null => sport = null
                Sport sport = (sportName == null) ? null : new Sport(sportName, isTeam);

                Athlete athlete = new Athlete.Builder()
                        .setAthleteId(rs.getInt("athlete_id"))
                        .setName(rs.getString("name"))
                        .setAge(rs.getInt("age"))
                        .setSport(sport)
                        .setClubName(rs.getString("club_name"))
                        .setExperience(rs.getInt("experience"))
                        .build();

                list.add(athlete);
            }

            return list;

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching athletes", e);
        }
    }

    // ================= UPDATE (FULL) =================
    @Override
    public void update(int athleteId, Athlete athlete) {
        if (athlete == null) throw new IllegalArgumentException("athlete is required");
        if (athlete.getExperience() < 0) throw new IllegalArgumentException("experience must be >= 0");
        if (athlete.getSport() == null) throw new IllegalArgumentException("sport is required");

        String sportName = athlete.getSport().getSportName();
        String category = athlete.getSport().isTeam() ? "Team" : "Individual";
        int sportId = getOrCreateSport(sportName, category);

        Integer clubId = null;
        String clubName = athlete.getClubName();
        if (clubName != null && !clubName.isBlank()) {
            clubId = getOrCreateClub(clubName);
        }

        String sql = """
            UPDATE athlete
            SET name = ?, age = ?, sport_id = ?, club_id = ?, experience = ?
            WHERE athlete_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, athlete.getName());
            ps.setInt(2, athlete.getAge());
            ps.setInt(3, sportId);

            if (clubId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, clubId);

            ps.setInt(5, athlete.getExperience());
            ps.setInt(6, athleteId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Athlete with id " + athleteId + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating athlete", e);
        }
    }

    // ================= UPDATE (ONLY EXPERIENCE) =================
    @Override
    public void updateExperience(int athleteId, int newExperience) {
        if (newExperience < 0) {
            throw new IllegalArgumentException("experience must be >= 0");
        }

        String sql = "UPDATE athlete SET experience = ? WHERE athlete_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newExperience);
            ps.setInt(2, athleteId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new EntityNotFoundException("Athlete with id " + athleteId + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating athlete experience", e);
        }
    }

    // ================= DELETE =================
    @Override
    public void delete(int athleteId) {
        String sql = "DELETE FROM athlete WHERE athlete_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, athleteId);

            int deleted = ps.executeUpdate();
            if (deleted == 0) {
                throw new EntityNotFoundException("Athlete with id " + athleteId + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting athlete", e);
        }
    }

    // ================= HELPERS =================
    private int getOrCreateSport(String name, String category) {
        String findSql = "SELECT sport_id FROM sport WHERE LOWER(name) = LOWER(?)";

        try (PreparedStatement ps = conn.prepareStatement(findSql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("sport_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while searching sport", e);
        }

        String insertSql = "INSERT INTO sport (name, category) VALUES (?, ?) RETURNING sport_id";

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("sport_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while creating sport", e);
        }
    }

    private int getOrCreateClub(String name) {
        String findSql = "SELECT club_id FROM sports_club WHERE LOWER(name) = LOWER(?)";

        try (PreparedStatement ps = conn.prepareStatement(findSql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("club_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while searching club", e);
        }

        String defaultCity = "Unknown";
        int defaultFoundedYear = 2000;

        String insertSql = """
            INSERT INTO sports_club (name, city, founded_year)
            VALUES (?, ?, ?)
            RETURNING club_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, name);
            ps.setString(2, defaultCity);
            ps.setInt(3, defaultFoundedYear);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("club_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while creating club", e);
        }
    }
}
