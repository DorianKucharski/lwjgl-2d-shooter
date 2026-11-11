package gra.scores;

import java.io.Serializable;

/**
 * Klasa wyniku. Przechowuje wynik i nazwe gracza.
 * Implementuje Serializacje i Comparable
 */
public class Score implements Serializable, Comparable<Score> {

    /**
     * Punkty
     */
    public int points;
    /**
     * Nazwa gracza
     */
    public String name;

    /**
     * Konstruktor wyniku
     * @param points punkty
     * @param name nazwa gracza
     */
    public Score(int points, String name){
        this.points = points;
        this.name = name;
    }

    /**
     * Przesloniety komparator wynikow. Porownuje punkty wynikow.
     * @param o wynik do porownania
     * @return zwraca 1 jezeli drugi wynik jest wiekszy, -1 jesli pierwszy jest wiekszy i 0 jesli sa rowne
     */
    @Override
    public int compareTo(Score o) {
        if (this.points < o.points) return 1;
        if (this.points > o.points) return -1;
        return 0;
    }
}
