/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformMat4Array;
import Uniforms.UniformMatrix;
import Uniforms.UniformSampler;
import Uniforms.UniformVec3;
import Utilities.GlobalConstants;
import Utilities.InternalJarFile;

public class AnimatedModelShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/AnimatedEntityVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/AnimatedEntityFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"in_position", "in_textureCoords", "in_normal", "in_jointIndices", "in_weights"};
	private static final int[] IN_INDICES = {0, 1, 2, 3, 4};

	public UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	public UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	public UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	public UniformVec3 lightDirection = new UniformVec3("lightDirection");
	public UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", GlobalConstants.MAX_JOINTS);
	private UniformSampler diffuseMap = new UniformSampler("diffuseMap");

	/**
	 * Creates the shader program for the {@link RenderEngine.AnimatedModelRenderer} by
	 * loading up the vertex and fragment shader code files. It also gets the
	 * location of all the specified uniform variables, and also indicates that
	 * the diffuse texture will be sampled from texture unit 0.
	 */
	public AnimatedModelShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{projectionMatrix, viewMatrix, transformationMatrix, diffuseMap, lightDirection, jointTransforms});

		connectTextureUnits();
	}

	/**
	 * Indicates which texture unit the diffuse texture should be sampled from.
	 */
	private void connectTextureUnits() {
		super.startProgram();

		diffuseMap.loadTexUnit(0);

		super.stopProgram();
	}
}
