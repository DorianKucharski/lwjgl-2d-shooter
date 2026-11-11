package gra.world;

import gra.render.Assets;
import gra.entity.Transform;
import gra.io.Window;
import gra.render.Camera;
import gra.render.Shader;
import gra.render.Texture;
import org.joml.Matrix4f;

/**
 * Klasa warstwy tła.
 */
public class BackgroundLayer {
    /**
     * Transform części warstwy tła znajdującej się po lewej stronie
     */
    private Transform transform1;
    /**
     * Transform części warstwy tła znajdującej się w środku
     */
    private Transform transform2;
    /**
     * Transform części warstwy tła znajdującej się po prawej stronie
     */
    private Transform transform3;
    /**
     * Tekstura warstwy
     */
    private Texture texture;
    /**
     * Szerokość okna gry przeskalowana przez skale świata
     */
    float width;
    /**
     * Wysokość okna gry przeskalowana przez skale świata
     */
    float height;


    /**
     * Konstruktor.
     * Oblicza szerokość i wysokość, przypisuje teksturę, tworzy obiekty klasy transform na podstawie szerokośći i wysokości
     * @param world świat gry
     * @param window okno gry
     * @param texture tekstura warstwy
     */
    public BackgroundLayer(World world, Window window, Texture texture) {
        width = window.getWidth() * 1.0f / world.getScale() / 2;
        height = window.getHeight() * 1.0f / world.getScale() / 2;
        this.texture = texture;

        transform1 = new Transform(0, 0, width, height);
        transform2 = new Transform(transform1.pos.x + width * 2, 0, width, height);
        transform3 = new Transform(transform2.pos.x + width * 2, 0, width, height);
    }


    /**
     * Przesuwanie warstwy tła.
     * Przesuwa poszczególne warstwy w lewo, jeżeli część warstwy jest wystarczająco daleko za lewą krawędzią okna,
     * przesuwa ją za prawą krawędź.
     * @param delta prędkość przesuwania
     */
    public void update(float delta) {
        transform1.pos.x += delta;
        if (transform1.pos.x <= -width * 2) {
            transform1.pos.x = transform3.pos.x + width * 2 - 1;
        }
        transform2.pos.x += delta;
        if (transform2.pos.x <= -width * 2) {
            transform2.pos.x = transform1.pos.x + width * 2 - 1;
        }
        transform3.pos.x += delta;
        if (transform3.pos.x <= -width * 2) {
            transform3.pos.x = transform2.pos.x + width * 2 - 1;
        }

        if (transform1.pos.x >= width * 4) {
            transform1.pos.x = transform2.pos.x - width * 2 + 1;
        }

        if (transform2.pos.x >= width * 4) {
            transform2.pos.x = transform3.pos.x - width * 2 + 1;
        }

        if (transform3.pos.x >= width * 4) {
            transform3.pos.x = transform1.pos.x - width * 2 + 1;
        }


    }

    /**
     * Renderowanie warstwy
     * @param world świat gry
     * @param shader shader
     * @param window okno gry
     */
    public void render(World world, Shader shader, Window window) {
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform1.getProjection(target));
        texture.bind(0);
        Assets.getModel().render();

        target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform2.getProjection(target));
        texture.bind(0);
        Assets.getModel().render();

        target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform3.getProjection(target));
        texture.bind(0);
        Assets.getModel().render();
    }



}
