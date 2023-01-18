/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package GaussianBlur;

import Shaders.HorizontalBlurShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import RenderEngine.ImageRenderer;

public class HorizontalBlur {
	
	private ImageRenderer renderer;
	private HorizontalBlurShader shader;
	
	public HorizontalBlur(int targetFBOWidth, int targetFBOHeight) {
		renderer = new ImageRenderer(targetFBOWidth, targetFBOHeight);
		shader = new HorizontalBlurShader();

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
