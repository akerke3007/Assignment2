package org.example;

public class Main {
    public static void main(String[] args) {
        // Создаем виды спорта
        Sport football = new Sport("Football", true);
        Sport tennis = new Sport("Tennis", false);

        // Создаем клуб
        Sportclub club = new Sportclub("Champions");

        // Добавляем атлетов
        club.addAthlete(new Athlete("Alex", 20, football, 3));
        club.addAthlete(new Athlete("Maria", 18, tennis, 2));
        club.addAthlete(new Athlete("John", 22, football, 4));

        // Выводим результат
        club.showAllAthletes();
        club.filterBySport("Football");
        club.findAthleteByName("Maria");
    }
}