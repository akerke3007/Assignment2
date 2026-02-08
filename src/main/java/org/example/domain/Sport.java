package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Sport {

    @JsonIgnore              // 👈 ВАЖНО: скрываем из JSON
    private int sportId;

    private String sportName;
    private boolean isTeam;

    // для чтения из БД (когда есть id)
    public Sport(int sportId, String sportName, boolean isTeam) {
        this.sportId = sportId;
        this.sportName = sportName;
        this.isTeam = isTeam;
    }

    // для JSON / создания
    public Sport(String sportName, boolean isTeam) {
        this.sportName = sportName;
        this.isTeam = isTeam;
    }

    public int getSportId() {
        return sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public boolean isTeam() {
        return isTeam;
    }

    @Override
    public String toString() {
        return sportName + " (" + (isTeam ? "Team" : "Individual") + ")";
    }
}
