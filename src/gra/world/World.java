package gra.world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import gra.game.Main;
import org.joml.*;

import gra.collision.AABB;
import gra.entity.*;
import gra.io.Window;
import gra.render.Camera;
import gra.render.Shader;

/**
 * Klasa świata
 */
public class World {
    /**
     * Przeskalowana szerokość świata
     */
    private int viewX;
    /**
     * Przeskalowana wysokość świata
     */
    private int viewY;
    /**
     * Kafelki świata
     */
    private byte[] tiles;
    /**
     * Bryły kolizji kafelków świata
     */
    private AABB[] bounding_boxes;
    /**
     * Lista jednostek świata
     */
    private ArrayList<Entity> entities;
    /**
     * Lista jednostek do dodania
     */
    private ArrayList<Entity> entitiesToAdd;
    /**
     * Lista jednostek do usunięcia
     */
    private ArrayList<Entity> entitiesToDelete;
    /**
     * Szerokość świata
     */
    private int width;
    /**
     * Wysokość świata
     */
    private int height;
    /**
     * Skala świata
     */
    private int scale;
    /**
     * Jednostka gracza
     */
    public Player player;
    /**
     * Macierz świata
     */
    private Matrix4f world;
    /**
     * Tło świata
     */
    Background background;

    /**
     * Konstruktor
     * @param world nazwa pliku mapy świata
     * @param camera kamera gry
     */
    public World(String world, Camera camera) {
        try {
            BufferedImage entity_sheet = ImageIO.read(new File("./levels/" + world + "/map.png"));

            width = entity_sheet.getWidth();
            height = entity_sheet.getHeight();
            scale = 32;

            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);

            int[] colorEntitySheet = entity_sheet.getRGB(0, 0, width, height, null, 0, width);

            tiles = new byte[width * height];
            bounding_boxes = new AABB[width * height];
            entities = new ArrayList<>();
            entitiesToAdd = new ArrayList<>();
            entitiesToDelete = new ArrayList<>();

            Transform transform;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int blue = colorEntitySheet[x + y * width] & 0xff;
                    int green = (colorEntitySheet[x + y * width] & 0xff00) >> 8;
                    int red = (colorEntitySheet[x + y * width] & 0xff0000) >> 16;

                    if (red == 0 && green == 0 && blue == 255) {
                        transform = new Transform();
                        transform.pos.x = x * 2;
                        transform.pos.y = -y * 2;
                        player = new Player(transform);
                        entities.add(player);
                        camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));
                        Tile t = getTile(x-1, y);
                        setTile(t, x, y);
                    } else if (red == 255 && green == 0 && blue == 0) {
                        transform = new Transform();
                        transform.pos.x = x * 2;
                        transform.pos.y = -y * 2;
                        entities.add(new Enemy(transform));
                        Tile t = getTile(x-1, y);
                        setTile(t, x, y);
                    } else if (red == 0 && green == 0 && blue == 0) {
                        Tile t = Tile.ground;
                        setTile(t, x, y);
                    } else if (red == 64 && green == 102 && blue == 24) {
                        Tile t = Tile.roof1;
                        setTile(t, x, y);
                    } else if (red == 0 && green == 94 && blue == 32) {
                        Tile t = Tile.roof2;
                        setTile(t, x, y);
                    } else if (red == 0 && green == 88 && blue == 38) {
                        Tile t = Tile.roof3;
                        setTile(t, x, y);
                    } else if (red == 46 && green == 49 && blue == 146) {
                        Tile t = Tile.wall1;
                        setTile(t, x, y);
                    } else if (red == 102 && green == 45 && blue == 145) {
                        Tile t = Tile.wall2;
                        setTile(t, x, y);
                    } else if (red == 146 && green == 39 && blue == 143) {
                        Tile t = Tile.wall3;
                        setTile(t, x, y);
                    } else if (red == 236 && green == 0 && blue == 140) {
                        Tile t = Tile.wall4;
                        setTile(t, x, y);
                    }
                }
            }

            background = new Background(this, Main.getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Obliczanie przeskalowanej wysokości i szerokości świata na podstawie okna
     * @param window okno gry
     */
    public void calculateView(Window window) {
        viewX = (window.getWidth() / (scale * 2)) + 4;
        viewY = (window.getHeight() / (scale * 2)) + 4;
    }

    /**
     * Getter macierzy świata
     * @return macierz świata
     */
    public Matrix4f getWorldMatrix() {
        return world;
    }

    /**
     * Renderowanie świata
     * Obcina obszar świata, następnie renderuje tło świata, kafelki świata i jednostki świata.
     * @param render obiekt klasy TileRenderer, wykonuje renderowanie kafelków świata
     * @param shader główny shader gry
     * @param cam kamera gry
     * @param window okno gry
     */
    public void render(TileRenderer render, Shader shader, Camera cam, Window window) {
        int posX = (int) cam.getPosition().x / (scale * 2);
        int posY = (int) cam.getPosition().y / (scale * 2);

        background.render(this, shader, window);

        for (int i = 0; i < viewX; i++) {
            for (int j = 0; j < viewY; j++) {
                Tile t = getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
                if (t != null && t.isVisible())
                    render.renderTile(t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2), shader, world, cam);
            }
        }

        for (Entity entity : entities) {
            if (entity.isOnScreen(cam, window)) {
                entity.render(shader, cam, this);
            }

        }


    }

    /**
     * Aktualizacja stanu świata.
     * Dodaje jednostki, które mają być dodane do listy jednostek, usuwa jednostki znajdujące się w liście jednostek
     * do usunięcia, następnie czyści te dwie pomocnicze listy, po czym aktualizuje stan jednostek, które znajdują się
     * w zakresie obciętego obszaru świata, jeżeli gracz się porusza - przesuwa tło świata, na koniec wywołuje metody
     * odpowiedzialne za obsługę kolizji jednostek z jednostkami i jednostek z kafelkami świata.
     * @param delta delta czasu
     * @param window okno gry
     * @param camera kamera gry
     */
    public void update(float delta, Window window, Camera camera) {
        entities.addAll(entitiesToAdd);
        entities.removeAll(entitiesToDelete);
        entitiesToDelete.clear();
        entitiesToAdd.clear();

        for (Entity entity : entities) {
            if (entity.isOnScreen(camera, window)) {
                entity.update(delta, window, camera, this);
            } else if (entity.type == Entity.Type.Bullet) {
                deleteEntity(entity);
            }

        }

        if ((player.getTransform().pos.x + 1) * 32 > (float) window.getWidth() / 2) {
            background.update(player.getMovement().x * delta);
        }

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).collideWithTiles(this);
            for (int j = i + 1; j < entities.size(); j++) {
                entities.get(i).collideWithEntity(entities.get(j));
            }
            entities.get(i).collideWithTiles(this);

        }


    }

    /**
     * Przesuwanie kamery
     * @param camera kamera gry
     * @param window okno gry
     */
    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth() / 2) + scale) pos.x = -(window.getWidth() / 2) + scale;
        if (pos.x < w + (window.getWidth() / 2) + scale) pos.x = w + (window.getWidth() / 2) + scale;

        if (pos.y < (window.getHeight() / 2) - scale) pos.y = (window.getHeight() / 2) - scale;
        if (pos.y > h - (window.getHeight() / 2) - scale) pos.y = h - (window.getHeight() / 2) - scale;

    }

    /**
     * Ustawia kafelek świata gry, nadaje mu bryłę kolizji jeśli kafelek ma atrybut solid.
     * @param tile kafelek
     * @param x położenie kafelka na osi X
     * @param y położenie kafelka na osi Y
     */
    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            bounding_boxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
        } else {
            bounding_boxes[x + y * width] = null;
        }
    }

    /**
     * Zwraca kafelek na podstawie podanego położenia
     * @param x położenie kafelka na osi X
     * @param y położenie kafelka na osi Y
     * @return kafelek na danej pozycji
     */
    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Zwraca bryłę kolizji kafelka świata na zadanym położeniu
     * @param x położenie kafelka na osi X
     * @param y położenie kafelka na osi Y
     * @return bryła obiektu jeśli kafelek ją posiada, w innym przypadku zwraca null
     */
    public AABB getTileBoundingBox(int x, int y) {
        try {
            return bounding_boxes[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Getter skali świata
     * @return skala świata
     */
    public int getScale() {
        return scale;
    }

    /**
     * Getter szerokości świata
     * @return szerokość świata
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter wysokości świata
     * @return wysokość świata
     */
    public int getHeight() {
        return height;
    }

    /**
     * Dodaje jednostę do listy jednostek do doania.
     * @param entity jednostka
     */
    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    /**
     * Dodaje jednostkę do listy jednostek do usunięcia.
     * @param entity jednostka
     */
    public void deleteEntity(Entity entity) {
        entitiesToDelete.add(entity);
    }

    /**
     * Czyszczenie listy jednostek świata
     */
    public void clearEntities() {
        entities.clear();
    }

    /**
     * Getter listy jednostek świata
     * @return lista jednostek
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Liczenie liczby żywych przeciwników
     * @return liczba przeciwników
     */
    public int countEnemies() {
        int i = 0;
        for (Entity entity : entities) {
            if (entity.type == Entity.Type.Enemy && entity.getIsAlive()) {
                i++;
            }
        }
        return i;
    }

    /**
     * Wyłączenie executorservice wszystkich jednostek świata
     */
    public void shutdown() {
        for (Entity entity : entities) {
            entity.shutdown();
        }
    }

}
