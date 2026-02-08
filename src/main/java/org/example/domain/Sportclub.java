package org.example.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sportclub {

    private final String clubName;
    private final String city;
    private final int foundedYear;
    private final List<Athlete> athletes = new ArrayList<>();

    public Sportclub(String clubName, String city, int foundedYear) {
        if (clubName == null || clubName.isBlank()) {
            throw new IllegalArgumentException("Club name cannot be null/blank");
        }
        if (foundedYear <= 0) {
            throw new IllegalArgumentException("Founded year must be positive");
        }

        this.clubName = clubName.trim();
        this.city = city;
        this.foundedYear = foundedYear;
    }

    public String getClubName() {
        return clubName;
    }

    public String getCity() {
        return city;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    /**
     * Returns read-only list to preserve encapsulation.
     */
    public List<Athlete> getAthletes() {
        return Collections.unmodifiableList(athletes);
    }

    public void addAthlete(Athlete athlete) {
        if (athlete == null) {
            throw new IllegalArgumentException("Athlete cannot be null");
        }
        athletes.add(athlete);
    }

    /* ================= OBJECT METHODS ================= */

    /**
     * Identity rule: clubName is unique (academic project).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sportclub)) return false;
        Sportclub that = (Sportclub) o;
        return Objects.equals(clubName, that.clubName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubName);
    }

    @Override
    public String toString() {
        return "Sportclub{" +
                "clubName='" + clubName + '\'' +
                ", city='" + city + '\'' +
                ", foundedYear=" + foundedYear +
                ", athletesCount=" + athletes.size() +
                '}';
    }
}
