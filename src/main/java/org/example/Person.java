package org.example;
import java.util.Objects;

public class Person {
    public String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Переопределяем toString, чтобы красиво выводить данные
    @Override
    public String toString() {
        return "Name: " + name + ", Age: " + age;
    }

    // Методы для сравнения объектов (требование задания)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}