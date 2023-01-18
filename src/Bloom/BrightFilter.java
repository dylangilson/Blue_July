/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Bloom;

import RenderEngine.ImageRenderer;
import Shaders.BrightFilterShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class BrightFilter {

	private ImageRenderer renderer;
	private BrightFilterShader shader;
	
	public BrightFilter(int width, int height) {
		shader = new BrightFilterShader();
		renderer = new ImageRenderer(width, height);
	}
	
	public void render(int texture) {
		shader.startProgram();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

		renderer.renderQuad(false);

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
