/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Textures;

import Utilities.InternalJarFile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {

	public final int textureID;
	public final int size;
	private final int type;

	protected Texture(int textureID, int size) {
		this.textureID = textureID;
		this.size = size;
		this.type = GL11.GL_TEXTURE_2D;
	}

	protected Texture(int textureID, int type, int size) {
		this.textureID = textureID;
		this.size = size;
		this.type = type;
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, textureID);
	}

	public void free() {
		GL11.glDeleteTextures(textureID);
	}

	public static TextureBuilder newTexture(InternalJarFile textureFile) {
		return new TextureBuilder(textureFile);
	}
}
