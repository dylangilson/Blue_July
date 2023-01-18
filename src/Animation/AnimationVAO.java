/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Animation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class AnimationVAO {
	
	private static final int BYTES_PER_FLOAT = 4;
	private static final int BYTES_PER_INT = 4;

	private final List<AnimationVBO> dataVBOs = new ArrayList<AnimationVBO>();
	public final int id;
	private AnimationVBO indexVBO;
	private int indexCount;

	public static AnimationVAO create() {
		int id = GL30.glGenVertexArrays();

		return new AnimationVAO(id);
	}

	private AnimationVAO(int id) {
		this.id = id;
	}
	
	public int getIndexCount() {
		return indexCount;
	}

	public void bind(int[] attributes) {
		bind();

		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				GL20.glEnableVertexAttribArray(attributes[i]);
			}
		}
	}

	public void unbind(int[] attributes) {
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				GL20.glDisableVertexAttribArray(attributes[i]);
			}
		}

		unbind();
	}
	
	public void createIndexBuffer(int[] indices) {
		this.indexVBO = AnimationVBO.create(GL15.GL_ELEMENT_ARRAY_BUFFER);

		indexVBO.bind();

		indexVBO.storeData(indices);

		this.indexCount = indices.length;
	}

	public void createAttribute(int attribute, float[] data, int attrSize) {
		AnimationVBO dataVBO = AnimationVBO.create(GL15.GL_ARRAY_BUFFER);

		dataVBO.bind();

		dataVBO.storeData(data);
		GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);

		dataVBO.unbind();

		dataVBOs.add(dataVBO);
	}
	
	public void createIntAttribute(int attribute, int[] data, int attrSize) {
		AnimationVBO dataVBO = AnimationVBO.create(GL15.GL_ARRAY_BUFFER);

		dataVBO.bind();

		dataVBO.storeData(data);
		GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);

		dataVBO.unbind();

		dataVBOs.add(dataVBO);
	}
	
	public void free() {
		GL30.glDeleteVertexArrays(id);

		for (int i = 0; i < dataVBOs.size(); i++) {
			dataVBOs.get(i).free();
		}

		indexVBO.free();
	}

	private void bind() {
		GL30.glBindVertexArray(id);
	}

	private void unbind() {
		GL30.glBindVertexArray(0);
	}
}
