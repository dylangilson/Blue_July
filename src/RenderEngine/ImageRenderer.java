/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package RenderEngine;

import PostProcessing.ProcessingFBO;
import org.lwjgl.opengl.GL11;

public class ImageRenderer {

	private ProcessingFBO fbo;

	public ImageRenderer(int width, int height) {
		this.fbo = new ProcessingFBO(width, height, ProcessingFBO.NONE);
	}

	public ImageRenderer() {

	}

	public void renderQuad(boolean isInputStage) {
		if (fbo != null) {
			fbo.bindFrameBuffer(isInputStage);
		}

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void free() {
		if (fbo != null) {
			fbo.free();
		}
	}
}
