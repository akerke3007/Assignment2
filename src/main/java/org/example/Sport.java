package org.example;

public class Sport {
    public String sportName;
    public boolean isTeam;

    public Sport(String sportName, boolean isTeam) {
        this.sportName = sportName;
        this.isTeam = isTeam;
    }

    @Override
    public String toString() {
        return sportName;
    }
}