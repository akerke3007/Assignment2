package org.example.domain;

import java.util.Objects;

public class Athlete extends Person {
    private int athleteId;          // ✅ ID
    private Sport sport;
    private String clubName;        // ✅ чтобы отображать клуб в JSON
    private int experience;

    private Athlete(int athleteId, String name, int age, Sport sport, String clubName, int experience) {
        super(name, age);
        this.athleteId = athleteId;
        this.sport = sport;
        this.clubName = clubName;
        this.experience = experience;
    }

    public int getAthleteId() { return athleteId; }
    public Sport getSport() { return sport; }
    public String getClubName() { return clubName; }
    public int getExperience() { return experience; }

    // ✅ Builder pattern (остается)
    public static class Builder {
        private int athleteId;
        private String name;
        private int age;
        private Sport sport;
        private String clubName;
        private int experience;

        public Builder setAthleteId(int athleteId) { this.athleteId = athleteId; return this; }
        public Builder setName(String name) { this.name = name; return this; }
        public Builder setAge(int age) { this.age = age; return this; }
        public Builder setSport(Sport sport) { this.sport = sport; return this; }
        public Builder setClubName(String clubName) { this.clubName = clubName; return this; }
        public Builder setExperience(int experience) { this.experience = experience; return this; }

        public Athlete build() {
            return new Athlete(athleteId, name, age, sport, clubName, experience);
        }
    }

    @Override
    public String toString() {
        return "Athlete{id=" + athleteId +
                ", name=" + getName() +
                ", age=" + getAge() +
                ", sport=" + (sport != null ? sport.getSportName() : "None") +
                ", club=" + (clubName != null ? clubName : "None") +
                ", exp=" + experience + "}";
    }

    // ✅ identity/collections behavior
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Athlete)) return false;
        Athlete athlete = (Athlete) o;
        return athleteId == athlete.athleteId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(athleteId);
    }
}
