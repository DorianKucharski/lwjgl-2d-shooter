package gra.entity;

import gra.render.Assets;
import gra.collision.AABB;
import gra.io.Window;
import gra.render.Animation;
import gra.render.Camera;
import gra.render.Shader;
import gra.world.World;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Klasa jednostki - pocisk
 */
public class Bullet extends Entity {

    /**
     * Polozenie jednostki na osi x
     */
    float x;
    /**
     * Polozenie jednostki na osi y
     */
    float y;
    /**
     * Kierunek w ktorum porusza sie jednostka wyrazony w radianach.
     */
    double angle;

    /**
     * Konstruktor
     * @param transform polozenie jednostki
     * @param angle kierunek w ktorym ma poruszac sie jednostka
     */
    public Bullet(Transform transform, double angle) {
        super(1, transform);
        this.angle = angle;
        setAnimation(0, new Animation(1, 1, "bullet/idle"));
        y = (float) Math.sin(angle - 1.57);
        x = (float) Math.cos(angle - 1.57);
        type = Type.Bullet;
        transform.scale.x = 0.5f;
        transform.scale.y = 0.5f;
        bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(0.1f, 0.1f));
        rotation = (float) angle - 1.57f;

    }

    /**
     * Metoda poruszajaca jednostke
     * @param direction dwuwymiarowy wektor ruchu w jakim poruszac się ma jednostka
     */
    @Override
    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
    }

    /**
     * Metoda aktualizujaca stan jednostki
     * @param delta delta aktualizacji. 1 / obecna ilosć klatek na sekudnę
     * @param window okno gry
     * @param camera kamery gra
     * @param world świat gry
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();
        movement.add(x, y);
        move(movement);
    }

    /**
     * Metoda renderująca jednostkę
     * @param shader shader
     * @param camera kamera
     * @param world świat
     */
    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target).rotate(rotation, 0, 0, 1));
        animations[use_animation].bind(0);
        Assets.getModel().render();

    }


}
