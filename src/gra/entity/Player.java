package gra.entity;

import gra.render.Assets;
import gra.collision.Collision;
import gra.game.Main;
import gra.io.Audio;
import gra.render.*;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import gra.io.Window;
import gra.world.World;

/**
 * Klasa jednostki - player
 */
public class Player extends Entity  {
    /**
     * Stała animacji domyślnej
     */
    public static final int ANIM_IDLE = 0;
    /**
     * Stała animacji chodzenia
     */
    public static final int ANIM_WALK = 1;
    /**
     * Stała animacji chodzenia tyłem
     */
    public static final int ANIM_WALK_BACKWARD = 2;
    /**
     * Stała rozmiaru tablicy animacji
     */
    public static final int ANIM_SIZE = 3;
    /**
     * Tablica animacji rąk jednostki
     */
    protected Animation[] handsAnimations;
    /**
     * Obiekt Transform rąk jednostki
     */
    protected Transform handsTransform;
    /**
     * Obecnie używana animacja rąk
     */
    private int handsUseAnimation;
    /**
     * Kierunek rąk jednostki
     */
    private float handsRotation;

    /**
     * Konstruktor jednostki
     * @param transform
     */
    public Player(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(1, 1, "player/idle"));
        setAnimation(ANIM_WALK, new Animation(6, 12, "player/walking"));
        setAnimation(ANIM_WALK_BACKWARD, new Animation(2, 4, "player/walking_backward"));

        handsAnimations = new Animation[1];
        handsTransform = transform.copy();
        handsUseAnimation = 0;
        handsAnimations[0] = new Animation(1, 1, "player/hands");
        type = Type.Player;
        TEXT_ARRAY = new String[]{"Cool one-liner 1", "Cool one-liner 2", "Cool one-liner 3", "Cool one-liner 4"};
    }

    /**
     * Metoda aktualizująca stan jednostki. Odpowiada za ruch jednostki, odczyt położenia wskaźnika myszy, kierowanie
     * rąk jednostki w jego kierunku, odczytywanie wciśniętych klawiszy i obracanie jednostki w osi Y, oraz zmianę animacji.
     * @param delta delta aktualizacji. 1 / obecna ilosć klatek na sekudnę
     * @param window okno gry
     * @param camera kamery gra
     * @param world świat gry
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        super.update(delta, window, camera, world);

        movement = new Vector2f();
        float mouseX = window.getInput().getMousePosition().x() / world.getScale();
        float mouseY = window.getInput().getMousePosition().y() / world.getScale();

        if ((transform.pos.x + 1) * 32 < (float) window.getWidth() / 2) {
            mouseX += (int) (window.getWidth() / world.getScale() / 2 - transform.pos.x);
        } else if ((world.getWidth() * 2 - transform.pos.x - 1) * world.getScale() < (float) window.getWidth() / 2) {
            mouseX -= (1.0 * window.getWidth() / world.getScale() / 2) - (world.getWidth() * 2 - transform.pos.x - 1);
        }

        if ((-transform.pos.y + 1) * world.getScale() < 1.0 * window.getHeight() / 2) {
            mouseY -= 1.0 * window.getHeight() / world.getScale() / 2 + transform.pos.y - 1;
        } else if ((world.getHeight() * 2 + transform.pos.y - 1) * world.getScale() < (float) window.getHeight() / 2) {
            mouseY += (1.0 * window.getHeight() / world.getScale() / 2) - (world.getHeight() * 2 + transform.pos.y - 1);
        }

        double angle = Math.atan2(mouseX, -mouseY);

        handsTransform.pos.y = (float) (transform.pos.y + Math.sin(angle - 1.57)) + 0.5f;
        handsTransform.pos.x = (float) (transform.pos.x + Math.cos(angle - 1.57));
        handsRotation = (float) angle;


        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
            movement.add(-10 * delta, 0);

        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
            movement.add(10 * delta, 0);

        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
            movement.add(0,10 * delta);
        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
            movement.add(0,-10 * delta);

        if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            jump(movement);
        }

        if (window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            speed = 2;
        }

        if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_E)) {
            makeBubble("MIEJSCE NA REKLAME", 100);
        }



        if (mouseX > 0) {
            flip = 0f;
        }
        if (mouseX < 0) {
            flip = 3.14f;
        }

        if (window.getInput().isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            attack();
        }

        move(movement);
        if (movement.x != 0) {
            if (mouseX < 0 && movement.x > 0 || mouseX > 0 && movement.x < 0) {
                useAnimation(ANIM_WALK_BACKWARD);
                tilt = 0.5f;
            } else {
                useAnimation(ANIM_WALK);
                tilt = 0f;
            }
        } else {
            useAnimation(ANIM_IDLE);
            tilt = 0f;
        }


        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.10f);
    }

    /**
     * Metoda odpowiedzialna za atak jednostki. Tworzy obiekt klasy Bullet, oddtwarza dźwięk strzału
     */
    public void attack() {
        Transform bulletTransform = handsTransform.copy();
        bulletTransform.pos.y += Math.sin(handsRotation - 1.57);
        bulletTransform.pos.x += Math.cos(handsRotation - 1.57);
        Bullet bullet = new Bullet(bulletTransform, handsRotation);
        Main.getWorld().addEntity(bullet);
        Audio.playAudio("audio/guns/9mm.wav");
    }

    /**
     * Metoda renderowania jednostki oraz jej rąk. Jeżeli pole visible ma wartość false, renderowanie się nie odbywa,
     * używane do uzyskaniu efektu migotającej jednostki po otrzymaniu obrażeń.
     * @param shader shader
     * @param camera kamera
     * @param world świat
     */
    public void render(Shader shader, Camera camera, World world) {
        if (visible) {
            Matrix4f target = camera.getProjection();
            target.mul(world.getWorldMatrix());
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(target)
                    .rotate(rotation, 0, 0, 1)
                    .rotate(flip, 0, 1, 0)
                    .rotate(tilt, 0, 0, 1));
            animations[use_animation].bind(0);
            Assets.getModel().render();

            target = camera.getProjection();
            target.mul(world.getWorldMatrix());
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", handsTransform.getProjection(target).rotate(handsRotation, 0, 0, 1));
            handsAnimations[handsUseAnimation].bind(0);
            Assets.getModel().render();
        }
        float x = camera.getPositionOnScreen(transform).x;
        float y = camera.getPositionOnScreen(transform).y + 100;

        if (bubbleTime > 0 && !Main.isPaused) {
            Draw.bubbleWithText(x, y, 8, bubbleText);
        }

    }

    /**
     * Metoda obsługująca otrzymywane obrażenia
     * @param collision obiekt kolizji
     */
    public void damage(Collision collision){
        Main.decreaseLives(1);
        scheduledExecutorService.submit(godModeRuntime);
    }


}
