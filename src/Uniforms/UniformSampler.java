/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;

public class UniformSampler extends Uniform {

	private int currentValue;
	private boolean used = false;

	public UniformSampler(String name) {
		super(name);
	}

	public void loadTexUnit(int texUnit) {
		if (!used || currentValue != texUnit) {
			GL20.glUniform1i(super.getLocation(), texUnit);
			used = true;
			currentValue = texUnit;
		}
	}
}
