/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Shaders;

import Uniforms.UniformFloat;
import Uniforms.UniformSampler;
import Utilities.InternalJarFile;

public class CombineShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/BloomVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/CombineFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};
	
	private UniformSampler colourTexture = new UniformSampler("colourTexture");
	private UniformSampler highlightTexture = new UniformSampler("highlightTexture");
	
	public CombineShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		connectTextureUnits();
	}

	public void connectTextureUnits() {
		super.startProgram();

		colourTexture.loadTexUnit(0);
		highlightTexture.loadTexUnit(1);

		super.stopProgram();
	}
}
