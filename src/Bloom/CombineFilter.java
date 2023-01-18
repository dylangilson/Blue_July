/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Bloom;

import Shaders.CombineShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import RenderEngine.ImageRenderer;

public class CombineFilter {
	
	public ImageRenderer renderer;
	private CombineShader shader;
	
	public CombineFilter() {
		shader = new CombineShader();

		shader.startProgram();
		shader.connectTextureUnits();
		shader.stopProgram();

		renderer = new ImageRenderer();
	}
	
	public void render(int colourTexture, int highlightTexture) {
		shader.startProgram();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);

		renderer.renderQuad(false);

		shader.stopProgram();
	}
	
	public void free() {
		renderer.free();
		shader.free();
	}
}
