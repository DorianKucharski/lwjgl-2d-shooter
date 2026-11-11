package gra.io;

/**
 * Zegar gry
 */
public class Timer {
	/**
	 * Zwraca aktualny czas w nanosekundach podzielony przez 1000000000
	 * @return aktualny czas
	 */
	public static double getTime() {
		return (double) System.nanoTime() / (double) 1000000000L;
	}
}
