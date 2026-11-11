package gra.gui;

import gra.game.Main;
import gra.io.Audio;
import org.joml.Vector2f;

import gra.io.Input;
import gra.io.Window;
import gra.render.*;
import org.lwjgl.glfw.GLFW;

/**
 * Menu ustawień
 */
public class SettingsMenu {

    /**
     * Głowne menu
     */
    Menu menu;
    /**
     * Przycisk poziomu głośności muzyki
     */
    private Button musicVolume;
    /**
     * Przycisk zwiększania poziomu głośności muzyki
     */
    private Button musicVolumeIncrease;
    /**
     * Przycisk zmniejszania poziomu głośności muzyki
     */
    private Button musicVolumeDecrease;
    /**
     * Przycisk poziomu głośności dźwięków
     */
    private Button soundVolume;
    /**
     * Przycisk zwiększania poziomu głośności dźwięków
     */
    private Button soundVolumeIncrease;
    /**
     * Przycisk zmniejszania poziomu głośności dźwięków
     */
    private Button soundVolumeDecrease;
    /**
     * Przycisk gore
     */
    private Button gore;
    /**
     * Przycisk poziomu trudności
     */
    private Button difficulty;
    /**
     * Przycisk powrotu do głównego menu
     */
    private Button exit;
    /**
     * Względna szerokość przycisków
     */
    float width;
    /**
     * Względna wysokość przycisków
     */
    float height;

    /**
     * Konstruktor.Inicjalizuje przyciski, ustawia ich położenie oraz tekst.
     * @param window okno gry
     * @param menu główne menu
     */
    public SettingsMenu(Window window, Menu menu) {
        this.menu = menu;
        width = window.getWidth() / 5f;
        height = window.getHeight() / 20f;

        musicVolume = new Button(new Vector2f(0, height * 4), new Vector2f(width/6*4, height), generateText(Audio.volumeMusic), 20);
        musicVolumeDecrease = new Button(new Vector2f(-width/6*5, height * 4), new Vector2f(width/6, height), "-",32);
        musicVolumeIncrease = new Button(new Vector2f(width/6*5, height * 4), new Vector2f(width/6, height), "+",32);

        soundVolume = new Button(new Vector2f(0, 0), new Vector2f(width/6*4, height), generateText(Audio.volumeSound), 20);
        soundVolumeDecrease = new Button(new Vector2f(-width/6*5, 0), new Vector2f(width/6, height), "-",32);
        soundVolumeIncrease = new Button(new Vector2f(width/6*5, 0), new Vector2f(width/6, height), "+",32);

        difficulty = new Button(new Vector2f(0, -height * 4), new Vector2f(width, height), "");
        switch(Main.difficulty){
            case 1:
                difficulty.setText("LATWY");
                break;
            case 2:
                difficulty.setText("NORMALNY");
                break;
            case 3:
                difficulty.setText("TRUDNY");
                break;
        }

        gore = new Button(new Vector2f(0, -height * 6), new Vector2f(width, height), "",32);
        if (Main.gore){
            gore.setText("GORE: ON");
        } else {
            gore.setText("GORE: OFF");
        }

        exit = new Button(new Vector2f(0, -height * 8), new Vector2f(width, height), "POWROT");
    }

    /**
     * Aktualizacja menu. Odpowiada za obsługę przycisków, zmianę ich stanu.
     * @param input interfejs wejścia
     */
    public void update(Input input) {
        musicVolumeDecrease.update(input);
        musicVolumeIncrease.update(input);
        soundVolumeDecrease.update(input);
        soundVolumeIncrease.update(input);
        gore.update(input);
        difficulty.update(input);
        exit.update(input);

        if (musicVolumeDecrease.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Audio.changeMusicVolume(-10);
            musicVolume.setText(generateText(Audio.volumeMusic));
        }
        if (musicVolumeIncrease.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Audio.changeMusicVolume(10);
            musicVolume.setText(generateText(Audio.volumeMusic));
        }
        if (soundVolumeDecrease.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Audio.changeSoundVolume(-10);
            soundVolume.setText(generateText(Audio.volumeSound));
        }
        if (soundVolumeIncrease.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Audio.changeSoundVolume(10);
            soundVolume.setText(generateText(Audio.volumeSound));
        }
        if (gore.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Main.gore = !Main.gore;
            if (Main.gore){
                gore.setText("GORE: ON");
            } else {
                gore.setText("GORE: OFF");
            }
        }

        if (difficulty.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Main.difficulty++;
            if(Main.difficulty == 4){
                Main.difficulty = 1;
            }
            switch(Main.difficulty){
                case 1:
                    difficulty.setText("LATWY");
                    break;
                case 2:
                    difficulty.setText("NORMALNY");
                    break;
                case 3:
                    difficulty.setText("TRUDNY");
                    break;
            }

        }


        if (exit.selectedState == 2 && input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            menu.currentMenu = menu.MAIN_MENU;
        }

    }

    /**
     * Generuje tekst przycisków głośności.
     * @param value wartość która ma być przetworzona
     * @return tekst
     */
    private String generateText(float value){
        int place = (int) (8 + value/10);
        StringBuilder string = new StringBuilder("---------");
        string.setCharAt(place, '#');
        return string.toString();
    }

    /**
     * Renderowanie przycisków i napisów.
     */
    public void render() {
        Draw.stringCentered("GLOSNOSC MUZYKI", 0, height*6, 24);
        musicVolume.render(menu.camera, menu.sheet, menu.shader);
        musicVolumeDecrease.render(menu.camera, menu.sheet, menu.shader);
        musicVolumeIncrease.render(menu.camera, menu.sheet, menu.shader);
        Draw.stringCentered("GLOSNOSC DZWIEKOW", 0, height*2, 24);
        soundVolume.render(menu.camera, menu.sheet, menu.shader);
        soundVolumeDecrease.render(menu.camera, menu.sheet, menu.shader);
        soundVolumeIncrease.render(menu.camera, menu.sheet, menu.shader);
        Draw.stringCentered("POZIOM TRUDNOSCI", 0, -height*2, 24);
        difficulty.render(menu.camera, menu.sheet, menu.shader);
        gore.render(menu.camera, menu.sheet, menu.shader);
        exit.render(menu.camera, menu.sheet, menu.shader);
    }

    /**
     * Obsługa zmiany wielkości okna
     * @param window okno gry
     */
    public void resizeCamera(Window window) {
        menu.camera.setProjection(window.getWidth(), window.getHeight());
    }

}
