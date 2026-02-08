package org.example.domain;

import java.util.*;
import java.util.stream.Collectors;

public class AthletePool {
    private final List<Athlete> data = new ArrayList<>();

    public void load(List<Athlete> athletes) {
        data.clear();
        data.addAll(athletes);
    }

    public List<Athlete> sortByAgeAsc() {
        return data.stream()
                .sorted(Comparator.comparingInt(Athlete::getAge))
                .collect(Collectors.toList());
    }

    public List<Athlete> filterByMinExperience(int minYears) {
        return data.stream()
                .filter(a -> a.getExperience() >= minYears)
                .collect(Collectors.toList());
    }

    public Optional<Athlete> findByName(String name) {
        return data.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
