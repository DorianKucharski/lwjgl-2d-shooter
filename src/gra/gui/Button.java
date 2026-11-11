package gra.gui;

import gra.io.Audio;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import gra.render.Assets;
import gra.collision.AABB;
import gra.collision.Collision;
import gra.io.Input;
import gra.render.*;

/**
 * Klasa przycisk
 */
public class Button {
    /**
     * Stała stanu spoczynku
     */
    public static final int STATE_IDLE = 0;
    /**
     * Stała stanu najechania myszką
      */
    public static final int STATE_SELECTED = 1;
    /**
     * Stała stanu kliknięcia
     */
    public static final int STATE_CLICKED = 2;

    /**
     * Bryłą przycisku
     */
    private AABB boundingBox;
    /**
     * Aktualny stan przycisku.
     */
    int selectedState;
    /**
     * Macierz przekształceń przycisku
     */
    private static Matrix4f transform = new Matrix4f();
    /**
     * Tekst przycisku
     */
    private String text;
    /**
     * Rozmiar czcionki przycisku
     */
    float textScale;
    /**
     * Czy przycisk został najechany. Używane do odtwarzania dźwięku
     */
    boolean select;

    /**
     * Konstruktor przycisku
     * @param position dwuwymiarowy wektor pozycji
     * @param scale dwuwymiarowy wektor skali
     */
    public Button(Vector2f position, Vector2f scale) {
        this(position, scale, null);
    }

    /**
     * Konstruktor przycisku z tekstem
     * @param position dwuwymiarowy wektor pozycji
     * @param scale dwuwymiarowy wektor skali
     * @param text tekst przycisku
     */
    public Button(Vector2f position, Vector2f scale, String text) {
        this(position, scale, text, (scale.x / 10));
    }

    /**
     * Konstruktor przycisku z tekstem o określonym rozmiarze czcionki
     * @param position  dwuwymiarowy wektor pozycji
     * @param scale  dwuwymiarowy wektor skali
     * @param text  tekst przycisku
     * @param textScale rozmiar czcionki
     */
    public Button(Vector2f position, Vector2f scale, String text, float textScale) {
        this.boundingBox = new AABB(position, scale);
        selectedState = STATE_IDLE;
        this.text = text;
        this.textScale = textScale;
    }

    /**
     * Metoda zwracająca położenie lewego górnego narożnika przycisku
     * @return Dwuwymiarowy wektor określający położenie
     */
    public Vector2f getCornerUpLeft(){
        Vector2f vector2f = new Vector2f();
        vector2f.x = boundingBox.getCenter().x - boundingBox.half_extent.x+10;
        vector2f.y = boundingBox.getCenter().y + boundingBox.half_extent.y-10;
        return vector2f;
    }

    /**
     * Metoda zwracająca położenie prawego górnego narożnika przycisku
     * @return  Dwuwymiarowy wektor określający położenie
     */
    public Vector2f getCornerUpRight(){
        Vector2f vector2f = new Vector2f();
        vector2f.x = boundingBox.getCenter().x + boundingBox.half_extent.x-10;
        vector2f.y = boundingBox.getCenter().y + boundingBox.half_extent.y-10;
        return vector2f;
    }

    /**
     * Metoda zwracająca wysokość przycisku
     * @return wysokość
     */
    public int getHeight(){
        return (int) (boundingBox.half_extent.y * 2);
    }

    /**
     * Metoda zwracająca środek przycisku
     * @return Dwuwymiarowy wektor wysokości
     */
    public Vector2f getCenter(){
        return boundingBox.center;
    }

    /**
     * Metoda ustawiająca tekst przycisku
     * @param text tekst do ustawienia
     */
    public void setText(String text){
        this.text = text;
    }

