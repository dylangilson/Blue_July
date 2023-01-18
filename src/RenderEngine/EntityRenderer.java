/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package RenderEngine;

import Entities.InanimateEntity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShader;
import Textures.ModelTexture;
import Utilities.Mathematics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.startProgram();

        shader.loadProjectionMatrix(projectionMatrix);

        shader.stopProgram();
    }

    public void render(Map<TexturedModel, List<InanimateEntity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<InanimateEntity> batch = entities.get(model);

            for (int i = 0; i < batch.size(); i++) {
                prepareInstance(batch.get(i));
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0); // 0 -> position
        GL20.glEnableVertexAttribArray(1); // 1 -> textureCoords
        GL20.glEnableVertexAttribArray(2); // 2 -> normal

        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfColumns());

        if (texture.isTransparent()) {
            MasterRenderer.disableCulling();
        }

        shader.loadBrightness(MasterRenderer.ENTITY_BRIGHTNESS);
        shader.loadFakeLightingVariable(texture.useFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0); // unbind VAO
    }

    private void prepareInstance(InanimateEntity entity) {
        Matrix4f transformationMatrix = Mathematics.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadAtlasOffset(new Vector2f(entity.getTextureOffsetX(), entity.getTextureOffsetY()));
    }
}
