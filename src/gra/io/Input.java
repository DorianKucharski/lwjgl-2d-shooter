package gra.io;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

/**
 * Sterowanie
 */
public class Input {
	/**
	 * Okno gry
	 */
	private long window;
	/**
	 * Tablica klawiszy
	 */
	private boolean keys[];
	/**
	 * Tablica przyciskow myszy
	 */
	private boolean mouse_click[];

	/**
	 * Pozycja myszy
	 */
	private static Vector2f mousePos = new Vector2f();

	/**
	 * Pozycja myszy x
	 */
	private static double[] x = new double[1],
	/**
	 * Pozycja myszy y
	 */
	y = new double[1];

	/**
	 * Szerokosc ekranu
	 */
	private static int[] winWidth = new int[1],
	/**
	 * Wysokosc ekranu
	 */
	winHeight = new int[1];

	/**
	 * Konstruktor
	 * @param window okno gry
	 */
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST];
		for (int i = 0; i < GLFW_KEY_LAST; i++)
			keys[i] = false;

		this.mouse_click = new boolean[5];
		for (int i = 0; i < 5; i++)
			mouse_click[i] = false;

	}

	/**
	 * Metoda sprawdzajaca czy dany klawisz jest wcisniety
	 * @param key numer klawisza w ASCI albo jako stala GLFW
	 * @return true jesli wcisniety, false jesli nie
	 */
	public boolean isKeyDown(int key) {
		return glfwGetKey(window, key) == 1;
	}

	/**
	 * Czy klawisz zostal nacisniety. Zwraca pojedyncze wcisniecie
	 * @param key numer klawisza w ASCI albo jako stala GLFW
	 * @return true jesli wcisniety false jesli nie
	 */
	public boolean isKeyPressed(int key) {return (isKeyDown(key) && !keys[key]);}

	/**
	 * Czy klawisz jest wycisniety
	 * @param key numer klawisza w ASCI albo jako stala GLFW
	 * @return true jesli nie jest wcisniety i vice wersa
	 */
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && keys[key]);
	}

	/**
	 * Czy przycisk myszy jest wcisniety
	 * @param button numer przycisku
	 * @return true jesli wcisniety, false jesli nie
	 */
	public boolean isMouseButtonDown(int button) { return glfwGetMouseButton(window, button) == 1; }

	/**
	 * Czy klawisz myszy zostal klikniety. Zwraca pojedyncze wcisniecie
	 * @param button numer przycisku
	 * @return true jesli tak, false jesli...
	 */
	public boolean isMouseButtonPressed(int button){ return ((glfwGetMouseButton(window, button) == 1) && !mouse_click[button]); }

	/**
	 * Zwraca pozycje myszy
	 * @return pozycja myszy
	 */
	public Vector2f getMousePosition() {
		glfwGetCursorPos(window, x, y);

		glfwGetWindowSize(window, winWidth, winHeight);


		mousePos.set((float) x[0] - (winWidth[0] / 2.0f), -((float) y[0] - (winHeight[0] / 2.0f)));

		return mousePos;
	}

	/**
	 * Aktualizuje tablice klawiszy i przyciskow myszy. Sprawdza ich stan.
	 * Niezbedne do wykrywania pojedynczych klikniec.
	 */
	public void update() {

		for (int i = 32; i < GLFW_KEY_LAST; i++){
			keys[i] = isKeyDown(i);
		}

		for (int i = 0; i < 5; i++){
			mouse_click[i] = isMouseButtonDown(i);
		}

	}

	/**
	 * Zwraca numer aktualnie wcisnietego klawisza
	 * @return zwraca numer klawisza
	 */
	public int returnkey(){
		for (int i = 32; i < GLFW_KEY_LAST; i++){
			if(isKeyPressed(i)) return i;
		}
		return 0;
	}
}
