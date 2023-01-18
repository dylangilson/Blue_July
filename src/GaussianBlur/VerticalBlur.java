/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package GaussianBlur;

import Shaders.VerticalBlurShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import RenderEngine.ImageRenderer;

public class VerticalBlur {
	
	private ImageRenderer renderer;
	private VerticalBlurShader shader;
	
	public VerticalBlur(int targetFBOWidth, int targetFBOHeight) {
		shader = new VerticalBlurShader();
		renderer = new ImageRenderer(targetFBOWidth, targetFBOHeight);

		shader.startProgram();

		shader.stopProgram();
	}

	
	public void render(int textureID, boolean isInputStage) {
		shader.startProgram();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		renderer.renderQuad(isInputStage);

		shader.stopProgram();
	}

	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void free() {
		renderer.free();
		shader.free();
	}
}
