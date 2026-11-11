package gra.gui;

import gra.game.Main;
import gra.render.Draw;
import gra.io.Window;
import gra.scores.Scores;
import org.lwjgl.glfw.GLFW;

/**
 * Tablica wynikow
 */
public class ScoreBoard {
    /**
     * Nazwa gracza
     */
    private String name = new String();
    /**
     * Wskaznik na okno
     */
    private Window window;
    /**
     * Obiekt przechowujacy wyniki
     */
    private Scores scores = new Scores();
    /**
     * Miejsce zajete przez gracza
     */
    private int miejsce = -1;

    /**
     * Czy zaladowano wyniki
     */
    private boolean loaded = false;
    /**
     * Czy zapisano wyniki
     */
    private boolean saved = false;


    /**
     * Konstruktor tablicy wynikow
     *
     * @param window wskaznik na okno
     */
    public ScoreBoard(Window window) {
        this.window = window;
    }

    private int stage;

    /**
     * Glowna metoda renderujaca tablice wynikow.
     * Przechodzi przez poszczegolne ekrany tablicy wynikow. Wczytuje wyniki i zapisuje.
     *
     */
    public void render() {
        if (!loaded) {
            scores = Scores.load();
            loaded = true;
        }
        if (stage == 0) {
            renderScore();
        }
        if (stage == 1){
            if (!saved) {
                miejsce = scores.add(Main.getPoints(), name.substring(0, name.length() - 1));
                scores.save();
                saved = true;
            }
            rednerTable();
        }
        if (stage == 2){
            clear();
            Main.setWorld(null);
            if (Main.getLives() > 0){
                Main.initCredits();
                Main.state = Main.State.Credits;
            } else {
                Main.state = Main.State.Menu;
            }

        }
    }

    /**
     * Renderowanie ekranu z uszyskanym wynikiem i wpisywaniem imienia
     */
    public void renderScore() {
        Draw.string("BLACK", 0, 0, window.getWidth(), window.getHeight());
        Draw.stringCentered("UZYSKALES: " + Main.getPoints() + " PUNKTOW", 0 ,300, 24);

        if (scores.record(Main.getPoints())) {
            Draw.stringCentered("ZALAPALES SIE DO TABLICY WYNIKOW", 0 ,200, 12);
            Draw.stringCentered("WPISZ SWOJE IMIE", 0, 100, 12, 12);
            Draw.stringCentered(name, 0, 0, 24, 24);

            int i = window.getInput().returnkey();
            if (i != 0) {
                if (i == 259 && name.length()>0) name = name.substring(0, name.length() - 1);
                if ((i >= 65 && i <= 90) || i == 32 || i == 257) name += (char) i;
                if (i == 257) {
                    stage = 1;
                    window.update();
                }
            }
        } else {
            Draw.stringCentered("NIE ZALAPALES SIE DO TABLICY WYNIKOW", 0, 100, 12);
            if (window.getInput().returnkey() > 0) {
                stage = 2;
                window.update();
            }
        }

    }

    /**
     * Renderowanie tablicy wynikow
     */
    public void rednerTable() {
        if (!scores.areLoaded()) {
            scores = Scores.load();
        }

        Draw.string("BLACK", 0, 0, window.getWidth(), window.getHeight());
        Draw.stringCentered("WYNIKI", 0, 350, 84);

        for (int i = 0; i < scores.size(); i++) {
            if (i == miejsce) {
                Draw.stringCentered(scores.getPoints(i) + " " + scores.getName(i), 0, 200 - i * 50, 24);
            } else {
                Draw.stringCentered(scores.getPoints(i) + " " + scores.getName(i), 0, 200 - i * 50, 12);
            }
        }

        if(window.getInput().isKeyPressed(GLFW.GLFW_KEY_ENTER)){
            stage = 2;
            window.update();
        };
    }

    /**
     * Ustawianie warunkow poczatkowych, resetowanie zmiennych.
     */
    public void clear() {
        name = "";
        miejsce = -1;
        stage = 0;
        loaded = false;
        saved = false;
    }


}
