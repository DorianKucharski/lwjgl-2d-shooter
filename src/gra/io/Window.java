package gra.io;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.*;

/**
 * Okno
 */
public class Window {
    /**
     * Wskażnik do obiektu okna biblioteki glfw
     */
    private long window;
    /**
     * Szerokość okna
     */
    private int width;
	/**
	 * Wysokość okna
	 */
	private int height;
	/**
	 * Czy okno ma być zmaksymalnizowane
	 */
    private boolean fullscreen;
	/**
	 * Czy okno zmieniło rozmiar
	 */
	private boolean hasResized;
	/**
	 * Callback zmiany rozmiaru okna
	 */
    private GLFWWindowSizeCallback windowSizeCallback;
	/**
	 * Obiekt input
	 */
    private Input input;

	/**
	 * Ustawienie callbacków błędów
	 */
	public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallbackI() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

	/**
	 * Usstawienie callbacku zmiany rozmiaru okna
	 */
	private void setLocalCallbacks() {
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long argWindow, int argWidth, int argHeight) {
                width = argWidth;
                height = argHeight;
                hasResized = true;
            }
        };

        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

	/**
	 * Konstruktor
	 * Ustawia standardowy rozmiar
	 */
	public Window() {
        setSize(640, 480);
        setFullscreen(false);
        hasResized = false;
    }

	/**
	 * Tworzy obiekt okna biblioteki glfw, przypisuje wksaźnik na niego do pola window, wyświetla okno i ustawia okna
	 * na domyślny kontekst glfw
	 * @param title
	 */
	public void createWindow(String title) {
        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);

        if (window == 0) throw new IllegalStateException("Failed to create window!");

        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
        }

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        input = new Input(window);
        setLocalCallbacks();
    }

	/**
	 * Czy okno powinno zostać zamknięte.
	 * @return true jeśli okno powinno się zamknąć
	 */
	public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

	/**
	 * Zamiana bufferów okna
	 */
	public void swapBuffers() {
        glfwSwapBuffers(window);
    }

	/**
	 * Ustawienie rozmiaru okna
	 * @param width szerokość okna w pixelach
	 * @param height wysokość okna w pixelach
	 */
	public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

	/**
	 * Ustawienie okna na pełny ekran
	 * @param fullscreen true - pełny ekran, false - okno
	 */
	public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

	/**
	 * Aktualizacja stanu okna, resetuje pole hasResized, odczytuje interfejs wejścia.
	 */
	public void update() {
        hasResized = false;
        input.update();
        glfwPollEvents();
    }

	/**
	 * Getter szerokości okna
	 * @return szerokość w pixelach
	 */
	public int getWidth() {
        return width;
    }

	/**
	 * Getter wysokości okna
	 * @return wysokość w pixelach
	 */
	public int getHeight() {
        return height;
    }

	/**
	 * Czy okno zmieniło swój rozmiar
	 * @return true jeśli zmieniło rozmiar
	 */
	public boolean hasResized() {
        return hasResized;
    }

	/**
	 * Getter wskaźnika na obiekt okna
	 * @return wksąźnik
	 */
    public long getWindow() {
        return window;
    }

	/**
	 * Getter obiektu interfejsu wejścia
	 * @return obiekt input
	 */
    public Input getInput() {
        return input;
    }
}