    /**
     * Metoda akutalizująca stan przycisku, sprawdza czy wskaźnik myszki koliduje z przyciskiem.
     * Ustawia aktualny stan przycisku, odtwarza dźwięki
     * @param input obiekt obsługujący wejście
     */
    public void update(Input input) {
        Collision data = boundingBox.getCollision(input.getMousePosition());

        if (data.isIntersecting) {
            selectedState = STATE_SELECTED;
            if(!select){
                select = true;
                Audio.playAudio("audio/menu/button_selected.wav");
            }
            if (input.isMouseButtonPressed(0)) {
                selectedState = STATE_CLICKED;
                    Audio.playAudio("audio/menu/button_pressed.wav");
            }
        } else {
            selectedState = STATE_IDLE;
            select = false;
        }
    }

    /**
     * Metoda renderująca przycisk. Renderuje środek przycisku, następnie wywołuje metody renderowania, krawędzi,
     * wierzchołów, oraz tekstu.
     * @param camera kamera
     * @param sheet arkusz kafelków
     * @param shader shader gui
     */
    public void render(Camera camera, SpriteSheet sheet, Shader shader) {
        shader.bind();
        Vector2f position = boundingBox.getCenter(), scale = boundingBox.getHalfExtent();
        transform.identity().translate(position.x, position.y, 0).scale(scale.x, scale.y, 1);

        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 4, 1);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 7, 1);
                break;
            default:
                sheet.bindTile(shader, 1, 1);
                break;
        }
        Assets.getModel().render();
        renderSides(position, scale, camera, sheet, shader);
        renderCorners(position, scale, camera, sheet, shader);

        if (text != null) {
            Draw.stringCentered(text, position.x, position.y, textScale);
        }
    }

    /**
     * Metoda renderująca krawędzie przycisku
     * @param position środek przycisku
     * @param scale skala przycisku
     * @param camera kamera
     * @param sheet arkusz kafelków
     * @param shader shader gui
     */
    private void renderSides(Vector2f position, Vector2f scale, Camera camera, SpriteSheet sheet, Shader shader) {
        transform.identity().translate(position.x, position.y + scale.y - 16, 0).scale(scale.x, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 4, 0);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 7, 0);
                break;
            default:
                sheet.bindTile(shader, 1, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x, position.y - scale.y + 16, 0).scale(scale.x, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 4, 2);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 7, 2);
                break;
            default:
                sheet.bindTile(shader, 1, 2);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y, 0).scale(16, scale.y, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 3, 1);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 6, 1);
                break;
            default:
                sheet.bindTile(shader, 0, 1);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y, 0).scale(16, scale.y, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 5, 1);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 8, 1);
                break;
            default:
                sheet.bindTile(shader, 2, 1);
                break;
        }
        Assets.getModel().render();
    }

    /**
     * Metoda renderująca wierzchołki
     * @param position środek przycisku
     * @param scale skala przycisku
     * @param camera kamera przycisku
     * @param sheet arkusz kafelków
     * @param shader shader gui
     */
    private void renderCorners(Vector2f position, Vector2f scale, Camera camera, SpriteSheet sheet, Shader shader) {
        transform.identity().translate(position.x - scale.x + 16, position.y + scale.y - 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 3, 0);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 6, 0);
                break;
            default:
                sheet.bindTile(shader, 0, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y + scale.y - 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 5, 0);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 8, 0);
                break;
            default:
                sheet.bindTile(shader, 2, 0);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x - scale.x + 16, position.y - scale.y + 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 3, 2);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 6, 2);
                break;
            default:
                sheet.bindTile(shader, 0, 2);
                break;
        }
        Assets.getModel().render();

        transform.identity().translate(position.x + scale.x - 16, position.y - scale.y + 16, 0).scale(16, 16, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        switch (selectedState) {
            case STATE_SELECTED:
                sheet.bindTile(shader, 5, 2);
                break;
            case STATE_CLICKED:
                sheet.bindTile(shader, 8, 2);
                break;
            default:
                sheet.bindTile(shader, 2, 2);
                break;
        }
        Assets.getModel().render();
    }
}
