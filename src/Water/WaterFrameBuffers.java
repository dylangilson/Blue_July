/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 22, 2021
 */

package Water;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class WaterFrameBuffers {

	protected static final int REFLECTION_WIDTH = 320;
	private static final int REFLECTION_HEIGHT = 180;

	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;

	private int reflectionFrameBufferID;
	private int reflectionTextureID;
	private int reflectionDepthBufferID;

	private int refractionFrameBufferID;
	private int refractionTextureID;
	private int refractionDepthTextureID;

	// call when loading the game
	public WaterFrameBuffers() {
		initReflectionFrameBuffer();
		initRefractionFrameBuffer();
	}

	// call when closing the game
	public void free() {
		GL30.glDeleteFramebuffers(reflectionFrameBufferID);
		GL11.glDeleteTextures(reflectionTextureID);
		GL30.glDeleteRenderbuffers(reflectionDepthBufferID);
		GL30.glDeleteFramebuffers(refractionFrameBufferID);
		GL11.glDeleteTextures(refractionTextureID);
		GL11.glDeleteTextures(refractionDepthTextureID);
	}

	// call before rendering to this FBO
	public void bindReflectionFrameBuffer() {
		bindFrameBuffer(reflectionFrameBufferID, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}

	// call before rendering to this FBO
	public void bindRefractionFrameBuffer() {
		bindFrameBuffer(refractionFrameBufferID, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}

	// call to switch to default frame buffer
	public void unbindCurrentFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public int getReflectionTexture() {
		return reflectionTextureID;
	}


	public int getRefractionTexture() {
		return refractionTextureID;
	}

	public int getRefractionDepthTexture() {
		return refractionDepthTextureID;
	}

	private void initReflectionFrameBuffer() {
		reflectionFrameBufferID = createFrameBuffer();
		reflectionTextureID = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthBufferID = createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);

		unbindCurrentFrameBuffer();
	}

	private void initRefractionFrameBuffer() {
		refractionFrameBufferID = createFrameBuffer();
		refractionTextureID = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTextureID = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);

		unbindCurrentFrameBuffer();
	}

	private void bindFrameBuffer(int frameBuffer, int width, int height) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // to make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBufferID = GL30.glGenFramebuffers();

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferID); // generate name for frame buffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0); // create the framebuffer

		return frameBufferID; // indicate that we will always render to color attachment 0
	}

	private int createTextureAttachment(int width, int height) {
		int textureID = GL11.glGenTextures();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, textureID, 0);

		return textureID;
	}

	private int createDepthTextureAttachment(int width, int height) {
		int textureID = GL11.glGenTextures();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer)null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, textureID, 0);

		return textureID;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBufferID = GL30.glGenRenderbuffers();

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBufferID);

		return depthBufferID;
	}
}
