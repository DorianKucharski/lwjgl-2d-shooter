package gra.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


import gra.entity.Entity;
import gra.gui.*;
import gra.gui.Menu;
import gra.io.*;
import gra.io.Window;
import gra.world.Saves;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import gra.render.Assets;
import gra.render.Camera;
import gra.render.Shader;
import gra.world.TileRenderer;
import gra.world.World;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Główna klasa gry
 */
public class Main {

    /**
     * Wskaźnik na okno GLFW
     */
    private static long windowId;
    /**
     * Świat gry
     */
    private static World world;
    /**
     * Okno gry
     */
    private static Window window;
    /**
     * Kamera gry
     */
    private static Camera camera;
    /**
     * Główny shader
     */
    private static Shader shader;

    /**
     * Menu gry
     */
    private static Menu menu;
    /**
     * Pasek statystyk
     */
    private static Bar bar;
    /**
     * Tablica wyników
     */
    private static ScoreBoard scoreBoard;
    /**
     * Napisy końcowe
     */
    private static Credits credits;
    /**
     * Intro
     */
    private static Intro intro;
    /**
     * Cutscenes
     */
    private static Cutscenes cutscenes;
    /**
     * Quick save
     */
    public static Saves quickSave;
    /**
     * Nazwa pliku do którego zapisany zostanie zrzut ekran, używany do menu zapisywania i wczytywania
     */
    public static String nameOfScreenshotForSave;
    /**
     * Zmienna określająca czy w następnej klatce wykonać zrzut ekranu.
     */
    public static boolean takeScreenshotForSave;

    /**
     * Aktualna liczba uzyskanych punktów
     */
    private static int points;
    /**
     * Aktualna ilość żyć gracza
     */
    private static int lives;
    /**
     * Aktualny etap gry
     */
    private static int level;
    /**
     * Czy krew ma być widoczna
     */
    public static boolean gore = true;
    /**
     * Poziom trudność, przyjmuje wartości 1 - łatwy, 2 - średni, 3 - trudny
     */
    public static int difficulty = 1;
    /**
     * Czy gra jest zatrzymana
     */
    public static boolean isPaused;
    /**
     * Aktualny stan pętli gry
     */
    public static State state;

    /**
     * Enum stanów gry
     */
    public enum State {
        Menu, Game, Scoreboard, Credits, Intro, Cutscenes
    }

