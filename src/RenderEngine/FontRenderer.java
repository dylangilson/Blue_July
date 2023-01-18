/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 2, 2021
 */

package RenderEngine;

import Font.FontType;
import Font.GUIText;
import Shaders.FontShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();

		for (FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());

			for (int i = 0; i < texts.get(font).size(); i++) {
				renderText(texts.get(font).get(i));
			}
		}

		endRendering();
	}

	public void free() {
		shader.free();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		shader.startProgram();
	}
	
	private void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		shader.loadColour(text.getColour());
		shader.loadTranslation(text.getPosition());
		shader.loadOutlineColour(text.getOutlineColour());
		shader.loadBorderLengths(text.getBorderLengths());

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0); // unbind VAO
	}
	
	private void endRendering() {
		shader.stopProgram();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
