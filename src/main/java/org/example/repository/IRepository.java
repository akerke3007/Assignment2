package org.example.repository;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll();

    // Requirement #8: default interface method
    default boolean isEmpty() {
        return getAll().isEmpty();
    }
}
