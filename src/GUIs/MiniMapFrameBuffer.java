/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 22, 2021
 */

package GUIs;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class MiniMapFrameBuffer {

    protected static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private int FrameBufferID;
    private int TextureID;
    private int DepthBufferID;

    // call when loading the game
    public MiniMapFrameBuffer() {
        initFrameBuffer();
    }

    // call when closing the game
    public void free() {
        GL30.glDeleteFramebuffers(FrameBufferID);
        GL11.glDeleteTextures(TextureID);
        GL30.glDeleteRenderbuffers(DepthBufferID);
    }

    // call before rendering to this FBO
    public void bindFrameBuffer() {
        bindFrameBuffer(FrameBufferID, WIDTH, HEIGHT);
    }

    // call to switch to default frame buffer
    public void unbindCurrentFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public int getTextureID() {
        return TextureID;
    }

    public int getDepthBufferID() {
        return DepthBufferID;
    }

    private void initFrameBuffer() {
        FrameBufferID = createFrameBuffer();
        TextureID = createTextureAttachment(WIDTH, HEIGHT);
        DepthBufferID = createDepthBufferAttachment(WIDTH, HEIGHT);
        unbindCurrentFrameBuffer();
    }

    private void bindFrameBuffer(int frameBuffer, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // make sure the texture isn't bound
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

    private int createDepthBufferAttachment(int width, int height) {
        int depthBufferID = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBufferID);
        return depthBufferID;
    }
}
