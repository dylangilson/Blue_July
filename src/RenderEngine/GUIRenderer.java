/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * March 23, 2020
 */

package RenderEngine;

import Engine.Main;
import GUIs.GUITexture;
import Models.RawModel;
import Shaders.GUIShader;
import Utilities.Mathematics;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

public class GUIRenderer {

    private final RawModel quad;
    private GUIShader shader;

    public GUIRenderer() {
        float[] positions = {-1 , 1 , -1 , -1 , 1 , 1 , 1 , -1};
        quad = Main.LOADER.loadQuadToVAO(positions);
        shader = new GUIShader();
    }

    public void render(List<GUITexture> guis) {
        shader.startProgram();

        GL30.glBindVertexArray(quad.getVaoID());

        GL20.glEnableVertexAttribArray(0); // 0 -> positions

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (int i = 0; i < guis.size(); i++) {
            GUITexture gui = guis.get(i);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureID());

            Matrix4f matrix = Mathematics.createTransformationMatrix(gui.getPosition(), gui.getScale(), gui.getRotation());

            if (gui.getMirrorHorizontally()) {
                shader.loadMirrorHorizontally(true);
            } else {
                shader.loadMirrorHorizontally(false);
            }

            shader.loadTransformationMatrix(matrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0); // unbind vao

        shader.stopProgram();
    }

    public void free() {
        shader.free();
    }
}
