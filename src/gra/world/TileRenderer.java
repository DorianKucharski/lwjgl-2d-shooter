package gra.world;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import gra.render.*;

/**
 * Klasa TileRenderer
 */
public class TileRenderer {
    /**
     * Mapa Tekstur
     */
    private HashMap<String, Texture> tileTextures;
    /**
     * Model kafelka
     */
    private Model tileModel;

	/**
	 * Konstruktor
	 * Inicjalizuje mapę kafelków oraz model kafelka, a także dodaje kafelki do mapy wraz z plikami ich tekstur.
	 */
	public TileRenderer() {
        tileTextures = new HashMap<>();
		tileModel = Assets.getModel();

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tileTextures.put(tex, new Texture("/textures/" + tex + ".png"));
                }
            }
        }
    }

	/**
	 * Renderowanie kafelka
	 * @param tile kafelek
	 * @param x położenie kafelka na osi X
	 * @param y położenie kafelka na osi Y
	 * @param shader główny shader
	 * @param world świat gry
	 * @param cam kamera gry
	 */
    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera cam) {
        shader.bind();
        if (tileTextures.containsKey(tile.getTexture())) tileTextures.get(tile.getTexture()).bind(0);

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        tileModel.render();
    }
}