    /**
     * Metoda z główną pętlą gry.
     */
    public static void Main() {
        /**
         * Ustawienie wywołań zwrotnych okna
         */
        Window.setCallbacks();
        /**
         * Inicjalizacja biblioteki GLFW
         */
        if (!glfwInit()) {
            System.exit(1);
        }

        /**
         * Ustawienie okna, jego szerokosci, wysokości i nazwy.
         */
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        window = new Window();
        window.setSize(width, height);
        window.setFullscreen(false);
        window.createWindow("Gra");
        /**
         * Przypisanie wskaznika obiektu window z biblioteki GLFW
         */
        windowId = window.getWindow();

        /**
         * Ustawienie możliwości bbllioteki graficznej. Włączenie blendowania grafiki, ustawienie trybu blendowania,
         * ustawienie możliwości teksturowania tekstur 2D
         */
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        /**
         * Inicjalizacja modelu obiektów.
         */
        Assets.initAsset();
        /**
         * Utworzenie obiektu renderującego kafelki
         */
        TileRenderer tiles = new TileRenderer();

        /**
         * Ustawienie stanu głównej pętli gry
         */
        state = State.Intro;
        /**
         * Inicjalizacja kamery
         */
        camera = new Camera(window.getWidth(), window.getHeight());
        /**
         * Inicjalizacja głównego shadera
         */
        shader = new Shader("shader");
        /**
         * Inicjalizacje elementów interfejsu
         */
        menu = new Menu(window);
        bar = new Bar();
        scoreBoard = new ScoreBoard(window);
        credits = new Credits(window);
        intro = new Intro(window);
        cutscenes = new Cutscenes(window);

        /**
         * Inicjalizacja quick save'a
         */
        quickSave = new Saves("quickSave");

        /**
         * Inicjalizacje zmiennych odpowiedzialnych za działanie pętli gry
         */
        double frame_cap = 1.0 / 60.0;
        double time = Timer.getTime();
        double unprocessed = 0;

        Audio.playMusic();

        /**
         * Główna pętla gry
         */
        while (!window.shouldClose()) {
            /**
             * Czy gra może być renderowana
             */
            boolean can_render = false;

            /**
             * Aktualny czas
             */
            double time_2 = Timer.getTime();
            /**
             * Czas który upłynął od ostatniego renderowania
             */
            double passed = time_2 - time;
            /**
             * Liczba aktualizacji stanu gry bez renderowania
             */
            unprocessed += passed;
            /**
             * Przypisanie aktualnego czasu do zmiennej time
             */
            time = time_2;

            /**
             * Pętla aktualizaowania stanu gry. Pętla wykonuje się do momentu w którym powinno nastąpić renderowanie
             */
            while (unprocessed >= frame_cap) {
                /**
                 * Odczytywanie wciśniętych klawiszy, F5 odpowiada za quick save, F8 za quick load,
                 * F9 za zrobienie zrzutu ekarnu, Escape za pokazanie i ukrycie menu
                 */
                if (window.getInput().isKeyPressed(org.lwjgl.glfw.GLFW.GLFW_KEY_F5)) {
                    quickSave.save(world);
                    quickSave.saveToFile();
                } else if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_F8)) {
                    quickSave = quickSave.loadFromFile();
                    quickSave.load(world);
                } else if (window.getInput().isKeyPressed(GLFW_KEY_F9)) {
                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                    String formattedDate = date.format(formatter);
                    ScreenShots.take("screenshots/" + formattedDate);
                } else if (window.getInput().isKeyReleased(GLFW_KEY_ESCAPE) && state == State.Game) {
                    menu.reset();
                    state = State.Menu;
                    window.update();
                    isPaused = true;
                } else if (window.getInput().isKeyReleased(GLFW_KEY_ESCAPE) && state == State.Menu && world != null) {
                    menu.reset();
                    state = State.Game;
                    window.update();
                }

                /**
                 * Sprawdzenie czy okno zmieniło wielkość, jeśli tak aktualizowany jest stan kamery, menu oraz świata.
                 */
                if (window.hasResized()) {
                    camera.setProjection(window.getWidth(), window.getHeight());
                    menu.resizeCamera(window);
                    if (state == State.Game) {
                        world.calculateView(window);
                    }
                    glViewport(0, 0, window.getWidth(), window.getHeight());
                }

                /**
                 * Wykonywanie poszczególnych metod stanów pętli. Jeżeli pętla jest w stanie GAME wykonywane są
                 * metody związane z właściwą grą, jeżeli MENU - wykonywane są metody odpowiedzialne za wykonywanie menu,
                 * Credis - napisy końcowe, Cutscenes - cutscenki
                 */
                if (state == State.Game && !isPaused) {
                    world.update((float) frame_cap, window, camera);
                    world.correctCamera(camera, window);
                    if (world.player.getTransform().pos.x > 620) {
                        cutscenes.init(Cutscenes.NEXT_LEVEL);
                        state = State.Cutscenes;
                    }
                } else if (state == State.Menu) {
                    menu.update(window.getInput());
                } else if (state == State.Credits) {
                    credits.update(frame_cap);
                } else if (state == State.Intro) {
                    intro.update(frame_cap);
                } else if (state == State.Cutscenes) {
                    cutscenes.update(frame_cap);
                }
                /**
                 * Metoda statyczna sprawdzająca czy wpisane zostały kody
                 */
                Konami.check(window);
                /**
                 * Aktualizacja okna
                 */
                window.update();
                /**
                 * Zmniejszenie zmiennej odpowiedzialnej za wyjście z pętli, wyrażającej ilość aktualizacji stanu gry
                 * przed nastąpieniem klatki animacji
                 */
                unprocessed -= frame_cap;
                /**
                 * Czy możliwe jest renderowanie
                 */
                can_render = true;
            }

