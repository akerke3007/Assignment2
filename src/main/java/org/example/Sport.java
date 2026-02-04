package org.example;

public class Sport {
    private String sportName; // Encapsulation: өрістер жабық
    private boolean isTeam;

    public Sport(String sportName, boolean isTeam) {
        this.sportName = sportName;
        this.isTeam = isTeam;
    }

    // Бұл геттерлер DAO кластары үшін міндетті
    public String getSportName() { return sportName; }
    public boolean isTeam() { return isTeam; }

    @Override
    public String toString() {
        return sportName;
    }
}