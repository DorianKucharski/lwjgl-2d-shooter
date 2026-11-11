package gra.gui;

import gra.game.Main;
import org.joml.Vector2f;

import gra.io.Input;
import gra.io.Window;
import org.lwjgl.glfw.GLFW;

/**
 * Klasa menu
 */
public class MainMenu {
    /**
     * Menu nadrzędne
     */
    Menu menu;
    /**
     * Przycisk nowa gra
     */
    private Button newGame;
    /**
     * Przycisk menu ładowania stanów gry
     */
    private Button load;
    /**
     * Przycisk menu zapisywania stanów gry
     */
    private Button save;
    /**
     * Przycisk menu ustawień
     */
    private Button settings;
    /**
     * Przycisk creditsów
     */
    private Button credits;
    /**
     * Przycisk wyjścia z gry
     */
    private Button exit;
    /**
     * Szerokość przycisków
     */
    float width;
    /**
     * Wysokość przycisków
     */
    float height;

    /**
     * Konstruktor głównego menu
     * @param window okno gry
     * @param menu Menu nadrzędne
     */
    public MainMenu(Window window, Menu menu) {
        this.menu = menu;

        width = window.getWidth() / 5f;
        height = window.getHeight() / 20f;

        newGame = new Button(new Vector2f(0, height * 2), new Vector2f(width, height), "NOWA GRA");
        load = new Button(new Vector2f(-width / 2, 0), new Vector2f(width / 2, height), "WCZYTAJ");
        save = new Button(new Vector2f(+width / 2, 0), new Vector2f(width / 2, height), "ZAPISZ");
        settings = new Button(new Vector2f(0, -height * 2), new Vector2f(width, height), "OPCJE");
        credits = new Button(new Vector2f(0, -height * 4), new Vector2f(width, height), "AUTORZY");
        exit = new Button(new Vector2f(0, -height * 6), new Vector2f(width, height), "WYJSCIE");
    }

    /**
     * Aktualizacja stanu menu, odczyt wybranego przycisku.
     * @param input interfejs wejścia
     */
    public void update(Input input) {
        newGame.update(input);
        load.update(input);
        if (Main.getWorld() != null){
            save.update(input);
        }
        settings.update(input);
        credits.update(input);
        exit.update(input);
        if (newGame.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Main.startGame();
        }
        if (load.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            menu.saveLoadMenu.loadSaves();
            menu.saveLoadMenu.loadPictures();
            menu.saveLoadMenu.mode = menu.saveLoadMenu.LOAD;
            menu.currentMenu = menu.SAVE_LOAD;
        }
        if (save.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            menu.saveLoadMenu.loadSaves();
            menu.saveLoadMenu.loadPictures();
            menu.saveLoadMenu.mode = menu.saveLoadMenu.SAVE;
            menu.currentMenu = menu.SAVE_LOAD;
        }

        if (settings.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            menu.currentMenu = menu.SETTINGS;
        }

        if (credits.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Main.initCredits();
            Main.state = Main.State.Credits;
        }

        if (exit.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Main.exit();
        }

    }

    /**
     * Renderowanie menu
     */
    public void render() {
        newGame.render(menu.camera, menu.sheet, menu.shader);
        load.render(menu.camera, menu.sheet, menu.shader);
        save.render(menu.camera, menu.sheet, menu.shader);
        settings.render(menu.camera, menu.sheet, menu.shader);
        credits.render(menu.camera, menu.sheet, menu.shader);
        exit.render(menu.camera, menu.sheet, menu.shader);
    }

    /**
     * Odświeżenie menu, w przypadku zmiany rozmiaru okna.
     * @param window okno
     */
    public void resizeCamera(Window window) {
        menu.camera.setProjection(window.getWidth(), window.getHeight());
    }

}
