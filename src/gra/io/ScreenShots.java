package gra.io;

import gra.game.Main;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Robienie i zapisywanie zrzutow ekranu
 */
public class ScreenShots {

    /**
     * Robienie i zapisywanie zrzutow ekranu
     * Odczytuje obraz z przedniego bufferu openGl, zapisuje go do bufferu bajtów o rozmiarze określonym przez rozmiar okna
     * następnie zapisuje ten buffer do pliku, jeżeli zrzut ekranu przeznaczony jest do zapisu gry, zmienia jego rozmiar.
     * @param path sciezka pliku do ktorego zapisac zrzut
     */
    public static void take(String path) {
        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = Main.getWindow().getWidth();
        int height = Main.getWindow().getHeight();


        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);


        File file = new File(path + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;

                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);

            }
        }

        if (path.contains("saves")){
            image = resize(image, 1024, 1024);
        }

        try {
            ImageIO.write(image, format, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Przekształcenie bufferu obrazu do podanego rozmiaru
     * @param img buffer obrazu
     * @param newW nowa szerokość obrazu
     * @param newH nowa wysokość obrazu
     * @return przekształcony buffer
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
