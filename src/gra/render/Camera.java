package gra.render;

import gra.entity.Transform;
import gra.game.Main;
import gra.io.Window;
import gra.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Kamera
 */
public class Camera {
    /**
     * Pozycja kamery
     */
    private Vector3f position;
    /**
     * Macierz projekcji kamery
     */
    private Matrix4f projection;

    /**
     * Konstruktor. Inicjalizuje wektor pozycji kamery oraz projektcje kamery.
     * @param width szerokość kamery
     * @param height wysokość kamery
     */
    public Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        setProjection(width, height);
    }

    /**
     * Ustawia projekcje kamery
     * @param width szerokość okna
     * @param height wysokość okna
     */
    public void setProjection(int width, int height) {
        projection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    /**
     * Getter wektora pozycji kamery
     * @return wektor pozycji kamery
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Getter macierzy projekcji kamery
     * @return macierz projekcji
     */
    public Matrix4f getProjection() {
        return projection.translate(position, new Matrix4f());
    }

    /**
     * Oblicza pozycje obiektu transform na ekranie.
     * @param transform transform którego pozycja ma być obliczona
     * @return wektor pozycji
     */
    public Vector2f getPositionOnScreen(Transform transform) {
        Vector2f vector2f = new Vector2f();
        vector2f.x = Math.abs(getPosition().x / 32);
        vector2f.y = Math.abs(getPosition().y / 32);
        vector2f.x = (transform.pos.x - vector2f.x) * Main.getWorld().getScale();

        vector2f.y = (transform.pos.y + vector2f.y) * Main.getWorld().getScale();
        return vector2f;
    }


}
