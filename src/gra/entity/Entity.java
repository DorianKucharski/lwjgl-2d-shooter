package gra.entity;

import gra.game.Main;
import org.joml.*;

import gra.render.Assets;
import gra.collision.AABB;
import gra.collision.Collision;
import gra.io.Window;
import gra.render.*;
import gra.world.World;
import java.io.Serializable;
import java.lang.Math;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Klasa bazowa wszystkich jednostek gry
 */
public class Entity implements Serializable {

    /**
     * Typ wyliczeniowy, okreslajacy typ jednostki
     */
    public enum Type {
        Player, Enemy, Bullet, Blood
    }

    /**
     * Typ jednostki
     */
    public Type type;
    /**
     * Bryła jednostki
     */
    protected AABB bounding_box;
    /**
     * Tablica animacji
     */
    transient protected Animation[] animations;
    /**
     * Wskazuje na to która animacja jest używana obecnie
     */
    transient protected int use_animation;
    /**
     * Obiekt Transform określający położenie jednostki i jej rozmiar
     */
    Transform transform;
    /**
     * Obrot jednostki wokół osi Z, wyrażony w radianach
     */
    float rotation;
    /**
     * Wektor dwuwymiarowy wyrażający chwilowy ruch jednostki
     */
    Vector2f movement;

    /**
     * Czy jednostka jest na krawedzi
     */
    boolean isOnEdge;
    /**
     * Czy jednostka jest na ziemi
     */
    boolean isOnGround;
    /**
     * Czy jednostka jest na ścianie
     */
    boolean isOnWall;
    /**
     * Czy jednostka krwawi
     */
    boolean isBleeding;
    /**
     * Czy jednostka obecnie wchodzi w kolizje
     */
    boolean isColliding;

    /**
     * Ruch jaki ma narzucony jednostka w osi X wykonywany w nastepnych klatkach animacji
     */
    double deltaX;
    /**
     * Ruch jaki ma narzucony jednostka w osi Y wykonywany w nastepnych klatkach animacji
     */
    double deltaY;
    /**
     * Kierunek w ktorym porusza się jednostka przeciwnika
     */
    int direction;
    /**
     * Obecna sila grawitacji
     */
    double gravityCurrent;
    /**
     * Domyśla siła graitacji
     */
    double gravityDefault = 0.2;

    /**
     * Ilosc wykonanych w powietrzu skokow
     */
    int jumpsAmount;

    /**
     * Maksymalna ilosc skokow
     */
    int jumpsMaxAmount = 2;
    /**
     * Wysokosc skoku
     */
    int jumpsHigh = 5;

    /**
     * Ilosc wykonanych krokow po scianie
     */
    int wallSteps;
    /**
     * Maksymalna ilosc krokow po scianie
     */
    int wallMaxSteps = 1;
    /**
     * Po ktorej stronie jest sciana. 1 - po prawej, - 1 - po lewej
     */
    int wallOnWhichSide;

    /**
     * Czas od ostatniego ataku
     */
    int attackTimer;
    /**
     * Czas przerwy pomiedzy atakami
     */
    int attackCooldown;
    /**
     * Czy przeciwnicy moga do sobie nawzajem zadawać obrażenia
     */
    boolean friendlyFire;
    /**
     * Predkosc poruszania sie
     */
    float speed = 1;
    /**
     * Czas wyswietlania dymku z tekstem pozostaly do jego znikniecia
     */
    float bubbleTime;
    /**
     * Tekst wyswietlany w dymku
     */
    String bubbleText;
    /**
     * Czy jednostka żyje
     */
    boolean isAlive;
    /**
     * Czas który upłynął od zabicia jednostki
     */
    float timeAfterDeath;
    /**
     * Nieśmiertelność
     */
    boolean godMode;

    /**
     * Obrót w osi Y wyrażany w radianach
     */
    float flip;
    /**
     * Obrót w osi Z wyrażany w radianach, obpowiedzialny za pochylenie się jednostki
     */
    float tilt;
    /**
     * Czy jednostka jest widoczna
     */
    boolean visible;
    /**
     * Random generator
     */
    Random random;
    /**
     * Tablica kwesti wypowiadanych przez jednostkę
     */
    String[] TEXT_ARRAY;
    /**
     * ScheduledExecutorService
     */
    transient ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    /**
     * Runnable odpowiedzialne za losowe dymki z tekstem
     */
    transient Runnable bubbleRuntime = new Runnable() {
        public void run() {
            if (isAlive && random.nextBoolean() && TEXT_ARRAY.length > 0) {
                makeBubble(TEXT_ARRAY[new Random().nextInt(TEXT_ARRAY.length)], 100);
            }
        }
    };

