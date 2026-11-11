package gra.world;

import gra.entity.Entity;
import gra.game.Main;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Zapisywanie i wczytywanie stanu gry
 */
public class Saves implements Serializable {
    /**
     * Lista jednostek przechowywanych w zapisie
     */
    private List<Entity> entities = new ArrayList<>();
    /**
     * Ilosc punktow
     */
    private int points;
    /**
     * Ilosc zyc
     */
    private int lives;
    /**
     * Poziom gry
     */
    private int level;
    /**
     * Data zapisu stanu gry
     */
    private LocalDateTime date;
    /**
     * Nazwa zapisu
     */
    private String name;
    /**
     * Czy zapis jest wczytany z pliku
     */
    private boolean loaded = false;

    /**
     * Konstruktor
     */
    public Saves() {
    }

    /**
     * Konstruktor przyjmujacy nazwe zapisu
     *
     * @param name nazwa zapisu
     */
    public Saves(String name) {
        this.name = name;
    }

    /**
     * Metoda zapisujaca stan gry.
     * Kopiuje ona wszystkie jednostki swiata gry i zapisuje informacje o grze
     *
     * @param world swiat gry
     */
    public void save(World world) {
        entities.clear();
        for (Entity e : world.getEntities()) {
            entities.add(e.copy());
        }

        points = Main.getPoints();
        lives = Main.getLives();
        level = Main.getLevel();
        date = LocalDateTime.now();


        Main.nameOfScreenshotForSave = name;
        Main.takeScreenshotForSave = true;

    }

    /**
     * Getter puntkow przechowywanych w danych zapisie
     *
     * @return zwraca ilosc punktow
     */
    public int getPoints() {
        return points;
    }

    /**
     * Zwraca ilosc zyc zapisanych w danych save
     *
     * @return zwraca ilsoc zyc
     */
    public int getLives() {
        return lives;
    }

    /**
     * Zwraca level zapisany w danym save
     *
     * @return zwraca level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Zwraca date i godzine zapisanego stanu gry
     *
     * @return zwraca date i czas
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sprawdza czy zapis zostal zaladowany z pliku
     *
     * @return zapisany czy nie
     */
    public boolean isLoaded() {
        return loaded;
    }


    /**
     * Wczytuje stan gry
     *
     * @param world swiat gry
     */
    public void load(World world) {
        if (loaded) {
            if (Main.getLevel() == 0) {
                Main.startGame();
            } else {
                world.clearEntities();
                for (Entity e : entities) {
                    world.addEntity(e.copy());
                }

            }
        }
    }

    /**
     * Lapie wyjatki i wywoluje wczytywanie zapisu z pliku
     *
     * @return zwraca wczytany z pliku stan gry
     */
    public Saves loadFromFile() {
        String nameTmp = new String();
        nameTmp = this.name;
        Saves save = new Saves();
        try {
            try {
                try {
                    save = readFile();
                    save.loaded = true;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        save.name = nameTmp;
        return save;
    }

    /**
     * Lapie wyjatki i wywoluje zapis do pliku
     */
    public void saveToFile() {
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Zapis obiektu Save do pliku
     *
     * @throws IOException wyjatek wyjscia/wejscia
     */
    public void writeToFile() throws IOException {
        ObjectOutputStream objectOutputstream = new ObjectOutputStream(new FileOutputStream("saves/" + name + ".bin"));
        objectOutputstream.writeObject(this);
    }

    /**
     * Wczytuje obiekt klasy Save z pliku
     *
     * @return zwraca zapis
     * @throws IOException            wyjatek wyjscia/wejscia
     * @throws ClassNotFoundException wyjatek nie znalezionej klasy
     * @throws NullPointerException   wyjatek pustego wskaznika
     */
    public Saves readFile() throws IOException, ClassNotFoundException, NullPointerException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("saves/" + name + ".bin"));
        Saves a = (Saves) objectInputStream.readObject();
        return a;
    }
}
