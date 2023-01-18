/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package RenderEngine;

import Entities.AnimatedEntity;
import Entities.Camera;
import Shaders.AnimatedModelShader;
import Utilities.GlobalConstants;
import Utilities.Mathematics;
import Utilities.OpenGLUtilities;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * This class deals with rendering an animated entity. Nothing particularly new
 * here. The only exciting part is that the joint transforms get loaded up to
 * the shader in a uniform array.
 * 
 * @author Eliseo
 *
 */
public class AnimatedModelRenderer {

	private AnimatedModelShader shader;

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	public AnimatedModelRenderer(Matrix4f projectionMatrix) {
		this.shader = new AnimatedModelShader();

		shader.startProgram();

		shader.projectionMatrix.loadMatrix(projectionMatrix);

		shader.stopProgram();
	}

	/**
	 * Renders an animated entity. The main thing to note here is that all the
	 * joint transforms are loaded up to the shader to a uniform array. Also 5
	 * attributes of the VAO are enabled before rendering, to include joint
	 * indices and weights.
	 * 
	 * @param entity
	 *            - the animated entity to be rendered.
	 */
	public void render(AnimatedEntity entity, Camera camera) {
		shader.startProgram();
		prepare(entity, GlobalConstants.LIGHT_DIRECTION);

		entity.getModel().getTexture().bindToUnit(0);
		entity.getModel().getModel().bind(new int[]{0, 1, 2, 3, 4});

		shader.jointTransforms.loadMatrixArray(entity.getModel().getJointTransforms());
		shader.viewMatrix.loadMatrix(camera.getViewMatrix());

		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);

		entity.getModel().getModel().unbind(new int[]{0, 1, 2, 3, 4});

		shader.stopProgram();
	}

	/**
	 * Deletes the shader program when the game closes.
	 */
	public void free() {
		shader.free();
	}

	/**
	 * Starts the shader program and loads up the projection view matrix, as
	 * well as the light direction. Enables and disables a few settings which
	 * should be pretty self-explanatory.
	 * 
	 * @param entity
	 *            - the entity being prepared
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
	private void prepare(AnimatedEntity entity, Vector3f lightDir) {
		shader.transformationMatrix.loadMatrix(Mathematics.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale()));
		shader.lightDirection.loadVec3(lightDir);

		OpenGLUtilities.antialias(true);
		OpenGLUtilities.disableBlending();
		OpenGLUtilities.enableDepthTesting(true);
	}
}
