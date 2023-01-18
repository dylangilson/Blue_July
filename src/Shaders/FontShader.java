/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 2, 2021
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformVec2;
import Uniforms.UniformVec3;
import Utilities.InternalJarFile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram {

	private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/FontVertexShader.glsl");
	private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/FontFragmentShader.glsl");
	private static final String[] IN_VARIABLES = {"position", "textureCoords"};
	private static final int[] IN_INDICES = {0, 1};

	private UniformVec3 colour = new UniformVec3("colour");
	private UniformVec2 translation = new UniformVec2("translation");
	private UniformVec3 outlineColour = new UniformVec3("outlineColour");
	private UniformFloat borderWidth = new UniformFloat("borderWidth");
	private UniformFloat borderEdge = new UniformFloat("borderEdge");
	
	public FontShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

		super.storeAllUniformLocations(new Uniform[]{colour, translation, outlineColour, borderWidth, borderEdge});
	}

	public void loadColour(Vector3f value) {
		colour.loadVec3(value);
	}

	public void loadTranslation(Vector2f value) {
		translation.loadVec2(value);
	}

	public void loadOutlineColour(Vector3f value) {
		outlineColour.loadVec3(value);
	}

	public void loadBorderLengths(Vector2f borderLengths) {
		borderWidth.loadFloat(borderLengths.x);
		borderEdge.loadFloat(borderLengths.y);
	}
}
