package gra.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture implements Serializable {
	/**
	 * Obiekt tekstury OpenGl
	 */
	private int textureObject;
	/**
	 * Szerokość tekstury
	 */
	private int width;
	/**
	 * Wysokość tekstury
	 */
	private int height;

	/**
	 * Konstruktor. Wczytuje plik tekstur i przetwarza go na obiekt tekstury OpenGl.
	 * @param filename nazwa pliku tekstury
	 */
	public Texture(String filename) {
		BufferedImage bufferedImage;
		try {
			File file = new File(System.getProperty("user.dir")  + filename);
			bufferedImage = ImageIO.read(file);
			width = bufferedImage.getWidth();
			height = bufferedImage.getHeight();

			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int pixel = pixels_raw[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF));
					pixels.put((byte) ((pixel >> 8) & 0xFF));
					pixels.put((byte) (pixel & 0xFF));
					pixels.put((byte) ((pixel >> 24) & 0xFF));
				}
			}

			pixels.flip();

			textureObject = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, textureObject);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		}
		catch (IOException  e) {
			e.printStackTrace();
		}
	}


	/**
	 * Wiązanie tekstury z samplerem
	 * @param sampler id samplera
	 */
	public void bind(int sampler) {
		if (sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, textureObject);
		}
	}
}
