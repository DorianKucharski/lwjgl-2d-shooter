package gra.gui;

import gra.game.Main;
import gra.io.Input;
import gra.io.Window;
import gra.render.*;

/**
 * Menu na
 */
public class Menu {
    /**
     * Shader gui
     */
    Shader shader;
    /**
     * Kamera gry
     */
    Camera camera;
    /**
     * Arkusz kafelków gui
     */
    SpriteSheet sheet;
    /**
     * Menu podrzędne - zapisywania/wczytywania stanów gry
     */
    SaveLoadMenu saveLoadMenu;
    /**
     * Menu podrzedne - główne
     */
    MainMenu mainMenu;
    /**
     * Menu podrzędne - ustawienia
     */
    SettingsMenu settingsMenu;
    /**
     * Stała głownego menu
     */
    final int MAIN_MENU = 0;
    /**
     * Stała menu zapisywania/wczytywania
     */
    final int SAVE_LOAD = 1;
    /**
     * Stała menu ustawień
     */
    final int SETTINGS = 2;
    /**
     * Bieżące menu
     */
    int currentMenu;

    /**
     * Konstruktor. Inicjalizuje shader menu, kamere, arkusz tekstur, inicjalizuje podrzędne mena.
     * @param window okno gry
     */
    public Menu(Window window) {
        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        sheet = new SpriteSheet("gui.png", 9);

        currentMenu = MAIN_MENU;
        saveLoadMenu = new SaveLoadMenu(window, this);
        mainMenu = new MainMenu(window, this);
        settingsMenu = new SettingsMenu(window, this);

    }

    /**
     * Obsługa zmiany rozmiaru okna. Wywołuje metodę obsługi w aktualnym menu podrzędnym.
     * @param window okno gry
     */
    public void resizeCamera(Window window) {
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        camera.setProjection(window.getWidth(), window.getHeight());
        if (currentMenu == MAIN_MENU){
            mainMenu.resizeCamera(window);
        } else if (currentMenu == SAVE_LOAD){
            saveLoadMenu.resizeCamera(window);
        } else if (currentMenu == SETTINGS){
            settingsMenu.resizeCamera(window);
        }

    }

    /**
     * Aktualizacja stanu menu, w zależności od aktualnego menu wywołuje jego metodę update.
     * @param input interfejs wejścia
     */
    public void update(Input input) {
        if (currentMenu == MAIN_MENU) {
            mainMenu.update(input);
        } else if (currentMenu == SAVE_LOAD) {
            saveLoadMenu.update(input);
        } else if (currentMenu == SETTINGS){
            settingsMenu.update(input);
        }
    }

    /**
     * Renderowanie menu, w zależności od aktualnego menu wyowołuje metodę renderowania menu.
     */
    public void render() {
        Draw.stringCentered("NAZWA GRY", 0, Main.getWindow().getHeight() / 20f * 8, 64);
        if (currentMenu == MAIN_MENU) {
            mainMenu.render();
        } else if (currentMenu == SAVE_LOAD) {
            saveLoadMenu.render();
        }else if (currentMenu == SETTINGS) {
            settingsMenu.render();
        }
    }

    /**
     * Powrót do menu głównego.
     */
    public void reset(){
        currentMenu = MAIN_MENU;
    }
}
