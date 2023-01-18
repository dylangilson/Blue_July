/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 22, 2021
 */

package RenderEngine;

import java.util.List;

import Engine.Main;
import Entities.Camera;
import Entities.Light;
import Models.RawModel;
import Shaders.WaterShader;
import Utilities.Mathematics;
import Water.WaterFrameBuffers;
import Water.WaterTile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class WaterRenderer {

	private static final String DUDV_MAP = "waterDUDVMap";
	private static final String NORMAL_MAP = "waterNormalMap";
	private static final float WAVE_SPEED = 0.03f;

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers waterFBOs;
	private int dudvMapTextureID;
	private int normalMapTextureID;
	private float moveFactor = 0;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers waterFBOs) {
		this.shader = shader;
		this.waterFBOs = waterFBOs;
		dudvMapTextureID = Main.LOADER.loadTexture(DUDV_MAP, "Terrain Elements");
		normalMapTextureID = Main.LOADER.loadTexture(NORMAL_MAP, "Terrain Elements");
		shader.startProgram();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stopProgram();
		setUpVAO();
	}

	public void render(List<WaterTile> water, Camera camera, Light sun) {
		prepareRender(camera, sun);

		for (int i = 0; i < water.size(); i++) {
			WaterTile tile = water.get(i);

			Matrix4f modelMatrix = Mathematics.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), new Vector3f(0, 0, 0), WaterTile.TILE_SIZE);

			shader.loadModelMatrix(modelMatrix);

			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}

		unbind();
	}
	
	private void prepareRender(Camera camera, Light sun) {
		shader.startProgram();

		shader.loadViewMatrix(camera);

		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeInSeconds();
		moveFactor %= 1; // reset value after it becomes >= 1
		shader.loadMoveFactor(moveFactor);

		shader.loadLight(sun);

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFBOs.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFBOs.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvMapTextureID);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMapTextureID);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterFBOs.getRefractionDepthTexture());

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stopProgram();
	}

	private void setUpVAO() {
		// Just x and z vertex positions here ; y is set to 0 in vertex shader
		float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
		quad = Main.LOADER.loadQuadToVAO(vertices);
	}
}
