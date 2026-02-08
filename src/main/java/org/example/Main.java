package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.example.domain.Athlete;
import org.example.domain.SportFactory;
import org.example.dto.ApiError;
import org.example.dto.ApiOk;
import org.example.dto.AthleteCreateDto;
import org.example.dto.AthleteUpdateDto;
import org.example.exception.DatabaseException;
import org.example.exception.EntityNotFoundException;
import org.example.repository.*;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String url  = "jdbc:postgresql://localhost:5432/project?currentSchema=public";
        String user = "postgres";
        String pass = "1234";

        Connection conn = null;

        try {
            // ✅ Connection НЕ в try-with-resources, чтобы не закрылся сразу
            conn = DriverManager.getConnection(url, user, pass);

            AthleteRepository athleteRepo = new JdbcAthleteRepository(conn);
            SportRepository sportRepo = new JdbcSportRepository(conn);
            ClubRepository clubRepo = new JdbcClubRepository(conn);

            String staticDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/public")
                    .toAbsolutePath()
                    .toString();

            Javalin app = Javalin.create(config -> {
                config.staticFiles.add(staticFiles -> {
                    staticFiles.directory = staticDir;
                    staticFiles.location = Location.EXTERNAL;
                });

                config.bundledPlugins.enableCors(cors ->
                        cors.addRule(rule -> rule.anyHost())
                );
            }).start(8080);

            // ✅ Закрыть соединение при остановке приложения
            Connection finalConn = conn;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (finalConn != null && !finalConn.isClosed()) {
                        finalConn.close();
                        System.out.println("DB connection closed.");
                    }
                } catch (SQLException ignored) {}
            }));

            // ===== ROUTES =====
            app.get("/", ctx -> ctx.result("Sports Club API is running"));

            app.get("/sports", ctx -> ctx.json(sportRepo.getAll()));
            app.get("/clubs", ctx -> ctx.json(clubRepo.getAll()));

            app.get("/athletes", ctx -> {
                List<Athlete> list = athleteRepo.getAll();
                ctx.json(list);
            });

            app.post("/athletes", ctx -> {
                AthleteCreateDto dto = ctx.bodyAsClass(AthleteCreateDto.class);

                Athlete athlete = new Athlete.Builder()
                        .setName(dto.name)
                        .setAge(dto.age)
                        .setSport(SportFactory.create(dto.sportName, dto.team))
                        .setClubName(dto.clubName)
                        .setExperience(dto.experience)
                        .build();

                athleteRepo.insert(athlete);
                ctx.status(201).json(new ApiOk("CREATED", "Athlete created"));
            });

            app.put("/athletes/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                AthleteUpdateDto dto = ctx.bodyAsClass(AthleteUpdateDto.class);

                Athlete athlete = new Athlete.Builder()
                        .setAthleteId(id)
                        .setName(dto.name)
                        .setAge(dto.age)
                        .setSport(SportFactory.create(dto.sportName, dto.team))
                        .setClubName(dto.clubName)
                        .setExperience(dto.experience)
                        .build();

                athleteRepo.update(id, athlete);
                ctx.json(new ApiOk("UPDATED", "Athlete updated"));
            });

            app.put("/athletes/{id}/experience", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));

                String valueStr = ctx.queryParam("value");
                if (valueStr == null) throw new IllegalArgumentException("value is required");

                int value = Integer.parseInt(valueStr);
                if (value < 0) throw new IllegalArgumentException("experience must be >= 0");

                athleteRepo.updateExperience(id, value);
                ctx.json(new ApiOk("UPDATED", "Experience updated"));
            });

            app.delete("/athletes/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));
                athleteRepo.delete(id);
                ctx.status(204);
            });

            // ===== ERROR HANDLERS =====
            app.exception(NumberFormatException.class, (e, ctx) ->
                    ctx.status(400).json(new ApiError("VALIDATION_ERROR", "id must be an integer"))
            );

            app.exception(IllegalArgumentException.class, (e, ctx) ->
                    ctx.status(400).json(new ApiError("VALIDATION_ERROR", e.getMessage()))
            );

            app.exception(EntityNotFoundException.class, (e, ctx) ->
                    ctx.status(404).json(new ApiError("NOT_FOUND", e.getMessage()))
            );

            app.exception(DatabaseException.class, (e, ctx) -> {
                String msg = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
                ctx.status(500).json(new ApiError("DB_ERROR", msg));
            });

            app.exception(Exception.class, (e, ctx) ->
                    ctx.status(500).json(new ApiError("SERVER_ERROR", "Unexpected error"))
            );

            System.out.println("=================================");
            System.out.println("SERVER STARTED");
            System.out.println("Frontend: http://localhost:8080/index.html");
            System.out.println("API:      http://localhost:8080/athletes");
            System.out.println("DB:       project/public");
            System.out.println("=================================");

        } catch (Exception e) {
            System.out.println("Startup error: " + e.getMessage());
            // если соединение успели открыть — закроем
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException ignored) {}
        }
    }
}
