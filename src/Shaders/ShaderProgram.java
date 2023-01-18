/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Utilities.InternalJarFile;
import Utilities.ResourceStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;

public class ShaderProgram {

	private int programID;

	public ShaderProgram(InternalJarFile vertexFile, InternalJarFile fragmentFile, String[] inVariables, int[] inIndices) {
		int vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		int fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		bindAttributes(inVariables, inIndices);

		GL20.glLinkProgram(programID);

		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
	}

	protected void storeAllUniformLocations(Uniform[] uniforms) {
		for (int i = 0; i < uniforms.length; i++) {
			uniforms[i].storeUniformLocation(programID);
		}

		GL20.glValidateProgram(programID);
	}

	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	protected void load3DVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	public void startProgram() {
		GL20.glUseProgram(programID);
	}

	public void stopProgram() {
		GL20.glUseProgram(0);
	}

	public void free() {
		stopProgram();
		GL20.glDeleteProgram(programID);
	}

	private void bindAttributes(String[] inVariables, int[] inIndices) {
		for (int i = 0; i < inVariables.length; i++) {
			GL20.glBindAttribLocation(programID, inIndices[i], inVariables[i]);
		}
	}
	
	private int loadShader(InternalJarFile file, int type) {
		StringBuilder shaderSource = new StringBuilder();

		try {
			BufferedReader reader = ResourceStreamReader.getResourceReader(file.getPath());
			String line;

			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}

			reader.close();
		} catch (Exception e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader " + file);
			System.exit(-1);
		}

		return shaderID;
	}
}