package org.example.repository;

import org.example.domain.Athlete;

import java.util.List;

public interface AthleteRepository {
    List<Athlete> getAll();
    void insert(Athlete athlete);

    // ✅ FIX #3: полноценный update
    void update(int athleteId, Athlete athlete);

    void updateExperience(int athleteId, int newExperience);
    void delete(int athleteId);
}
