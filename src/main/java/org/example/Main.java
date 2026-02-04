package org.example;

import io.javalin.Javalin;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String pass = "1234";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

// 1. Кестелерді құру және деректерді SQL арқылы автоматты түрде салу
            createTablesWithData(conn);

// 2. Репозиторийді интерфейс арқылы баптау (SOLID: DIP)
            IRepository<Athlete> athleteRepo = new AthleteDao(conn);

// 3. Серверді іске қосу
            Javalin app = Javalin.create().start(8080);

// Роуттар (Endpoints)
            app.get("/", ctx -> ctx.result("Welcome to Sports Club API!"));
            app.get("/athletes", ctx -> ctx.json(athleteRepo.getAll()));

            System.out.println("\n✅ SERVER STARTED AT http://localhost:8080/athletes");
            System.out.println("🚀 Data loaded successfully from SQL scripts inside Main.");

        } catch (Exception e) {
            System.err.println("❌ Critical Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createTablesWithData(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
// Ескі кестелерді өшіру
            st.executeUpdate("DROP TABLE IF EXISTS athlete CASCADE");
            st.executeUpdate("DROP TABLE IF EXISTS sport CASCADE");
            st.executeUpdate("DROP TABLE IF EXISTS sports_club CASCADE");

// Кестелерді құру
            st.executeUpdate("CREATE TABLE sport (sport_id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, category VARCHAR(100))");
            st.executeUpdate("CREATE TABLE sports_club (club_id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, city VARCHAR(100), founded_year INT)");
            st.executeUpdate("CREATE TABLE athlete (athlete_id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, age INT, " +
                    "sport_id INT REFERENCES sport(sport_id), " +
                    "club_id INT REFERENCES sports_club(club_id), " +
                    "experience INT)");

// --- ДЕРЕКТЕРДІ ЕНГІЗУ (INSERT DATA) ---

// Спорт түрлері
            st.executeUpdate("INSERT INTO sport (name, category) VALUES ('Football', 'Team')");
            st.executeUpdate("INSERT INTO sport (name, category) VALUES ('Tennis', 'Individual')");

// Клубтар
            st.executeUpdate("INSERT INTO sports_club (name, city, founded_year) VALUES ('Real Madrid', 'Madrid', 1902)");
            st.executeUpdate("INSERT INTO sports_club (name, city, founded_year) VALUES ('Astana Qazaqstan', 'Astana', 2007)");

// Атлеттер (sport_id және club_id сілтемелерімен)
            st.executeUpdate("INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES ('Mbappe', 25, 1, 1, 7)");
            st.executeUpdate("INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES ('Vinicius', 24, 1, 1, 6)");
            st.executeUpdate("INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES ('Djokovic', 36, 2, 2, 20)");

            System.out.println("✅ Database schema and initial data applied successfully.");
        }
    }
}