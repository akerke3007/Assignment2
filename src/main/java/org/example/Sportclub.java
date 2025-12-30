package org.example;
import java.util.ArrayList;

public class Sportclub {
    public String clubName;
    // Наш "Data Pool" — список атлетов
    public ArrayList<Athlete> athletes = new ArrayList<>();

    public Sportclub(String clubName) {
        this.clubName = clubName;
    }

    public void addAthlete(Athlete a) {
        athletes.add(a);
    }

    public void showAllAthletes() {
        System.out.println("--- List of athletes " + clubName + " ---");
        for (Athlete a : athletes) {
            System.out.println(a);
        }
    }

    public void filterBySport(String searchSport) {
        System.out.println("\nSearchind by sport: " + searchSport);
        for (Athlete a : athletes) {
            if (a.sport.sportName.equalsIgnoreCase(searchSport)) {
                System.out.println("Found: " + a.name);
            }
        }
    }

    public void findAthleteByName(String searchName) {
        System.out.println("\nSearching by name: " + searchName);
        for (Athlete a : athletes) {
            if (a.name.equalsIgnoreCase(searchName)) {
                System.out.println("Information: " + a);
            }
        }
    }
}