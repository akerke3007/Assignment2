package org.example.domain;

public class SportFactory {
    private SportFactory() {}

    public static Sport create(String name, boolean team) {
        return new Sport(name, team);
    }
}

