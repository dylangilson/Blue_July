/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 6, 2021
 */
package RenderEngine;

import Engine.Main;
import Entities.Camera;
import Models.RawModel;
import Shaders.SkyboxShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxRenderer {

    private static final float SIZE = 500f;
    private static final String[] DAY_TEXTURE_FILES = {"dayRight", "dayLeft", "dayTop", "dayBottom", "dayBack", "dayFront"};
    private static final String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
    private static final float[] VERTICES = {
        -SIZE,  SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        -SIZE,  SIZE, -SIZE,
        SIZE,  SIZE, -SIZE,
        SIZE,  SIZE,  SIZE,
        SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
        SIZE, -SIZE, -SIZE,
        SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
        SIZE, -SIZE,  SIZE
    };

    private RawModel cube;

    private int[] textureIDs; // index 0 -> day, index 1 -> night
    private SkyboxShader shader;
    private float time = 0;

    public SkyboxRenderer(Matrix4f projectionMatrix) {
        cube = Main.LOADER.loadSkyboxToVAO(VERTICES);
        textureIDs = Main.LOADER.loadCubeMap(DAY_TEXTURE_FILES, NIGHT_TEXTURE_FILES, "Skybox Images");
        shader = new SkyboxShader();

        shader.startProgram();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stopProgram();
    }

    public void render(Camera camera, Vector3f colour) {
        shader.startProgram();
        shader.loadViewMatrix(camera);
        shader.loadFogColour(colour);

        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0); // 0 -> position

        dayNightCycle();

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0); // unbind VAO

        shader.stopProgram();
    }

    private void dayNightCycle() {
        time += DisplayManager.getFrameTimeInSeconds() * 100;
        time %= 24000;

        int texture1;
        int texture2;
        float blendFactor;

        if (time >= 0 && time < 13000) {
            texture1 = textureIDs[0];
            texture2 = textureIDs[0];
            blendFactor = (time - 0) / (13000 - 0);
        } else if (time >= 13000 && time < 18000) {
            texture1 = textureIDs[0];
            texture2 = textureIDs[1];
            blendFactor = (time - 13000) / (18000 - 13000);
        } else if (time >= 18000 && time < 21000) {
            texture1 = textureIDs[1];
            texture2 = textureIDs[1];
            blendFactor = (time - 18000) / (21000 - 18000);
        } else {
            texture1 = textureIDs[1];
            texture2 = textureIDs[0];
            blendFactor = (time - 21000) / (24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);

        shader.loadBlendFactor(blendFactor);
    }
}
