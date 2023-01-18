/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {
	
	private String name;
	private int location;
	
	public Uniform(String name) {
		this.name = name;
	}
	
	public void storeUniformLocation(int programID) {
		location = GL20.glGetUniformLocation(programID, name);
	}
	
	protected int getLocation() {
		return location;
	}
}
