/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 18, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformMatrix;
import Utilities.InternalJarFile;

import org.lwjgl.util.vector.Matrix4f;

public class ShadowShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/ShadowVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/ShadowFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position", "in_textureCoords"};
	private static final int[] IN_INDICES = {0, 1};
	
	private UniformMatrix mvpMatrix = new UniformMatrix("mvpMatrix");

	public ShadowShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{mvpMatrix});
	}

	public void loadMvpMatrix(Matrix4f matrix) {
		mvpMatrix.loadMatrix(matrix);
	}
}