    /**
     * Runnable odpowiedzialne za wlaczenie i wylaczenie godmode po otrzymaniu obrażeń
     */
    transient Runnable godModeRuntime = new Runnable() {
        public void run() {
            godMode = true;

            for (int i = 0; i < 30; i++) {
                visible = !visible;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
            }

            godMode = false;
        }
    };

    /**
     * Publiczny konstruktor jednostki
     * @param max_animations Maksymalna ilosc animacji
     * @param transform Obiekt Transform określający położenie jednostki i jej skalę
     */
    public Entity(int max_animations, Transform transform) {
        this.animations = new Animation[max_animations];
        this.transform = transform;
        this.use_animation = 0;
        bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));
        isAlive = true;
        visible = true;
        random = new Random();
        scheduledExecutorService.scheduleAtFixedRate(bubbleRuntime, random.nextInt(10000)+5000, random.nextInt(10000)+5000, TimeUnit.MILLISECONDS);
    }

    /**
     * Ustawia animacje na tablicy animacji
     * @param index indeks w tablicy
     * @param animation animacja
     */
    protected void setAnimation(int index, Animation animation) {
        animations[index] = animation;
    }

    /**
     * Ustawia używaną obecnie animacje
     * @param index indeks w tablicy animacji
     */
    public void useAnimation(int index) {
        this.use_animation = index;
    }

    /**
     * Metoda obslugujaca otrzymanie obrażen przez jednostkę
     * @param collision obiekt kolizji
     */
    public void damage(Collision collision) {
        Main.getWorld().deleteEntity(this);
    }

    /**
     * Metoda tworzaca dymek z tekstem
     * @param text tekst do wyświetlenia
     * @param time czas wyświetlania w millisekundach
     */
    public void makeBubble(String text, float time) {
        bubbleText = text;
        bubbleTime = time;
    }

    /**
     * Poruszanie sie jednostki. Metoda odpowiada za poruszanie się, skakanie,
     * wchodzenie po ścianach, oraz zsuwanie się po nich
     * @param direction dwuwymiarowy wektor ruchu w jakim poruszac się ma jednostka
     */
    public void move(Vector2f direction) {
        if (deltaY > 5){
            deltaY = 5;
        }
        if (isOnWall && ((direction.x > 0 && wallOnWhichSide > 0) || direction.x < 0 && wallOnWhichSide < 0)) {
            if (wallSteps < wallMaxSteps) {
                deltaY += jumpsHigh - deltaY;
                wallSteps++;
            }
            if (deltaY > 0) {
                if (wallOnWhichSide < 0) {
                    rotation = -1.1f;
                } else {
                    rotation = 1.1f;
                }
            }
        }

        if (rotation > 0)
            rotation -= 0.2;
        if (rotation < 0)
            rotation += 0.2;
        if (Math.abs(rotation) < 0.2)
            rotation = 0;

        direction.y += (deltaY / 10);
        direction.x += (deltaX / 10);

        if (deltaY > 0.1) {
            deltaY -= gravityCurrent / 2;
        } else if (deltaY < -0.1) {
            deltaY += gravityCurrent / 2;
        } else {
            deltaY = 0;
        }

        if (deltaX > 0.1) {
            deltaX -= gravityCurrent / 2;
        } else if (deltaX < -0.1) {
            deltaX += gravityCurrent / 2;
        } else {
            deltaX = 0;
        }


        if (isOnGround || isOnWall) {
            gravityCurrent = gravityDefault;
            jumpsAmount = 0;
        } else if (deltaY == 0) {
            gravityCurrent += gravityCurrent * 0.03;
        }

        if (isOnGround) {
            wallSteps = 0;
        }

        if (!isOnGround && !isOnWall) {
            wallSteps = 0;
        }


        direction.y -= gravityCurrent;
        direction.x = direction.x * speed;
        speed = 1;
        transform.pos.add(new Vector3f(direction, 0));
        bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
    }


    /**
     * Metoda inicjująca skakanie, nadaje rotacje w przypadku podwójnego skoku, pozwala na odbijanie się od ścian
     * @param direction wketor wyraążający obecny ruch postaci
     */
    public void jump(Vector2f direction) {
        jumpsAmount++;
        if (isOnGround) {
            deltaY += jumpsHigh;

        } else if (jumpsAmount < jumpsMaxAmount) {
            if (isOnWall) {
                if (wallOnWhichSide > 0) {
                    deltaX -= 3 - direction.x;
                } else {
                    deltaX += 3 - direction.x;
                }
            }
            deltaY += (jumpsHigh - deltaY);
            if (direction.x > 0)
                rotation = 6.28f;
            else
                rotation = -6.28f;
        }
    }


    /**
     * Metoda realizujaca kolizje jednostki z kafelkami świata. Sprawdza relacje jednostki z kafelkami,
     * nie pozwala przejsc jednostce przez kafelki, ustawia takie zmienne jak isOnGround, isOnWall, isOnEdge i wallOnWhichSide
     * @param world świat
     */
    public void collideWithTiles(World world) {
        if (type != Type.Blood) {
            AABB[] boxes = new AABB[25];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    boxes[i + j * 5] = world.getTileBoundingBox((int) (((transform.pos.x / 2) + 0.5f) - (5 / 2)) + i, (int) (((-transform.pos.y / 2) + 0.5f) - (5 / 2)) + j);
                }
            }

            AABB box = null;
            int nearTilesOnEntityLevel = 0;
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (boxes[i].getCenter().y == bounding_box.getCenter().y - 2)
                        nearTilesOnEntityLevel++;
                    if (box == null) box = boxes[i];
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }

            if (0 < nearTilesOnEntityLevel && nearTilesOnEntityLevel < 4) {
                isOnEdge = true;
            } else {
                isOnEdge = false;
            }
            if (box != null) {
                wallOnWhichSide = (int) (box.getCenter().x - transform.pos.x);
                Collision data = bounding_box.getCollision(box);
                isOnWall = Math.abs(data.distance.x) < 0.1 && data.distance.y < -0.1;
                isOnGround = Math.abs(data.distance.y) < 0.1;
                if (data.isIntersecting) {
                    if ((box.getCenter().y - transform.pos.y) > 1) {
                        deltaY = 0;
                    }

                    if (type == Type.Bullet) {
                        damage(data);
                    }
                    if (gravityCurrent > 1 && !isOnWall) {
                        damage(data);
                    }
                    bounding_box.correctPosition(box, data);
                    transform.pos.set(bounding_box.getCenter(), 0);
                }

                for (int i = 0; i < boxes.length; i++) {
                    if (boxes[i] != null) {
                        if (box == null) box = boxes[i];
                        Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                        Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                        if (length1.lengthSquared() > length2.lengthSquared()) {
                            box = boxes[i];
                        }
                    }
                }

                data = bounding_box.getCollision(box);
                if (data.isIntersecting) {
                    bounding_box.correctPosition(box, data);
                    transform.pos.set(bounding_box.getCenter(), 0);
                }
            } else {
                isOnWall = false;
                isOnGround = false;
            }
        }
    }

    /**
     * Metoda realizujaca kolizje jednostki z drugą jednostką. Sprawdza typy obu jednostek i zależnie od ich rodzaju
     * realizuje kolizje w odpowiedni sposób.
     * @param entity druga jednostka
     */
    public void collideWithEntity(Entity entity) {
        if (isAlive && entity.isAlive) {
            Collision collision = bounding_box.getCollision(entity.bounding_box);
            if (collision.isIntersecting) {
                if (type == Type.Bullet && entity.type == Type.Enemy && !friendlyFire) {
                    damage(collision);
                    entity.damage(collision);
                    Main.increasePoints(1000);
                } else if (type == Type.Enemy && entity.type == Type.Bullet && !entity.friendlyFire) {
                    damage(collision);
                    entity.damage(collision);
                    Main.increasePoints(1000);
                } else if (type == Type.Bullet && entity.type == Type.Player && !entity.godMode) {
                    damage(collision);
                    entity.damage(collision);
                } else if (type == Type.Player && entity.type == Type.Bullet && !godMode) {
                    damage(collision);
                    entity.damage(collision);
                } else if (type == Type.Player && entity.type == Type.Enemy) {
                    if (!godMode) {
                        damage(collision);
                    }
                    entity.direction *= -1;
                } else if (type == Type.Enemy && entity.type == Type.Player) {
                    if (!entity.godMode) {
                        entity.damage(collision);
                    }
                    direction *= -1;
                }

//                collision.distance.x /= 2;
//                collision.distance.y /= 2;
//                bounding_box.correctPosition(entity.bounding_box, collision);
//                transform.pos.set(bounding_box.getCenter().x, bounding_box.getCenter().y, 0);
//                entity.bounding_box.correctPosition(bounding_box, collision);
//                entity.transform.pos.set(entity.bounding_
//                box.getCenter().x, entity.bounding_box.getCenter().y, 0);
            } else {
                isColliding = false;
                entity.isColliding = false;
            }
        }
    }

    /**
     * Metoda aktualizujaca stan jednostki.
     * @param delta delta aktualizacji. 1 / obecna ilosć klatek na sekudnę
     * @param window okno gry
     * @param camera kamery gra
     * @param world świat gry
     */
    public void update(float delta, Window window, Camera camera, World world) {
        bubbleTime -= 1;
    }

    /**
     * Renderowanie jednostki
     * @param shader shader
     * @param camera kamera
     * @param world świat
     */
    public void render(Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        if (isAlive) {
            shader.setUniform("projection", transform.getProjection(target).rotate(rotation, 1, 0, 1).rotate(flip, 0, 1, 0));
        } else {
            shader.setUniform("projection", transform.getProjection(target).rotate(-1.57f, 0, 0, 1));
        }

        animations[use_animation].bind(0);
        Assets.getModel().render();

        float x = camera.getPositionOnScreen(transform).x;
        float y = camera.getPositionOnScreen(transform).y + 100;

        if (bubbleTime > 0 && !Main.isPaused) {
            Draw.bubbleWithText(x, y, 8, bubbleText);
        }
    }

    /**
     * Metoda sprawdzająca czy jednostka znajduje się na ekranie
     * @param camera camera
     * @param window okno
     * @return true jeśli jednostka jest na ekranie i false gdy nie jest
     */
    public boolean isOnScreen(Camera camera, Window window) {
        Vector2f position = camera.getPositionOnScreen(transform);
        int margin = 200;
        int leftLine = -window.getWidth() / 2 - margin;
        int rightLine = window.getWidth() / 2 + margin;
        int bottomLine = -window.getHeight() / 2 - margin;
        int topLine = window.getHeight() / 2 + margin;
        return position.x > leftLine && position.x < rightLine && position.y > bottomLine && position.y < topLine;
    }

    /**
     * Metoda kopiująca jednostkę
     * @return kopia jednostki
     */
    public Entity copy() {
        Entity entity = null;
        Transform transformCopy = transform.copy();
        if (this instanceof Blood) {
            entity = new Blood(transformCopy);
        } else if (this instanceof Bullet) {
            entity = new Bullet(transformCopy, ((Bullet) this).angle);
        } else if (this instanceof Enemy) {
            entity = new Enemy(transformCopy);
        } else if (this instanceof Player) {
            entity = new Player(transformCopy);
        }

        if (entity != null) {
            entity.transform = transformCopy;
            entity.bounding_box = bounding_box.copy();
            entity.deltaX = deltaX;
            entity.deltaY = deltaY;
            entity.isAlive = isAlive;
            entity.timeAfterDeath = timeAfterDeath;
        }
        return entity;
    }

    /**
     * Wyłączenie scheduledExecutorService
     */
    public void shutdown(){
        scheduledExecutorService.shutdownNow();
    }

    /**
     * Getter transformu
     * @return Transform
     */

    public Transform getTransform(){
        return transform;
    }

    /**
     * Getter typu jednostki
     * @return Typ jednostki
     */
    public Type getType(){
        return type;
    }

    /**
     * Getter movementu
     * @return movement
     */
    public Vector2f getMovement(){
        return movement;
    }

    /**
     * Getter isAlive
     * @return isAlive
     */
    public boolean getIsAlive(){
        return isAlive;
    }

    /**
     * Getter is GodMode
     * @return godMode
     */
    public boolean isGodMode(){
        return godMode;
    }

    /**
     * Setter godMode
     * @param is godMode
     */
    public void setGodMode(boolean is){
        godMode = is;
    }
}
