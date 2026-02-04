package org.example;

import java.util.ArrayList;
import java.util.List;

public class Sportclub {
    private String clubName; // private қылдық
    private String city;
    private int foundedYear;
    private List<Athlete> athletes = new ArrayList<>();

    public Sportclub(String clubName, String city, int foundedYear) {
        this.clubName = clubName;
        this.city = city;
        this.foundedYear = foundedYear;
    }

    // Getters - олар талап бойынша міндетті
    public String getClubName() { return clubName; }
    public String getCity() { return city; }
    public int getFoundedYear() { return foundedYear; }

    public void addAthlete(Athlete a) { athletes.add(a); }

    @Override
    public String toString() {
        return "Club: " + clubName + ", City: " + city + ", Founded: " + foundedYear;
    }
}