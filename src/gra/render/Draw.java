package gra.render;


import gra.entity.Transform;
import gra.game.Main;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Rysowananie napisów i dymków z tekstem
 */
public class Draw {
    /**
     * Shader napisów
     */
    private static Shader shader;
    /**
     * Kamera okna
     */
    private static Camera camera;
    /**
     * Arkusz czcionki
     */
    private static SpriteSheet sheet;
    /**
     * Arkusz dymków
     */
    private static SpriteSheet cloudSheet;

    /**
     * Inicjalizacja shadera, kamery, arkuszy czcionki i dymków
     */
    static {
        shader = new Shader("gui");
        camera = new Camera(Main.getWindow().getWidth(), Main.getWindow().getHeight());
        sheet = new SpriteSheet("font.png", 18);
        cloudSheet = new SpriteSheet("gui.png", 9);
    }

    /**
     * Rysowanie dymku z tekstem.
     * Oblicza rozmiar dymku zależny od długości tekstu i ilości lini, następnie rysuje dymek poprzez odwołanie się do
     * metody bubble oraz napis za pomocą metody string.
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scale skala dumku
     * @param text tekst który ma być narysowany
     */
    public static void bubbleWithText(float x, float y, float scale, String text) {
        String[] lines = text.split("\n");
        float bubbleScaleY = (2 + lines.length) * scale;
        float widthOfText = 0;
        for (String line : lines) {
            if (widthOfText < line.length()) {
                widthOfText = line.length();
            }
        }

        float bubbleScaleX = (4 + widthOfText) * scale;
        bubble(x, y, bubbleScaleX, bubbleScaleY);
        for (String line : lines) {
            string(line, x - widthOfText * scale, y + lines.length * scale, scale, scale);
            y -= (1 + scale) * 2;
        }
    }

