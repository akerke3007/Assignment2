package org.example;

public class Athlete extends Person {
    public Sport sport;
    public int experience;

    public Athlete(String name, int age, Sport sport, int experience) {
        super(name, age); // Вызываем конструктор родителя (Person)
        this.sport = sport;
        this.experience = experience;
    }

    @Override
    public String toString() {
        // Используем super.toString(), чтобы взять имя и возраст из родителя
        return super.toString() + ", Sport type: " + sport + ", Experience: " + experience + " лет";
    }
}