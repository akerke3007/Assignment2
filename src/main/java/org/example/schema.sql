-- 1. Кестелерді құру
CREATE TABLE IF NOT EXISTS sport (
                                     sport_id SERIAL PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
    category VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS sports_club (
                                           club_id SERIAL PRIMARY KEY,
                                           name VARCHAR(100) NOT NULL,
    city VARCHAR(100),
    founded_year INT
    );

CREATE TABLE IF NOT EXISTS athlete (
                                       athlete_id SERIAL PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
    age INT,
    sport_id INT REFERENCES sport(sport_id),
    club_id INT REFERENCES sports_club(club_id),
    experience INT
    );

-- 2. Данныйларды (Тесттік деректерді) енгізу
-- Бұл деректер кесте құрылған соң бірден қосылады
INSERT INTO sport (name, category) VALUES ('Football', 'Team');
INSERT INTO sport (name, category) VALUES ('Tennis', 'Individual');

INSERT INTO sports_club (name, city, founded_year) VALUES ('Real Madrid', 'Madrid', 1902);
INSERT INTO sports_club (name, city, founded_year) VALUES ('Astana Qazaqstan', 'Astana', 2007);

INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES ('Mbappe', 25, 1, 1, 7);
INSERT INTO athlete (name, age, sport_id, club_id, experience) VALUES ('Vinicius', 24, 1, 1, 6);