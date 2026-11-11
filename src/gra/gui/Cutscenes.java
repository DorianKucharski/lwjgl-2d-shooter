package gra.gui;

import gra.game.Main;
import gra.io.Window;
import gra.render.Draw;

/**
 * Cutscenes
 */
public class Cutscenes {
    /**
     * Okno gry
     */
    Window window;
    /**
     * Pole odmierzejące czas cutscenki
     */
    float time;
    /**
     * Etap cutscenek
     */
    int stage;
    /**
     * Stała cutscenki z przegraną
     */
    public static final int GAME_OVER = 0;
    /**
     * Stała cutscenki z następnym poziomem
     */
    public static final int NEXT_LEVEL = 1;

    /**
     * Konstruktor
     * @param window okno gry
     */
    public Cutscenes(Window window) {
        this.window = window;
    }

    /**
     * Resetowanie stanu cutscenek i ustawienie etapu cutscenek
     * @param stage etap cutscenek określany przez stałe
     */
    public void init(int stage) {
        time = 0;
        this.stage = stage;
    }

    /**
     * Metoda aktualizująca stan cutscenek
     * @param delta
     */
    public void update(double delta) {
        time += delta * 100;
    }

    /**
     * Renderowanie cutscenek. Metoda wywołuje jedną z dwóch metod renderowania w zależości od wartości pola stage.
     */
    public void render() {
        switch (stage) {
            case GAME_OVER:
                gameOver();
                break;
            case NEXT_LEVEL:
                nextLevel();
                break;
        }
        if (window.getInput().returnkey() > 0) {
            time = 999;
            window.update();
        }
    }

    /**
     * Renderowanie cutscenki z przegraną
     */
    private void gameOver() {
        Draw.stringCentered("PRZEGRALES", 0, 0, 48);
        if (time > 100) {
            Main.state = Main.State.Scoreboard;
        }
    }

    /**
     * Renderowanie cutscenki z przejściem do następnego poziomu
     */
    private void nextLevel(){
        Draw.stringCentered("NEXT LEVEL", 0, 0, 48);
        if (time > 100) {
            Main.nextLevel();
        }
    }
}
