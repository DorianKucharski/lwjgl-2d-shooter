package gra.render;

import gra.io.Timer;

import java.io.Serializable;

/**
 * Animacje
 */
public class Animation implements Serializable {
	/**
	 * Tablica tekstur - klatki animacji
	 */
	private Texture[] frames;
	/**
	 * Aktualna tekstura
	 */
	private int texturePointer;

	/**
	 * Czas który upłynął od ostatniej zmiany klatki
	 */
	private double elapsedTime;
	/**
	 * Aktualny czas
	 */
	private double currentTime;
	/**
	 * Czas ostatniej zmiany klatki animacji
	 */
	private double lastTime;
	/**
	 * Liczba klatek na sekundę
	 */
	private double fps;

	/**
	 * Konstruktor. Inicjalizuje pola, ustawia aktualny czas oraz ilość klatek na sekundę. Inicjalizuje tablicę klatek,
	 * do której wczytuje tekstury klatek animacji z folderu podanego jako parametr.
	 * @param amount liczba klatek animacji
	 * @param fps liczba klatek na sekundę
	 * @param filename nazwa folderu z teksturami animacji
	 */
	public Animation(int amount, int fps, String filename) {
		this.texturePointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0 / fps;
		
		this.frames = new Texture[amount];
		for (int i = 0; i < amount; i++) {
			this.frames[i] = new Texture("/textures/" + filename + "/" + i + ".png");
		}
	}

	/**
	 * Wiązanie tekstury animacji do samplera
	 */
	public void bind() {
		bind(0);
	}

	/**
	 * Wiązania klatki animacji do samplera. Odpowiada także za zmianę klatki animacji na podstawie czasu który upłynął
	 * od ostatniej zmiany i ustawienie czasów.
	 * @param sampler id samplera
	 */
	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;
		
		if (elapsedTime >= fps) {
			elapsedTime = 0;
			texturePointer++;
		}
		
		if (texturePointer >= frames.length) texturePointer = 0;
		
		this.lastTime = currentTime;
		
		frames[texturePointer].bind(sampler);
	}
}
