/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

public class UniformVec4 extends Uniform {

	public UniformVec4(String name) {
		super(name);
	}

	public void loadVec4(Vector4f vec) {
		GL20.glUniform4f(super.getLocation(), vec.x, vec.y, vec.z, vec.w);
	}
}
