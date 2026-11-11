package gra.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

/**
 * Shader
 */
public class Shader {
	/**
	 * Wskaźnik do obiektu programu opengl
	 */
	private int programObject;

	/**
	 * Konstruktor. Tworzy program openGl, przypisuje wskaźnik na niego do pola programObject.
	 * Tworzy obiekty shaderów vertex i fragment, przypisuje wskaźniki na nie do zmiennych.
	 * Ładuje kod źródówy shaderów z plików oraz kompiluje kod. Następnie dołącza shadery do głównego programu opengl,
	 * wiaże shadery, następnie podłącza główny program opengl.
	 * @param filename nazwa pliku kodu źródłowego shadera.
	 */
	public Shader(String filename) {
		programObject = glCreateProgram();

		/**
		 * Wskaźnik do obiektu vertex shader
		 */
		int vertexShaderObject = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderObject, readFile(filename + ".vs"));
		glCompileShader(vertexShaderObject);
		if (glGetShaderi(vertexShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(vertexShaderObject));
			System.exit(1);
		}

		/**
		 * Wskaźnik do obiektu fragment shader
		 */
		int fragmentShaderObject = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderObject, readFile(filename + ".fs"));
		glCompileShader(fragmentShaderObject);
		if (glGetShaderi(fragmentShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(fragmentShaderObject));
			System.exit(1);
		}
		
		glAttachShader(programObject, vertexShaderObject);
		glAttachShader(programObject, fragmentShaderObject);
		
		glBindAttribLocation(programObject, 0, "vertices");
		glBindAttribLocation(programObject, 1, "textures");
		
		glLinkProgram(programObject);
		if (glGetProgrami(programObject, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
		glValidateProgram(programObject);
		if (glGetProgrami(programObject, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
	}

	/**
	 * Ustawia podaną zmienną shadera na określoną wartość
	 * @param uniformName nazwa zmiennej
	 * @param value wartość
	 */
	public void setUniform(String uniformName, int value) {
		int location = glGetUniformLocation(programObject, uniformName);
		if (location != -1) glUniform1i(location, value);
	}

	/**
	 * Ustawia podaną zmienną shadera na określoną wartość
	 * @param uniformName nazwa zmiennej
	 * @param value wartość
	 */
	public void setUniform(String uniformName, Matrix4f value) {
		int location = glGetUniformLocation(programObject, uniformName);
		FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
		value.get(matrixData);
		if (location != -1) glUniformMatrix4fv(location, false, matrixData);
	}

	/**
	 * Wiąże główny program openGl.
	 */
	public void bind() {
		glUseProgram(programObject);
	}

	/**
	 * Odczytywanie kodu źródłowego shadera.
	 * @param filename nazwa pliku do odczytania
	 * @return zawartość pliku
	 */
	private String readFile(String filename) {
		StringBuilder outputString = new StringBuilder();
		// Read via the classpath stream so it also works when the shaders are packed inside a jar.
		InputStream stream = getClass().getResourceAsStream("/shaders/" + filename);
		if (stream == null) {
			throw new IllegalStateException("Shader not found on classpath: /shaders/" + filename);
		}
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				outputString.append(line);
				outputString.append("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return outputString.toString();
	}
}
