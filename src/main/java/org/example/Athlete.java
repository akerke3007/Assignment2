package org.example;

public class Athlete extends Person {
    private Sport sport;
    private int experience;

    private Athlete(String name, int age, Sport sport, int experience) {
        super(name, age);
        this.sport = sport;
        this.experience = experience;
    }

    public Sport getSport() { return sport; }
    public int getExperience() { return experience; }

    public static class Builder {
        private String name;
        private int age;
        private Sport sport;
        private int experience;

        public Builder setName(String name) { this.name = name; return this; }
        public Builder setAge(int age) { this.age = age; return this; }
        public Builder setSport(Sport sport) { this.sport = sport; return this; }
        public Builder setExperience(int experience) { this.experience = experience; return this; }

        public Athlete build() {
            return new Athlete(name, age, sport, experience);
        }
    }

    @Override
    public String toString() {
        return "Athlete [Name: " + getName() + ", Age: " + getAge() +
                ", Sport: " + (sport != null ? sport.getSportName() : "None") +
                ", Experience: " + experience + " years]";
    }
}