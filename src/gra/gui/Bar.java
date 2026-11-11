package gra.gui;

import gra.game.Main;
import gra.render.Draw;

/**
 * Klasa bar - pasek stanu gry
 */
public class Bar {
    /**
     * Skala napisów
     */
    private int scale = 32;
    /**
     * Wysokość na której znajduje się pasek
     */
    private int height;
    /**
     * Środek ekranu
     */
    private int width;
    /**
     * Ilość punktów
     */
    private String points;
    /**
     * Ilość żyć
     */
    private int lives;

    /**
     * Konstruktor
     */
    public Bar() {
        height = Main.getWindow().getHeight()/2 - scale;
        width = Main.getWindow().getWidth()/2 - scale-10;
        points = "PUNKTY 00000000";
    }

    /**
     * Aktualizacja ilości punktów
     * @param points ilość punktów
     */
    public void updatePoints(int points) {
        int len = 8 - String.valueOf(points).length();
        this.points = "PUNKTY " + "0".repeat(Math.max(0, len)) + points;
    }

    /**
     * Aktualizacja ilość żyć.
     * @param lives ilość żyć
     */
    public void updateLives(int lives) {
        this.lives = lives;
    }

    /**
     * Metoda renderująca pasek
     */
    public void render() {
        for (int i = 0; i < lives; i++) {
            Draw.string("HEART", -width+i*scale*2.5f ,height, scale, scale);
        }
        Draw.string(points, 0, height, scale, scale);
    }
}
