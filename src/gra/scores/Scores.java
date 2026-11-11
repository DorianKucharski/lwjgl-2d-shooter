package gra.scores;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Klasa wynikow. Przechowuje wyniki, oraz dokonuje wszystkich metod zwiazanych z wynikami, tj. wczytywywanie, zapisywanie,
 * porownywanie itp.
 */
public class Scores implements Serializable {

    /**
     * Lista wynikow
     */
    List<Score> scores = new ArrayList<>();
    /**
     * Flaga oznaczajaca czy wyniki sa juz zapisane
     */
    private boolean saved = false;

    /**
     * Sprawdza czy wyniki sa wczytane. Robi to poprzez sprawdzenie rozmiaru listy wynikow
     * @return true jesli sa juz wczytane, false jesli nie
     */
    public boolean areLoaded(){
        if (scores.size() == 0) return false;
        else return true;
    }

    /**
     * Getter stanu zapisania
     * @return zwraca ture jesli wyniki zostaly zapisane, false jesli nie
     */
    public boolean areSaved(){
        return saved;
    }

    /**
     * Dodawanie wyniku do listy wynikow. Po dodaniu sortuje liste i zwraca pozycje gracza na tablicy wynikow
     * @param points ilosc punktow
     * @param name nazwa gracza
     * @return pozycja wyniku w klasyfikacji
     */
    public int add(int points, String name){
        Score score = new Score(points, name);
        scores.add(score);
        sort();
        return scores.indexOf(score);
    }

    /**
     * Sprawdza czy dany wynik trafi do tablicy wynikow
     * @param points ilosc uzyskanych punktow
     * @return true jesli wynik znajdzie sie na tablicy najl,epszych wynikow, false jesli nie
     */
    public boolean record(int points){
        for (int i = 0; i < scores.size(); i++) {
            if (points > this.getPoints(i)) return true;
        }
        if (scores.size() <10) return true;
        return false;
    }

    /**
     * Zwraca rozmiar tablicy wynikow
     * @return rozmiar
     */
    public int size(){
        return scores.size();
    }

    /**
     * Zwraca ilosc punktow na danym miejscu w tablicy wynikow
     * @param index miejsce w tablicy
     * @return ilosc punktow
     */
    public int getPoints(int index){
        return scores.get(index).points;
    }

    /**
     * Zwraca nazwe gracza na danym miejscu w tablicy wynikow.
     * @param index miejsce w tablicy
     * @return nazwa gracza
     */
    public String getName(int index){
        return scores.get(index).name;
    }

    /**
     * Sortowanie tablicy wynikow
     */
    public void sort(){
        Collections.sort(scores);
        if (scores.size() >= 11) scores.remove(scores.size()-1);
    }

    /**
     * Wczytywanie tablicy wynikow. Odwoluje sie do metody wczytywania z pliku, lapie wyjatki.
     * @return tablica wynikow
     */
    public static Scores load(){
        Scores scores = new Scores();
        try {
            try {
                scores = readFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return scores;
    }

    /**
     * Zapisywanie tablicy wynikow. Odwoluje sie do metody zapisania do pliku i lapie wyjatki.
     */
    public void save(){
        try {
            writeToFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        saved = true;
    }

    /**
     * Zapisywanie do pliku
     * @throws IOException wyjatek wejscia/wyjscia
     */
    public void writeToFile() throws IOException {
        ObjectOutputStream objectOutputstream = new ObjectOutputStream(new FileOutputStream("Wyniki.bin"));
        objectOutputstream.writeObject(this);
    }

    /**
     * Wczytywanie pliku
     * @return zwraca obiekt klasy Scores, czyli wyniki odczytane z pliku
     * @throws IOException wyjatek wejscia/wyjscia
     * @throws ClassNotFoundException wyjatek klasa nie znaleziona
     */
    public static Scores readFile() throws IOException, ClassNotFoundException{
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Wyniki.bin"));
        Scores a = (Scores) objectInputStream.readObject();
        return a;
    }



}
