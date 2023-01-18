/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 6, 2021
 */

package Shaders;

import Entities.Camera;
import RenderEngine.DisplayManager;
import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformMatrix;
import Uniforms.UniformSampler;
import Uniforms.UniformVec3;
import Utilities.InternalJarFile;
import Utilities.Mathematics;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/SkyboxVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/SkyboxFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};

	private static final float ROTATE_SPEED = 0.5f; // degrees per second the Skybox rotates
	
	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformVec3 fogColour = new UniformVec3("fogColour");
	private UniformSampler cubeMap1 = new UniformSampler("cubeMap1");
	private UniformSampler cubeMap2 = new UniformSampler("cubeMap2");
	private UniformFloat blendFactor = new UniformFloat("blendFactor");

	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{projectionMatrix, viewMatrix, cubeMap1, cubeMap2, fogColour, blendFactor});
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		projectionMatrix.loadMatrix(matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Mathematics.createViewMatrix(camera);

		// set translate to (0, 0, 0) so skybox follows camera, but continues to rotate
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;

		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeInSeconds();
		matrix.rotate((float)Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);

		viewMatrix.loadMatrix(matrix);
	}

	public void loadFogColour(Vector3f colour) {
		fogColour.loadVec3(colour);
	}

	public void connectTextureUnits() {
		cubeMap1.loadTexUnit(0);
		cubeMap2.loadTexUnit(1);
	}

	public void loadBlendFactor(float value) {
		blendFactor.loadFloat(value);
	}
}
