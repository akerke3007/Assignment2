package org.example.repository;

import org.example.domain.Sport;

public interface SportRepository extends IRepository<Sport> {
    void insert(Sport sport);
    void update(int sportId, Sport sport);
    void delete(int sportId);
}
