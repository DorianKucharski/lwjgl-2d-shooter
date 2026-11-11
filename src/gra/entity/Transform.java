package gra.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;

/**
 * Klasa Transform
 * Odpowiada za położenie jednostki i jej skalę
 */
public class Transform implements Serializable {
    /**
     * Trójwymiarowy wektor określający położenie jednostki.
     */
    public Vector3f pos;
    /**
     * Trójwymiarowy wektor określający skalę jednostki
     */
    public Vector3f scale;

    /**
     * Konstruktor bezparametrowy
     */
    public Transform() {
        pos = new Vector3f();
        scale = new Vector3f(1, 1, 1);
    }

    /**
     * Konstruktor
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     * @param width szerokosc
     * @param height wysokosc
     */
    public Transform(float x, float y, float width, float height) {
        pos = new Vector3f(x, y, 0);
        scale = new Vector3f(width, height, 1);
    }

    /**
     * Metoda rzucająca projekcję obiektu na macierz. Dokonuje transcalcji i skalowania macierzy na podstawie pozycji i skali obiektu.
     * @param target macierz
     * @return macierz
     */
    public Matrix4f getProjection(Matrix4f target) {
        target.translate(pos);
        target.scale(scale);
        return target;
    }

    /**
     * Metoda kopiująca obiekt
     * @return kopia obiektu
     */
    public Transform copy() {
        Transform transform = new Transform();
        transform.scale.x = this.scale.x;
        transform.scale.y = this.scale.y;
        transform.scale.z = this.scale.z;
        transform.pos.x = this.pos.x;
        transform.pos.y = this.pos.y;
        transform.pos.z = this.pos.z;
        return transform;
    }

}