    /**
     * Rysowanie dymku. Renderuje osobno krawędzie, wierzchołki i środek dymku, umieszczając je w obliczonych miejscach,
     * tak aby zachować proporcje dymku przy rozciąganiu.
     * @param x położenie dymku na osi X, jego środek
     * @param y położenie dymku na osi Y, jego środek
     * @param scaleX skala w osi X
     * @param scaleY skala w osi Y
     */
    public static void bubble(float x, float y, float scaleX, float scaleY) {
        shader.bind();
        shader.setUniform("sampler", 0);
        Matrix4f transform = new Matrix4f();
        Vector2f position = new Vector2f(x, y);
        Vector2f scale = new Vector2f(scaleX, scaleY);
        transform.identity().translate(position.x, position.y, 0).scale(scale.x - 16, scale.y - 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 1, 4);
        Assets.getModel().render();

        transform.identity().translate(position.x, position.y + scale.y - 16, 0).scale(scale.x - 16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 1, 3);
        Assets.getModel().render();

        transform.identity().translate(position.x, position.y - scale.y + 16, 0).scale(scale.x - 16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 1, 5);
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y, 0).scale(16, scale.y - 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 0, 4);
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y, 0).scale(16, scale.y - 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 2, 4);
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y + scale.y - 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 0, 3);
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y + scale.y - 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 2, 3);
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y - scale.y + 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 0, 5);
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y - scale.y + 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 2, 5);
        Assets.getModel().render();

        transform.identity().translate(position.x, position.y - scale.y - 6, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        cloudSheet.bindTile(shader, 3, 3);
        Assets.getModel().render();

    }

    /**
     * Rysuje napis o środku w podanym położeniu.
     * @param string tekst do narysowania
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scaleX szerokość napisu
     * @param scaleY wysokość napisu
     */
    public static void stringCentered(String string, float x, float y, float scaleX, float scaleY) {
        string(string, x - (string.length() - 1) * scaleX, y, scaleX, scaleY);
    }

    /**
     * Rysuje napis o środku w podanym położeniu.
     * @param string napis do narysowania
     * @param x położenie na osi X
     * @param y położenie na osi y
     * @param scale rozmiar napisy
     */
    public static void stringCentered(String string, float x, float y, float scale) {
        string(string, x - (string.length() - 1) * scale, y, scale);
    }

    /**
     * Rysuje napis od danego położenia
     * @param string tekst do narysowania
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scale rozmiar napisu
     */
    public static void string(String string, float x, float y, float scale) {
        string(string, x, y, scale, scale);
    }

    /**
     * Rysowanie napisu. Rysuje kolejno każdą literę ze stringu, wczytując ją z siatki czcionki, przesuwa kolejne litery
     * @param string napis do narysowania
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scaleX szerokość napisu
     * @param scaleY wysokość napisu
     */
    public static void string(String string, float x, float y, float scaleX, float scaleY) {
        Matrix4f transform = new Matrix4f();
        shader.bind();
        shader.setUniform("sampler", 0);
        string = string.toUpperCase();
        for (int i = 0; i < string.length(); i++) {
            transform.identity().translate(x + (scaleX * i * 2), y, 0).scale(scaleX, scaleY, 1);
            shader.setUniform("projection", camera.getProjection().mul(transform));
            if (string.equals("HEART")) {
                sheet.bindTile(shader, 7, 9);
                Assets.getModel().render();
                break;
            }
            if (string.equals("BLACK")) {
                sheet.bindTile(shader, 6, 9);
                Assets.getModel().render();
                break;
            }

            if (string.equals("WHITE")) {
                sheet.bindTile(shader, 5, 9);
                Assets.getModel().render();
                break;
            }

            if (string.equals("PICTURE")) {
                sheet.bindTile(shader, 0, 1);
                Assets.getModel().render();

                transform.identity().translate(x + (scaleX * 2), y, 0).scale(scaleX, scaleY, 1);
                shader.setUniform("projection", camera.getProjection().mul(transform));

                sheet.bindTile(shader, 1, 1);
                Assets.getModel().render();
                break;
            } else {
                char znak = string.charAt(i);
                switch (znak) {
                    case '0':
                        sheet.bindTile(shader, 1, 0);
                        break;
                    case '1':
                        sheet.bindTile(shader, 2, 0);
                        break;
                    case '2':
                        sheet.bindTile(shader, 3, 0);
                        break;
                    case '3':
                        sheet.bindTile(shader, 4, 0);
                        break;
                    case '4':
                        sheet.bindTile(shader, 5, 0);
                        break;
                    case '5':
                        sheet.bindTile(shader, 6, 0);
                        break;
                    case '6':
                        sheet.bindTile(shader, 7, 0);
                        break;
                    case '7':
                        sheet.bindTile(shader, 8, 0);
                        break;
                    case '8':
                        sheet.bindTile(shader, 9, 0);
                        break;
                    case '9':
                        sheet.bindTile(shader, 10, 0);
                        break;
                    case 'A':
                        sheet.bindTile(shader, 0, 4);
                        break;
                    case 'B':
                        sheet.bindTile(shader, 1, 4);
                        break;
                    case 'C':
                        sheet.bindTile(shader, 2, 4);
                        break;
                    case 'D':
                        sheet.bindTile(shader, 3, 4);
                        break;
                    case 'E':
                        sheet.bindTile(shader, 4, 4);
                        break;
                    case 'F':
                        sheet.bindTile(shader, 5, 4);
                        break;
                    case 'G':
                        sheet.bindTile(shader, 6, 4);
                        break;
                    case 'H':
                        sheet.bindTile(shader, 7, 4);
                        break;
                    case 'I':
                        sheet.bindTile(shader, 8, 4);
                        break;
                    case 'J':
                        sheet.bindTile(shader, 9, 4);
                        break;
                    case 'K':
                        sheet.bindTile(shader, 10, 4);
                        break;
                    case 'L':
                        sheet.bindTile(shader, 11, 4);
                        break;
                    case 'M':
                        sheet.bindTile(shader, 12, 4);
                        break;
                    case 'N':
                        sheet.bindTile(shader, 13, 4);
                        break;
                    case 'O':
                        sheet.bindTile(shader, 14, 4);
                        break;
                    case 'P':
                        sheet.bindTile(shader, 15, 4);
                        break;
                    case 'Q':
                        sheet.bindTile(shader, 0, 5);
                        break;
                    case 'R':
                        sheet.bindTile(shader, 1, 5);
                        break;
                    case 'S':
                        sheet.bindTile(shader, 2, 5);
                        break;
                    case 'T':
                        sheet.bindTile(shader, 3, 5);
                        break;
                    case 'U':
                        sheet.bindTile(shader, 4, 5);
                        break;
                    case 'V':
                        sheet.bindTile(shader, 5, 5);
                        break;
                    case 'W':
                        sheet.bindTile(shader, 6, 5);
                        break;
                    case 'X':
                        sheet.bindTile(shader, 7, 5);
                        break;
                    case 'Y':
                        sheet.bindTile(shader, 8, 5);
                        break;
                    case 'Z':
                        sheet.bindTile(shader, 9, 5);
                        break;
                    case '#':
                        sheet.bindTile(shader, 0, 1);
                        break;
                    case ':':
                        sheet.bindTile(shader, 4, 1);
                        break;
                    case '-':
                        sheet.bindTile(shader, 12, 0);
                        break;
                    case '+':
                        sheet.bindTile(shader, 11, 0);
                        break;
                    case '.':
                        sheet.bindTile(shader, 2, 1);
                        break;

                    default:
                        sheet.bindTile(shader, 0, 0);
                        break;
                }
            }
            Assets.getModel().render();
        }
    }

    /**
     * Rysowanie obrazka.
     * @param fileName nazwa pliku obrazku
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scaleX szerokość obrazka
     * @param scaleY wysokość obrazka
     */
    public static void picture(String fileName, float x, float y, float scaleX, float scaleY) {
        Texture texture = new Texture(fileName);
        picture(texture, x,y,scaleX,scaleY);
    }

    /**
     * Rysowanie obrazka
     * @param texture tekstura obrazka
     * @param x położenie na osi X
     * @param y położenie na osi Y
     * @param scaleX szerokość obrazka
     * @param scaleY wysokość obrazka
     */
    public static void picture(Texture texture, float x, float y, float scaleX, float scaleY) {
        Transform transform = new Transform(x,y, scaleX,scaleY);
        Matrix4f target = camera.getProjection();
        Main.getShader().bind();
        Main.getShader().setUniform("sampler", 0);
        Main.getShader().setUniform("projection", transform.getProjection(target));
        texture.bind(0);
        Assets.getModel().render();

    }


}
