/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 7, 2021
 */

package RenderEngine;

import Models.RawModel;
import Shaders.TerrainShader;
import Terrains.Terrain;
import Textures.TerrainTexturePack;
import Utilities.Mathematics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainRenderer {

    private static float MAX_SHADOW_DISTANCE;

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix, float shadowDistance) {
        this.shader = shader;
        this.MAX_SHADOW_DISTANCE = shadowDistance;

        shader.startProgram();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stopProgram();
    }

    public void render(List<Terrain> terrains, Matrix4f toShadowMapSpace) {
        shader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
        shader.loadToShadowSpaceMatrix(toShadowMapSpace);
        shader.loadShadowDistance(MAX_SHADOW_DISTANCE);
        shader.loadBrightness(MasterRenderer.TERRAIN_BRIGHTNESS);

        for (int i = 0; i < terrains.size(); i++) {
            Terrain terrain = terrains.get(i);

            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0); // 0 -> position
        GL20.glEnableVertexAttribArray(1); // 1 -> textureCoords
        GL20.glEnableVertexAttribArray(2); // 2 -> normal

        bindTextures(terrain);

        shader.loadShineVariables(1, 0);
    }

    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRedTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGreenTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBlueTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0); // unbind VAO
    }

    private void loadModelMatrix(Terrain terrain) {
        Vector3f position = new Vector3f(terrain.getxCoord(), 0, terrain.getzCoord());
        Matrix4f transformationMatrix = Mathematics.createTransformationMatrix(position, new Vector3f(0, 0, 0), 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
