package gra.collision;

import org.joml.Vector2f;
import java.io.Serializable;

/**
 * Axis-aligned bounding box klasa bryly obiektow
 */

public class AABB implements Serializable {
	/**
	 * Wektor dwuwymiarowy wskazujący środek obiektu
	 */
	public Vector2f center;
	/**
	 * Wektor dwuwymiarowy wyrażający rozmiar bryły od jej środka
	 */
	public Vector2f	half_extent;

	/**
	 * Publiczny konstruktor bryły
	 * @param center Wektor dwuwymiarowy wskazujący środek obiektu
	 * @param half_extent Wektor dwuwymiarowy wyrażający rozmiar bryły od jej środka
	 */
	public AABB(Vector2f center, Vector2f half_extent) {
		this.center = center;
		this.half_extent = half_extent;
	}

	/**
	 * Metoda zwracajaca kolizje w postaci obiektu Collision
	 * @param box2 Bryła z którym następuje kolizja
	 * @return Obiekt kolizji
	 */
	public Collision getCollision(AABB box2) {
		Vector2f distance = box2.center.sub(center, new Vector2f());
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);
		distance.sub(half_extent.add(box2.half_extent, new Vector2f()));
		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	/**
	 * Metoda zwracajaca kolizje w postaci obiektu Collision
	 * @param point Wektor dwuwymiarowy opisujący kolizje
	 * @return Obiekt kolizji
	 */
	public Collision getCollision(Vector2f point) {
		Vector2f distance = point.sub(center);
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);
		distance.sub(half_extent);
		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	/**
	 * Metoda korygująca pozycje bryły
	 * @param box2 Bryła z którą następuje kolizja
	 * @param data Obiekt kolizji
	 */
	public void correctPosition(AABB box2, Collision data) {
		Vector2f correctionDistance = box2.center.sub(center, new Vector2f());
		if (data.distance.x > data.distance.y) {
			if (correctionDistance.x > 0) {
				center.add(data.distance.x, 0);
			}
			else {
				center.add(-data.distance.x, 0);
			}
		}
		else {
			if (correctionDistance.y > 0) {
				center.add(0, data.distance.y);
			}
			else {
				center.add(0, -data.distance.y);
			}
		}
	}

	/**
	 * Metoda zwracajaca środek bryły
	 * @return środek bryły
	 */
	public Vector2f getCenter() {
		return center;
	}

	/**
	 * Metoda zwracająca wektor wyrażający rozmiar od środka bryły do jej krawędzi
	 * @return
	 */
	public Vector2f getHalfExtent() {
		return half_extent;
	}

	/**
	 * Metoda kopiująca obiekt
	 * @return kopia obiektu
	 */
	public AABB copy(){
		Vector2f center = new Vector2f();
		Vector2f half_extent = new Vector2f();
		center.x = this.center.x;
		center.y = this.center.y;
		half_extent.x = this.half_extent.x;
		half_extent.y = this.half_extent.y;
		return new AABB(center, half_extent);
	}
}
