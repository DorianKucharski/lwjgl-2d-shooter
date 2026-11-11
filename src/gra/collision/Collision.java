package gra.collision;

import org.joml.Vector2f;

/**
 * Klasa kolizji
 */
public class Collision {
	/**
	 * Wektor wyrazajacy odleglosc miedzy brylami
	 */
	public Vector2f distance;
	/**
	 * Czy bryły zachodzą na siebie
	 */
	public boolean isIntersecting;

	/**
	 * Konstruktor obiektu kolizji
	 * @param distance Wektor dwuwymiarowy odzwierciedlający odległość pomiędzy bryłami
	 * @param intersects Czy btyły zachodzą na siebie
	 */
	public Collision(Vector2f distance, boolean intersects) {
		this.distance = distance;
		this.isIntersecting = intersects;
	}
}
