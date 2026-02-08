package org.example.repository;

import org.example.domain.Sportclub;

public interface ClubRepository extends IRepository<Sportclub> {
    void insert(Sportclub club);
}
