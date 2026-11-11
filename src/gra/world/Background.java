package gra.world;
import gra.io.Window;
import gra.render.*;


/**
 * Tło gry
 */
public class Background {
    /**
     * Pierwsza warstwa tła
     */
    BackgroundLayer backgroundLayer1;
    /**
     * Druga warstwa tła
     */
    BackgroundLayer backgroundLayer2;
    /**
     * Trzecia warstwa tła
     */
    BackgroundLayer backgroundLayer3;


    /**
     * Konstruktor. Inicjalizuje warstwy tła
     * @param world świat gry
     * @param window okno gry
     */
    public Background(World world, Window window) {
        backgroundLayer1 = new BackgroundLayer(world, window, new Texture("/textures/map/background/1.png"));
        backgroundLayer2 = new BackgroundLayer(world, window, new Texture("/textures/map/background/2.png"));
        backgroundLayer3 = new BackgroundLayer(world, window, new Texture("/textures/map/background/3.png"));
    }

    /**
     * Przesuwanie warstw tła, każdą warstwę przesuwa z inną prędkośćią
     * @param delta delta czasu
     */
    public void update(float delta) {
        backgroundLayer1.update(-delta * 10);
        backgroundLayer2.update(-delta * 20);
        backgroundLayer3.update(-delta * 30);

    }

    /**
     * Renderowanie tła
     * @param world świat gry
     * @param shader shader gry
     * @param window okno gry
     */
    public void render(World world, Shader shader, Window window) {
        backgroundLayer1.render(world, shader, window);
        backgroundLayer2.render(world, shader, window);
        backgroundLayer3.render(world, shader, window);

    }


}
