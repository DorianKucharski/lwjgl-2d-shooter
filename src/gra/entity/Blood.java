package gra.entity;
import gra.render.Animation;
import gra.render.Camera;
import gra.io.Window;
import gra.world.World;

/**
 * Klasa jednostki - krew
 */

public class Blood extends Entity {

    /**
     * Stała animacji nr 1
     */
    private static final int ANIM_1 = 0;
    /**
     * Ilość animacji
     */
    private static final int ANIM_SIZE = 1;

    /**
     * Konstruktor
     * @param transform obiekt transform okreslajacy polozenie i skale jednostki
     */
    public Blood (Transform transform) {
        super(ANIM_SIZE, transform);
        type  = Type.Blood;
        setAnimation(ANIM_1, new Animation(1, 1, "blood/1"));
        isAlive = false;
    }

    /**
     * Aktualizacja stanu jednostki
     * @param delta delta aktualizacji. 1 / obecna ilosć klatek na sekudnę
     * @param window okno gry
     * @param camera kamery gra
     * @param world świat gry
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        useAnimation(ANIM_1);
        if (transform.scale.x < 4){
            transform.scale.x += 0.01;
        }
    }



}
