/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 19, 2021
 */

package PostProcessing;

import RenderEngine.ImageRenderer;
import Shaders.ContrastShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {

    private static final float CONTRAST = 0.1f;

    private ImageRenderer renderer;
    private ContrastShader shader;

    public ContrastChanger() {
        renderer = new ImageRenderer();
        shader = new ContrastShader();
    }

    public void render(int textureID, boolean isOnlyEffect) {
        shader.startProgram();

        shader.loadContrast(CONTRAST);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        renderer.renderQuad(isOnlyEffect);

        shader.stopProgram();
    }

    public void free() {
        renderer.free();
        shader.free();
    }
}
