package gra.gui;


import gra.game.Main;
import gra.world.Saves;
import gra.render.*;
import gra.io.Input;
import gra.io.Window;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Menu wczytywania i zapisywania stanow gry
 */
public class SaveLoadMenu {
    Menu menu;

    /**
     * Przycisk slotu
     */
    private Button slot1Button;
    /**
     * Przycisk slotu
     */
    private Button slot2Button;
    /**
     * Przycisk slotu
     */
    private Button slot3Button;
    /**
     * Przycisk slotu
     */
    private Button slot4Button;
    /**
     * Przycisk slotu
     */
    private Button exitButton;

    /**
     * Zdjęcia stanu gry
     */
    private Texture save1_pic;
    /**
     * Zdjęcia stanu gry
     */
    private Texture save2_pic;
    /**
     * Zdjęcia stanu gry
     */
    private Texture save3_pic;
    /**
     * Zdjęcia stanu gry
     */
    private Texture save4_pic;
    /**
     * Tryb menu.
     */
    public int mode = 0;
    /**
     * Stała trybu menu
     */
    public final int SAVE = 1;
    /**
     * Stałą trybu menu
     */
    public final int LOAD = 0;

    /**
     * Zapis gry
     */
    private Saves save1;
    /**
     * Zapis gry
     */
    private Saves save2;
    /**
     * Zapis gry
     */
    private Saves save3;
    /**
     * Zapis gry
     */
    private Saves save4;

    /**
     * Względna szerokość przycisków
     */
    int width;
    /**
     * Względna wysokość przycisków
     */
    int height;

    /**
     * Konstruktor. Inicjalizuje sloty zapisu, wywołuje metody wczytywania zapisów oraz ich zrzutów ekranu.
     * Inicjalizuje przyciski
     * @param window okno gry
     * @param menu główne menu
     */
    public SaveLoadMenu(Window window, Menu menu) {
        this.menu = menu;
        save1 = new Saves("save1");
        save2 = new Saves("save2");
        save3 = new Saves("save3");
        save4 = new Saves("save4");

        loadSaves();
        loadPictures();

        width = window.getWidth()/5;
        height = window.getHeight()/20;

        slot1Button = new Button(new Vector2f(0, height*2), new Vector2f(width, height));
        slot2Button = new Button(new Vector2f(0, 0), new Vector2f(width, height));
        slot3Button = new Button(new Vector2f(0, -height*2), new Vector2f(width, height));
        slot4Button = new Button(new Vector2f(0, -height*4), new Vector2f(width, height));
        exitButton = new Button(new Vector2f(0, -height*6), new Vector2f(width, height), "POWROT");

    }

    /**
     * Wczytywanie stanów gry z plików
     */
    void loadSaves() {
        save1 = save1.loadFromFile();
        save2 = save2.loadFromFile();
        save3 = save3.loadFromFile();
        save4 = save4.loadFromFile();
    }

    /**
     * Wczytywanie zrzutów ekranu zapisanych stanów gry.
     */
    void loadPictures() {
        if (save1.isLoaded()) {
            save1_pic = new Texture("/saves/save1.png");
        }
        if (save2.isLoaded()) {
            save2_pic = new Texture("/saves/save2.png");
        }
        if (save3.isLoaded()) {
            save3_pic = new Texture("/saves/save3.png");
        }
        if (save4.isLoaded()) {
            save4_pic = new Texture("/saves/save4.png");
        }
    }

    /**
     * Aktualizacja stanu menu. Odpowiada za obsługę przycisków.
     * @param input wejście
     */
    public void update(Input input) {
        if (mode == LOAD && save1.isLoaded()){
            slot1Button.update(input);
        }
        if (mode == LOAD && save2.isLoaded()){
            slot2Button.update(input);
        }
        if (mode == LOAD && save3.isLoaded()){
            slot3Button.update(input);
        }
        if (mode == LOAD && save4.isLoaded()){
            slot4Button.update(input);
        }
        if (mode == SAVE){
            slot1Button.update(input);
            slot2Button.update(input);
            slot3Button.update(input);
            slot4Button.update(input);
        }
        exitButton.update(input);

        if (slot1Button.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            buttonUpdate(save1);
        }
        if (slot2Button.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            buttonUpdate(save2);
        }
        if (slot3Button.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            buttonUpdate(save3);
        }
        if (slot4Button.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            buttonUpdate(save4);
        }

        if (exitButton.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            menu.currentMenu = menu.MAIN_MENU;
        }
    }

    /**
     * Renderowanie menu, wywoływanie renderowania przycisków.
     */
    public void render() {
        slot1Button.render(menu.camera, menu.sheet, menu.shader);
        slot2Button.render(menu.camera, menu.sheet, menu.shader);
        slot3Button.render(menu.camera, menu.sheet, menu.shader);
        slot4Button.render(menu.camera, menu.sheet, menu.shader);
        exitButton.render(menu.camera, menu.sheet, menu.shader);

        buttonRender(slot1Button, save1, save1_pic);
        buttonRender(slot2Button, save2, save2_pic);
        buttonRender(slot3Button, save3, save3_pic);
        buttonRender(slot4Button, save4, save4_pic);

    }

    /**
     * Obsługa wciśniecią przycisku, w zależności od trbyu menu save/load odpowiada za wczytywanie lub zapisywanie stanu gry.
     * @param save slot zapisu skojarzony z przyciskiem
     */
    private void buttonUpdate(Saves save){
        if (mode == 0) {
            save.load(Main.getWorld());
        } else {
            save.save(Main.getWorld());
            save.saveToFile();
        }
        Main.state = Main.State.Game;
        Main.isPaused = false;
        menu.currentMenu = menu.MAIN_MENU;
    }

    /**
     * Renderowanie przycisków oraz ich zawartości, informacji o zapisie stanu gry, ilości żyć, punktów, renderowanie
     * zrzutów ekranu, daty itp.
     * @param button przycisk zapisu
     * @param save slot zapisu
     * @param picture zrzut ekranu zapisu
     */
    private void buttonRender(Button button, Saves save, Texture picture){
        if (save.isLoaded()) {
            int gap = button.getHeight()/3;
            Draw.string("POZIOM: " + save.getLevel(), button.getCornerUpLeft().x+10, button.getCornerUpLeft().y-10, 10);
            Draw.string("PUNKTY: " + save.getPoints(), button.getCornerUpLeft().x+10, button.getCornerUpLeft().y-10-gap, 10);
            Draw.string("ZYCIA:" + save.getLives(), button.getCornerUpLeft().x+10, button.getCornerUpLeft().y-10-gap*2, 10);

            LocalDateTime date = save.getDate();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedDate = date.format(formatterDate);
            String formattedTime = date.format(formatterTime);

            Draw.string(formattedDate, button.getCenter().x, button.getCornerUpLeft().y-10, 10);
            Draw.string(formattedTime,  button.getCenter().x, button.getCornerUpLeft().y-10-gap, 10);

            float width = button.getHeight()/2f-5;
            float height = width/9*16;
            Draw.picture(picture, button.getCornerUpRight().x-height+5, button.getCornerUpLeft().y-width+4, height, width);
        } else {
            Draw.stringCentered("WOLNE", 0, button.getCenter().y, 24);
        }
    }

    /**
     * Obsługa zmiany rozmiaru okna.
     * @param window okno gry
     */
    public void resizeCamera(Window window) {
        menu.camera.setProjection(window.getWidth(), window.getHeight());
    }
}
