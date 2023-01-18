/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVec2 extends Uniform {

	private float currentX;
	private float currentY;
	private boolean used = false;

	public UniformVec2(String name) {
		super(name);
	}

	public void loadVec2(Vector2f vec) {
		if (!used || vec.x != currentX || vec.y != currentY) {
			this.currentX = vec.x;
			this.currentY = vec.y;
			used = true;
			GL20.glUniform2f(super.getLocation(), vec.x, vec.y);
		}
	}
}
