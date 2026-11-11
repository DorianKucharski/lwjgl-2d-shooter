package gra.render;

import org.joml.Matrix4f;

/**
 * Arkusz spritów
 */
public class SpriteSheet {
	/**
	 * Tekstura arkusza
	 */
	private Texture texture;
	/**
	 * Macierz skali
	 */
	private Matrix4f scale;
	/**
	 * Macierz translacji
	 */
	private Matrix4f translation;
	/**
	 * Liczba spriteów w arkuszu
	 */
	public int amountOfTiles;

	/**
	 * Konstruktor. Wczytuje teksturę, inicjalizuje teksturę, inicjalizuje skalę na podstawie ilości spriteów w arkuszu.
	 * @param texture tekstura pliku z arkuszem
	 * @param amountOfTiles liczba spriteów
	 */
	public SpriteSheet(String texture, int amountOfTiles) {
		this.texture = new Texture("/textures/sheets/" + texture);
		
		scale = new Matrix4f().scale(1.0f / (float)amountOfTiles);
		translation = new Matrix4f();
		
		this.amountOfTiles = amountOfTiles;
	}

	/**
	 * Wiąże sprite'a z arkusza z shaderem.
	 * @param shader shader
	 * @param x położenie sprite w arkuszu na osi X
	 * @param y położenie sprite w arkuszu na osi Y
	 */
	public void bindTile(Shader shader, int x, int y) {
		scale.translate(x, y, 0, translation);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("texModifier", translation);
		texture.bind(0);
	}
	

}
