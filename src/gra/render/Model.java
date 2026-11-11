package gra.render;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Model {
	/**
	 * Ilość wierzchołków
	 */
	private int drawCount;
	/**
	 * Wskaźnik na obiekt buffera wierzchołków
	 */
	private int vertexObject;
	/**
	 * Wskaźnik na obiekt buffera koordynatów wierzchołków tekstur
	 */
	private int textureCoordObject;
	/**
	 * Wskaźnik na obiekt buffera wksaźników wierzchołków
	 */
	private int indexObject;

	/**
	 * Konstruktor. Inicjalizuje buffery wierzchołków, wierzchołków tekstur oraz indeksów wierzchołków, oraz umieszcza
	 * dane wierzchołków, oraż wiąże buffery.
	 * @param vertices tablica wierzchołków
	 * @param tex_coords tablica koordynatów wierzchołków tekstur
	 * @param indices tablica indeksów wierzchołków
	 */
	public Model(float[] vertices, float[] tex_coords, int[] indices) {
		drawCount = indices.length;
		vertexObject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexObject);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
		
		textureCoordObject = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordObject);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);
		
		indexObject = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexObject);
		
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}


	/**
	 * Renderowanie
	 */
	public void render() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vertexObject);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordObject);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexObject);
		glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
	}

	/**
	 * Tworzy buffer floatów, umiesza w nim dane przesłąne w postaci tablicy, oraz odwraca buffer.
	 * @param data dane do umieszczenia w bufferze
	 * @return buffer liczb zmiennoprzecinkowych
	 */
	private FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