            /**
             * Renderowanie
             */
            if (can_render) {
                /**
                 * Wyczyszczenie buffera GL
                 */
                glClear(GL_COLOR_BUFFER_BIT);
                /**
                 * Wykonywanie metod renderowania w zależności od aktualnego stanu pętli zapisanego w zmiennej state
                 */
                if (state == State.Game) {
                    world.render(tiles, shader, camera, window);
                    bar.render();
                    isPaused = false;
                } else if (state == State.Menu) {
                    if (world != null) {
                        world.render(tiles, shader, camera, window);
                    }
                    if (!takeScreenshotForSave) {
                        menu.render();
                    } else {
                        if (world != null) {
                            world.render(tiles, shader, camera, window);
                            bar.render();
                        }
                        window.update();
                        window.swapBuffers();
                        ScreenShots.take("saves/" + nameOfScreenshotForSave);
                        takeScreenshotForSave = false;
                    }
                } else if (state == State.Scoreboard) {
                    scoreBoard.render();
                } else if (state == State.Credits) {
                    credits.render();
                } else if (state == State.Intro) {
                    intro.render();
                } else if (state == State.Cutscenes) {
                    cutscenes.render();
                }

                /**
                 * Zamiana bufferów
                 */
                window.swapBuffers();
            }
        }

        /**
         * Wyjście z gry. Usunięcie modeli, zamknięcie biblioteki GLFW
         */
        Assets.deleteAsset();
        glfwTerminate();
    }


    /**
     * Metoda wychodząca z głownej pętli gry
     */
    public static void exit() {
        Audio.stopMusic();
        if (world != null) {
            world.shutdown();
        }
        glfwSetWindowShouldClose(window.getWindow(), true);
    }

    /**
     * Metoda uruchamiająca nową grę.
     */
    public static void startGame() {
        level = 1;
        points = 0;
        switch (difficulty) {
            case 1:
                lives = 10;
                break;
            case 2:
                lives = 5;
                break;
            case 3:
                lives = 1;
                break;
        }
        bar.updateLives(lives);
        bar.updatePoints(points);
        world = new World("level1", camera);
        world.calculateView(window);
        state = State.Game;
    }

    /**
     * Metoda zmniejszająca ilość żyć gracza, kończy grę gdy ich ilość sięgnie zera.
     *
     * @param amount o ile zmniejszyć ilość żyć
     */
    public static void decreaseLives(int amount) {
        lives -= amount;
        bar.updateLives(lives);
        if (lives == 0) {
            gameOver();
        }
    }

    /**
     * Metoda ustawiająca ilość żyć
     *
     * @param amount
     */
    public static void setLives(int amount) {
        lives = amount;
        bar.updateLives(lives);
    }

    /**
     * Metoda zwiększająca ilość punktow. Aktualizuje stan paska statystyk
     *
     * @param amount
     */
    public static void increasePoints(int amount) {
        points += amount;
        bar.updatePoints(points);
        if (world.countEnemies() == 0) {

        }
    }

    /**
     * Metoda kończąca grę.
     */
    public static void gameOver() {
        cutscenes.init(Cutscenes.GAME_OVER);
        state = State.Cutscenes;
    }

    /**
     * Metoda wczytująca następny poziom.
     */
    public static void nextLevel() {
        level++;
        increasePoints(10000);
        if (level == 2) {
            world = new World("level2", camera);
            world.calculateView(window);
            Main.state = Main.State.Game;
        } else if (level == 3) {
            world = new World("level3", camera);
            world.calculateView(window);
            Main.state = Main.State.Game;
        } else if (level == 4) {
            world = new World("level4", camera);
            world.calculateView(window);
            Main.state = Main.State.Game;
        } else if (level == 5) {
            world = new World("level5", camera);
            world.calculateView(window);
            Main.state = Main.State.Game;
        } else {
            state = State.Scoreboard;
        }
    }

    /**
     * Metoda zabijająca wszystkich przeciwników.
     */
    public static void killEnemies() {
        for (Entity entity : world.getEntities()) {
            if (entity.type != Entity.Type.Player) {
                world.deleteEntity(entity);
            }
        }
    }


    /**
     * Getter punktów
     *
     * @return ilość punktów
     */
    public static int getPoints() {
        return points;
    }

    /**
     * Getter ilości żyć
     *
     * @return ilość żyć
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Getter aktualnego poziomu
     *
     * @return poziom
     */
    public static int getLevel() {
        return level;
    }

    /**
     * Getter świata
     *
     * @return świat
     */
    public static World getWorld() {
        return world;
    }

    /**
     * Setter świata
     *
     * @param world świat
     */
    public static void setWorld(World world) {
        Main.world = world;
    }

    /**
     * Metoda resetująca stan napisów końcowych
     */
    public static void initCredits() {
        credits.init();
    }

    /**
     * Getter kamery
     *
     * @return kamera
     */
    public static Camera getCamera() {
        return camera;
    }

    /**
     * Getter okna
     *
     * @return okno
     */
    public static Window getWindow() {
        return window;
    }

    /**
     * Getter głównego shadera
     *
     * @return shader
     */
    public static Shader getShader() {
        return shader;
    }

    /**
     * Główna metoda
     *
     * @param args
     */
    public static void main(String[] args) {
        Main();
    }


}
