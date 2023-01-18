/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 22, 2021
 */

package Shaders;

import Entities.Camera;
import Entities.Light;
import RenderEngine.MasterRenderer;
import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformInt;
import Uniforms.UniformMatrix;
import Uniforms.UniformSampler;
import Uniforms.UniformVec3;
import Utilities.InternalJarFile;
import Utilities.Mathematics;

import org.lwjgl.util.vector.Matrix4f;

public class WaterShader extends ShaderProgram {

	private final static InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/WaterVertexShader.glsl");
	private final static InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/WaterFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};

	private UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformSampler reflectionTexture = new UniformSampler("reflectionTexture");
	private UniformSampler refractionTexture = new UniformSampler("refractionTexture");
	private UniformSampler dudvMap = new UniformSampler("dudvMap");
	private UniformSampler normalMap = new UniformSampler("normalMap");
	private UniformSampler depthMap = new UniformSampler("depthMap");
	private UniformFloat moveFactor = new UniformFloat("moveFactor");
	private UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	private UniformVec3 lightPosition = new UniformVec3("lightPosition");
	private UniformVec3 lightColour = new UniformVec3("lightColour");
	private UniformFloat nearPlane = new UniformFloat("nearPlane");
	private UniformFloat farPlane = new UniformFloat("farPlane");

	public WaterShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{modelMatrix, viewMatrix, projectionMatrix, reflectionTexture, refractionTexture, dudvMap, normalMap, depthMap, moveFactor, cameraPosition,
				lightPosition, lightColour, nearPlane, farPlane});
	}

	public void connectTextureUnits() {
		reflectionTexture.loadTexUnit(0);
		refractionTexture.loadTexUnit(1);
		dudvMap.loadTexUnit(2);
		normalMap.loadTexUnit(3);
		depthMap.loadTexUnit(4);
		nearPlane.loadFloat(MasterRenderer.NEAR_PLANE);
		farPlane.loadFloat(MasterRenderer.FAR_PLANE);
	}

	public void loadLight(Light sun) {
		lightPosition.loadVec3(sun.getPosition());
		lightColour.loadVec3(sun.getColour());
	}

	public void loadMoveFactor(float value) {
		moveFactor.loadFloat(value);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		projectionMatrix.loadMatrix(matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Mathematics.createViewMatrix(camera);

		viewMatrix.loadMatrix(matrix);
		cameraPosition.loadVec3(camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f matrix) {
		modelMatrix.loadMatrix(matrix);
	}
}
