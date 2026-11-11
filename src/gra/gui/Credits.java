package gra.gui;

import gra.game.Main;
import gra.io.Window;
import gra.render.Draw;
import org.joml.Vector2f;


import java.util.ArrayList;
import java.util.List;

/**
 * Napisy końcowe
 */
public class Credits {
    /**
     * Główne okno
     */
    Window window;
    /**
     * Lista napisów
     */
    private List<String> credits = new ArrayList<>();
    /**
     * Tablica autorów
     */
    private String[] authors = {"DORIAN KUCHARSKI", "PAWEL KOPYCINSKI", "DAMIAN MARCINKOWSKI"};

    /**
     * Położenie pierwszego napisu na osi Y
     */
    float y = 0;
    /**
     * Dolna krawędź renderowania
     */
    float bottomLine;
    /**
     * Górna krawędź renderowania
     */
    float topLine;
    /**
     * Rozmiar dużej czcionki
     */
    float bigFont;
    /**
     * Rozmiar małej czcionki
     */
    float smallFont;

    /**
     * Konstruktor.
     * @param window okno gry
     */
    public Credits(Window window) {
        this.window = window;
        createStrings();
        bigFont = window.getHeight()/30f;
        smallFont = bigFont/2;
        bottomLine = -window.getHeight() / 2f - 100;
        topLine = window.getHeight() / 2f + bigFont*6;
    }

    /**
     * Resetowanie stanu napisów
     */
    public void init() {
        y = bottomLine;
    }

    /**
     * Aktualizacja stanu napisów
     * @param delta
     */
    public void update(double delta) {
        y += delta * 100;
    }

    /**
     * Renderowanie napisów
     */
    public void render() {
        for (int i = 0; i < credits.size(); i++) {
            float y2 = y - i * bigFont * 10;
            if (y2 > bottomLine && y2 < topLine) {
                Draw.stringCentered(credits.get(i), 0, y2, bigFont);
                y2 -= bigFont;
                Draw.stringCentered(authors[0], 0 , y2 - smallFont*3, smallFont);
                Draw.stringCentered(authors[1], 0 , y2 - smallFont*6, smallFont);
                Draw.stringCentered(authors[2], 0 , y2 - smallFont*9, smallFont);
            }
        }


        if (window.getInput().returnkey() > 0) {
            Main.state = Main.State.Menu;
        }
    }

    /**
     * Dodawanie napisów do listy napisów
     */
    public void createStrings() {
        credits.add("SCENARIUSZ");
        credits.add("REZYSERIA");
        credits.add("DIALOGI");
        credits.add("MONTAZ");
        credits.add("PRODUKCJA");
        credits.add("PROGRAMISCI ROZGRYWKI");
        credits.add("PROGRAMISCI SILNIKA");
        credits.add("PROGRAMISCI QUESTOW");
        credits.add("PROGRAMISCI INTERFEJSU");
        credits.add("RYSUNKI KONCEPCYJNE");
        credits.add("ARTYSCI POSTACI");
        credits.add("ARTYSCI LOKACJI");
        credits.add("PROJEKTANCI LOKACJI ");
        credits.add("ARTYSCI OSWIETLENIA");
        credits.add("SEKWENCJE FILMOWE");
        credits.add("ANNIMACJE POSTACI");
        credits.add("ARTYSCI TECHNICZNI");
        credits.add("EFEKTY WIZUALNE");
        credits.add("OPRAWA DZWIEKOWA");
        credits.add("LOKALIZACJA");
        credits.add("ANGIELSKA WERSJA JEZYKOWA");
        credits.add("KONTROLA JAKOSCI ROZGRYWKI");
        credits.add("KONTROLA JAKOSCI GRY");
        credits.add("KONTROLA JAKOSCI OSWIETLENIA");
        credits.add("KONTROLA JAKOSCI DZWIEKU");
        credits.add("MUZYKA");
        credits.add("UDZWIEKOWIENIE");
    }
}
