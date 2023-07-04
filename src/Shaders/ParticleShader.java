/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 15, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformMatrix;
import Uniforms.UniformSampler;
import Utilities.InternalJarFile;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/ParticleVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/ParticleFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position", "modelViewMatrix", "textureOffsets", "blend"};
	private static final int[] IN_INDICES = {0, 1, 5, 6};

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    private UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	private UniformSampler particleTexture = new UniformSampler("particleTexture");

	public ParticleShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{projectionMatrix, numberOfRows});
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		projectionMatrix.loadMatrix(matrix);
	}

	public void loadNumberOfRows(float value) {
		numberOfRows.loadFloat(value);
	}

	public void connectTextureUnits() {
		particleTexture.loadTexUnit(0);
	}
}
