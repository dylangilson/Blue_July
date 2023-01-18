/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

public class UniformVec3 extends Uniform {
	private Vector3f vec;
	private boolean used = false;

	public UniformVec3(String name) {
		super(name);
	}

	public void loadVec3(Vector3f vec) {
		if (!used || this.vec.x != vec.x || this.vec.y != vec.y || this.vec.z != vec.z) {
			this.vec = vec;
			used = true;
			GL20.glUniform3f(super.getLocation(), vec.x, vec.y, vec.z);
		}
	}
}
