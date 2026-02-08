package org.example.domain;

import java.util.Objects;

public abstract class Person {
    private final String name;   // имя не должно меняться после создания
    private int age;

    protected Person(String name, int age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null/blank");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        this.name = name.trim();
        this.age = age;
    }

    public String getName() { return name; }

    public int getAge() { return age; }
    public void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be positive");
        }
        this.age = age;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name='" + name + "', age=" + age + '}';
    }

    /**
     * Identity rule for Person (simple version): same runtime class + same name.
     * Для учебного проекта этого достаточно, чтобы коллекции работали стабильно.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
