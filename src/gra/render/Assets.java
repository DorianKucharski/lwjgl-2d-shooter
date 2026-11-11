

package gra.render;


/**
 * Assety
 */
public class Assets {
    /**
     * Model
     */
    private static Model model;

	/**
	 * Inicjalizacja modelu
	 */
	public static void initAsset() {
        float[] vertices = new float[]{
                -1f, 1f, 0,
                1f, 1f, 0,
                1f, -1f, 0,
                -1f, -1f, 0,
        };

        float[] texture = new float[]{
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, texture, indices);
    }

	/**
	 * Getter modelu
	 * @return model
	 */
	public static Model getModel() {
        return model;
    }

	/**
	 * Usuwanie modelu
	 */
	public static void deleteAsset() {
        model = null;
    }
}
