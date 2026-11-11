package gra.gui;


import gra.game.Main;
import gra.io.Window;
import gra.render.Draw;

/**
 * Intro
 */
public class Intro {
    /**
     * Okno gry
     */
    Window window;
    /**
     * Aktualny czas intra
     */
    float time;
    /**
     * Szerokosc ekranu
     */
    float width;
    /**
     * Czas ladowania intra
     */
    float loadingTime;
    /**
     * Losowy napis
     */
    String garbage_content;

    /**
     * Konstruktor
     * @param window okno
     */
    public Intro(Window window) {
        this.window = window;
        loadingTime = 400;
    }

    /**
     * Aktualizacja stanu intra
     * @param delta delta aktualizacji. 1 / liczba klatek na
     */
    public void update(double delta) {
        time += delta * 100;
        width += window.getWidth() / loadingTime;
    }

    /**
     * Renderowanie intra
     */
    public void render() {
        if (time < 100) garbage_content = "LADOWANIE TEKSTUR";
        else if (time > 100 && time < 200) garbage_content = "LADOWANIE MODELI";
        else if (time > 200 && time < 250) garbage_content = "LADOWANIE EASTER EGGOW";
        else if (time > 250 && time < 300) garbage_content = "SCIAGANIE TROJANOW";
        else if (time > 300 && time < 350) garbage_content = "WYSYLANIE HASEL ZAPISANYCH W PRZEGLADARCE";
        else if (time > 350) garbage_content = "PRZERWA NA SZLUGA";
        Draw.stringCentered("LADOWANIE", 0, 0, 48, 48);
        Draw.string("WHITE", 0, -window.getHeight()/6f, width, 20);
        Draw.stringCentered(garbage_content, 0, -window.getHeight()/4f, 20);

        if (window.getInput().returnkey() > 0 || time > loadingTime) {
            Main.state = Main.State.Menu;
        }
    }
}
