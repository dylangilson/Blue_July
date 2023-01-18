/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Shaders;

import Uniforms.UniformSampler;
import Utilities.InternalJarFile;

public class BrightFilterShader extends ShaderProgram {
	
	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/BloomVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/BrightFilterFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};

	public UniformSampler colourTexture = new UniformSampler("colourTexture");
	
	public BrightFilterShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		connectTextureUnits();
	}

	public void connectTextureUnits() {
		super.startProgram();

		colourTexture.loadTexUnit(0);

		super.stopProgram();
	}
}
