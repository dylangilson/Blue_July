/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Animation;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AnimationVBO {
	
	private final int vboID;
	private final int type;
	
	private AnimationVBO(int vboID, int type) {
		this.vboID = vboID;
		this.type = type;
	}
	
	public static AnimationVBO create(int type) {
		int id = GL15.glGenBuffers();

		return new AnimationVBO(id, type);
	}
	
	public void bind() {
		GL15.glBindBuffer(type, vboID);
	}
	
	public void unbind() {
		GL15.glBindBuffer(type, 0);
	}
	
	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

		buffer.put(data);
		buffer.flip();

		storeData(buffer);
	}

	public void storeData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

		buffer.put(data);
		buffer.flip();

		storeData(buffer);
	}
	
	public void storeData(IntBuffer data) {
		GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
	}
	
	public void storeData(FloatBuffer data) {
		GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
	}

	public void free() {
		GL15.glDeleteBuffers(vboID);
	}
}
