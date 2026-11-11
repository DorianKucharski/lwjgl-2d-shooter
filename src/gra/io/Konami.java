package gra.io;


import gra.game.Main;


/**
 * Kody
 */
public class Konami {

    /**
     * Lista ostatnio wcisnietych zdefiniowanych klawiszy
     */
    private static StringBuilder stringBuilder = new StringBuilder();

    /**
     * Sprawdza czy dany kod zostal wpisany i odpowiada za jego uruchomienie.
     * @param window okno gry
     */
    public static void check(Window window) {
        int code = window.getInput().returnkey();
        if (code >= 65 && code <= 90) {
            stringBuilder.append((char) code);
        }


        if (stringBuilder.toString().contains("AEZAKMI")) {
            if (Main.getWorld() != null) {
                Main.increasePoints(90000);
            }
            stringBuilder.setLength(0);
        } else if (stringBuilder.toString().contains("HESOYAM")) {
            if (Main.getWorld() != null) {
                Main.setLives(10);
            }
            stringBuilder.setLength(0);
        } else if (stringBuilder.toString().contains("GODMODE")) {
            if (Main.getWorld() != null) {
                Main.getWorld().player.setGodMode(!Main.getWorld().player.isGodMode());
            }
            stringBuilder.setLength(0);
        } else if (stringBuilder.toString().contains("KILLEMALL")) {
            if (Main.getWorld() != null) {
                Main.killEnemies();
            }
            stringBuilder.setLength(0);
        } else if (stringBuilder.toString().contains("NEXTLVL")){
            if (Main.getWorld() != null){
                Main.nextLevel();
            }
            stringBuilder.setLength(0);
        } else if (stringBuilder.toString().contains("IREGRETNOTHING")){
            if (Main.getWorld() != null){
                Main.gameOver();
            }
            stringBuilder.setLength(0);
        }


        if (stringBuilder.length() > 100) {
            stringBuilder.setLength(0);
        }


    }
}