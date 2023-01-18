/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 18, 2021
 */

package RenderEngine;

import Animation.AnimatedModel;
import Entities.AnimatedEntity;
import Entities.Entity;
import Entities.InanimateEntity;
import Models.RawModel;
import Models.TexturedModel;
import Utilities.Mathematics;
import Shaders.ShadowShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader
	 *            - the simple shader program being used for the shadow render
	 *            pass.
	 * @param projectionViewMatrix
	 *            - the orthographic projection matrix multiplied by the light's
	 *            "view" matrix.
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders inanimate entities to the shadow map. Each model is first bound and then all
	 * the inanimate entities using that model are rendered to the shadow map.
	 * 
	 * @param entities
	 *            - the entities to be rendered to the shadow map.
	 */
	public void renderInanimateEntities(Map<TexturedModel, List<InanimateEntity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			RawModel rawModel = model.getRawModel();
			bindModel(rawModel);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

			if (model.getTexture().isTransparent()) {
				MasterRenderer.disableCulling();
			}

			for (int i = 0; i < entities.get(model).size(); i++) {
				prepareInstance(entities.get(model).get(i));
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}

			if (model.getTexture().isTransparent()) {
				MasterRenderer.enableCulling();
			}
		}

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Renders animated entities to the shadow map. Each model is first bound and then all
	 * the inanimate entities using that model are rendered to the shadow map.
	 *
	 * @param entities
	 *            - the entities to be rendered to the shadow map.
	 */
	public void renderAnimatedEntities(List<AnimatedEntity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			AnimatedModel model = entities.get(i).getModel();
			bindModel(model);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().textureID);

			prepareInstance(entities.get(i));
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		}

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Binds a raw model before rendering. Only the attribute 0 is enabled here
	 * because that is where the positions are stored in the VAO, and only the
	 * positions are required in the vertex shader.
	 * 
	 * @param rawModel
	 *            - the model to be bound.
	 */
	private void bindModel(RawModel rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());

		GL20.glEnableVertexAttribArray(0); // 0 -> position
		GL20.glEnableVertexAttribArray(1); // 1 -> textureCoords
	}

	/**
	 * Binds a model before rendering. Only the attribute 0 is enabled here
	 * because that is where the positions are stored in the VAO, and only the
	 * positions are required in the vertex shader.
	 *
	 * @param model
	 *            - the model to be bound.
	 */
	private void bindModel(AnimatedModel model) {
		GL30.glBindVertexArray(model.getModel().id);

		GL20.glEnableVertexAttribArray(0); // 0 -> position
		GL20.glEnableVertexAttribArray(1); // 1 -> textureCoords
	}

	/**
	 * Prepares an entity to be rendered. The model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 * 
	 * @param entity
	 *            - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f modelMatrix = Mathematics.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);

		shader.loadMvpMatrix(mvpMatrix);
	}
}
