/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformSampler;
import Utilities.InternalJarFile;

public class ContrastShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/ContrastVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/ContrastFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};

	public UniformSampler colourTexture = new UniformSampler("colourTexture");
	private UniformFloat contrast = new UniformFloat("contrast");
	
	public ContrastShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{contrast});

		connectTextureUnits();
	}

	public void loadContrast(float value) {
		contrast.loadFloat(value);
	}

	public void connectTextureUnits() {
		super.startProgram();

		colourTexture.loadTexUnit(0);

		super.stopProgram();
	}
}
