package gra.entity;

import gra.collision.Collision;
import gra.game.Main;
import gra.io.Audio;
import gra.io.Window;
import gra.render.Animation;
import gra.render.Camera;
import gra.world.World;
import org.joml.Math;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Klasa jednostki - przeciwnik
 */
public class Enemy extends Entity {
    /**
     * Stała animacji domyślnej
     */
    public static final int ANIM_IDLE = 0;
    /**
     * Rozmiar tablicy animacji
     */
    public static final int ANIM_SIZE = 1;


    /**
     * Konstruktor
     * @param transform polozenie jednostki i jej skala
     */
    public Enemy(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(1, 1, "enemies/enemy/idle"));
        type = Type.Enemy;
        direction = -1;
        attackCooldown = 500;
        wallMaxSteps = 0;
        jumpsHigh = 0;
        TEXT_ARRAY = new String[]{"random text 1", "random text 2", "random text 3"};

        Runnable walkingRunnable = new Runnable() {
            public void run() {
                if (isOnEdge || isOnWall) {
                    direction *= -1;
                }
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(walkingRunnable, 0, 500, TimeUnit.MILLISECONDS);

    }

    /**
     * Metoda aktualizująca stan jednostki. Zmienia położenie jednostki, obraca ją w kierunku w którym się porusza,
     * aktualizuje stan zegara ataku, wywołuje metodę attack(), odpowiada również za pojawienie się krwi,
     * po pewnym czasie od uśmiercenia jednostki.
     * @param delta delta aktualizacji. 1 / obecna ilosć klatek na sekudnę
     * @param window okno gry
     * @param camera kamery gra
     * @param world świat gry
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        super.update(delta, window, camera, world);
        useAnimation(ANIM_IDLE);
        movement = new Vector2f();

        if (isAlive) {
            movement.x = direction * delta * 5;
            attackTimer += delta * 100;
            if (direction > 0) {
                flip = 0f;
            } else {
                flip = 3.14f;
            }

            if (attackTimer > attackCooldown) {
                attackTimer = 0;
                attack();
            }
        } else {
            if (timeAfterDeath < 0.5) {
                timeAfterDeath += delta;
            } else {
                if (!isBleeding) {
                    if (Main.gore) {
                        Random random = new Random();
                        Transform transformCopy = transform.copy();
                        transformCopy.scale.x = random.nextFloat() * 0.5f;
                        transformCopy.scale.y = random.nextFloat() * 0.5f;
                        Main.getWorld().addEntity(new Blood(transformCopy));
                        scheduledExecutorService.shutdownNow();

                    }
                    isBleeding = true;
                    makeBubble("ALA, RZECZYWISCIE. JAK MNIE WSZYSTKO BOLI", 100);
                }
            }

        }

        move(movement);
    }

    /**
     * Metoda obsługująca otrzymane obrażenia. Po otrzymaniu obrażeń pole isAlive zmienia wartość na false.
     * Odtwarzany jest dźwięk oraz do pola deltaX dodawana jest wartość, która przesuwa jednostkę po śmierci.
     * @param collision obiekt kolizji
     */
    public void damage(Collision collision) {
        if (isAlive) {
            Audio.playAudio("audio/monsters/scream.wav");
            bounding_box.half_extent = new Vector2f(2, 0);
            isAlive = false;
            if (Main.getWorld().player.transform.pos.x < transform.pos.x) {
                deltaX += 2;
            } else {
                deltaX -= 2;
            }
            deltaY += 2;
        }
    }

    /**
     * Metoda odpowiadająca za atak
     * Tworzy obiekt jednostki bullet, który porusza się w kierunku gracza.
     * Oddtwarza dźwięk strzału
     */
    public void attack() {
        Transform transformCopy = transform.copy();
        double x = Main.getWorld().player.transform.pos.x - transform.pos.x;
        double y = Main.getWorld().player.transform.pos.y - transform.pos.y;
        if (Math.abs(x) < 20 && Math.abs(y) < 20) {
            if (x < 0){
                direction = -1;
            } else {
                direction = 1;
            }
            double angle = Math.atan2(x, -y);

            transformCopy.pos.y += Math.sin(angle - 1.57);
            transformCopy.pos.x += Math.cos(angle - 1.57);

            Bullet bullet = new Bullet(transformCopy, angle);
            bullet.friendlyFire = true;
            Main.getWorld().addEntity(bullet);
            Audio.playAudio("audio/guns/9mm.wav");
        }
    }


}


