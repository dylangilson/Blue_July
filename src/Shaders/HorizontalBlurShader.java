/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Utilities.InternalJarFile;

public class HorizontalBlurShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/HorizontalBlurVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/BlurFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position"};
	private static final int[] IN_INDICES = {0};
	
	private UniformFloat targetWidth = new UniformFloat("targetWidth");
	
	public HorizontalBlurShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{targetWidth});
	}

	protected void loadTargetWidth(float width) {
		targetWidth.loadFloat(width);
	}
}
