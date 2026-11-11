package gra.world;

/**
 * Klasa kafelka
 */
public class Tile {
	/**
	 * Tablica rodzajów kafelków
	 */
	public static Tile[] tiles = new Tile[255];
	/**
	 * Ilość rodzajów kafelków
	 */
	public static byte not = 0;

	/**
	 * Kafelek pusty
	 */
	public static final Tile black = new Tile("/map/black").setVisible(false);
	/**
	 * kafelek ziemi
	 */
	public static final Tile ground = new Tile("/map/ground").setSolid();
	/**
	 * Kafelek ściany
	 */
	public static final Tile wall1 = new Tile("/map/wall1");
	/**
	 * Kafelek ściany
	 */
	public static final Tile wall2 = new Tile("/map/wall2");
	/**
	 * Kafelek ściany
	 */
	public static final Tile wall3 = new Tile("/map/wall3");
	/**
	 * Kafelek ściany
	 */
	public static final Tile wall4 = new Tile("/map/wall4");
	/**
	 * Kafelek dachu
	 */
	public static final Tile roof1 = new Tile("/map/roof1").setSolid();
	/**
	 * Kafelek dachu
	 */
	public static final Tile roof2 = new Tile("/map/roof2").setSolid();
	/**
	 * Kafelek dachu
	 */
	public static final Tile roof3 = new Tile("/map/roof3").setSolid();

	/**
	 * Id kafelka
	 */
	private byte id;
	/**
	 * Czy jest nieprzenikalny
	 */
	private boolean solid;
	/**
	 * Czy jest widoczny
	 */
	private boolean visible;
	/**
	 * Lokalizacja pliku tekstury kafelka
	 */
	private String texture;

	/**
	 * Konstruktor.
	 * Przypisuje id, teksture, ustawia solid na false, visible na true, dodaje kafelek do tablicy kafelków.
	 * @param texture
	 */
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if (tiles[id] != null) throw new IllegalStateException("Kafelek na [" + id + "] jest juz uzywany");
		tiles[id] = this;
		visible = true;
	}

	/**
	 * Setter solid
	 * @return kafelek
	 */
	public Tile setSolid() {
		this.solid = true;
		return this;
	}

	/**
	 * Setter visible
	 * @param is czy ma być widoczny czy nie
	 * @return kafelek
	 */
	public Tile setVisible(boolean is){
		visible = is;
		return this;
	}

	/**
	 * Getter visible
	 * @return czy jest widoczny czy nie
	 */
	public boolean isVisible(){
		return visible;
	}

	/**
	 * Getter solid
	 * @return czy jest nieprzenikalny czy nie
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Getter id
	 * @return id kafelka
	 */
	public byte getId() {
		return id;
	}

	/**
	 * Getter texture
	 * @return tekstura kafelka
	 */
	public String getTexture() {
		return texture;
	}
}
