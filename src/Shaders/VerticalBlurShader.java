/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Utilities.InternalJarFile;

public class VerticalBlurShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/VerticalBlurVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/BlurFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};
	
	private UniformFloat targetHeight = new UniformFloat("targetHeight");
	
	public VerticalBlurShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{targetHeight});
	}

	protected void loadTargetHeight(float height) {
		targetHeight.loadFloat(height);
	}
}
