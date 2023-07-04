/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 15, 2021
 */

package RenderEngine;

import Engine.Main;
import Entities.Camera;
import Models.RawModel;
import Particles.Particle;
import Particles.ParticleTexture;
import Shaders.ParticleShader;
import Utilities.Mathematics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 100000; // maximum number of particles allowed in entire scene
    private static final int INSTANCE_DATA_LENGTH = 21; // number of floats needed to store all attribute data for each particle
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private RawModel quad;
	private ParticleShader shader;
	private int vboID;
	private int pointer = 0; // index of float array currently being written to
	
	public ParticleRenderer(Matrix4f projectionMatrix) {
	    this.vboID = Main.LOADER.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

		quad = Main.LOADER.loadQuadToVAO(VERTICES);

		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 1, 4, INSTANCE_DATA_LENGTH, 0); // column A of modelViewMatrix
		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 2, 4, INSTANCE_DATA_LENGTH, 4); // column B of modelViewMatrix
		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 3, 4, INSTANCE_DATA_LENGTH, 8); // column C of modelViewMatrix
		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 4, 4, INSTANCE_DATA_LENGTH, 12); // column D of modelViewMatrix
		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 5, 4, INSTANCE_DATA_LENGTH, 16); // texture offsets
		Main.LOADER.addInstancedAttribute(quad.getVaoID(), vboID, 6, 1, INSTANCE_DATA_LENGTH, 20); // blend factor

		shader = new ParticleShader();
		shader.startProgram();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stopProgram();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = Mathematics.createViewMatrix(camera);
		prepareRender();

		for (ParticleTexture texture : particles.keySet()) {
            bindTexture(texture);
            List<Particle> particleList = particles.get(texture);

            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];

            for (int i = 0; i < particleList.size(); i++) {
            	Particle particle = particleList.get(i);

				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScaleX(), particle.getScaleY(), viewMatrix, vboData);
				updateTextureCoordInfo(particle, vboData);
			}

			Main.LOADER.updateVBO(vboID, vboData, buffer);

            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
		}

		finishRendering();
	}

	private void bindTexture(ParticleTexture texture) {
        if (texture.usesAdditiveBlending()) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID()); // bind texture

        shader.loadNumberOfRows(texture.getNumberOfRows());
    }

	private void updateModelViewMatrix(Vector3f position, float rotation, float scaleX, float scaleY, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(position, modelMatrix, modelMatrix);

		// ensures that particle always faces camera
		// set the top-left 3x3 matrix of the modelMatrix to the transpose of the top-left 3x3 matrix of the viewMatrix
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;

		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scaleX, scaleY, scaleX), modelMatrix, modelMatrix);

		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);

		storeMatrixData(modelViewMatrix, vboData);
	}

	private void storeMatrixData(Matrix4f modelViewMatrix, float[] vboData) {
        vboData[pointer++] = modelViewMatrix.m00;
        vboData[pointer++] = modelViewMatrix.m01;
        vboData[pointer++] = modelViewMatrix.m02;
        vboData[pointer++] = modelViewMatrix.m03;
        vboData[pointer++] = modelViewMatrix.m10;
        vboData[pointer++] = modelViewMatrix.m11;
        vboData[pointer++] = modelViewMatrix.m12;
        vboData[pointer++] = modelViewMatrix.m13;
        vboData[pointer++] = modelViewMatrix.m20;
        vboData[pointer++] = modelViewMatrix.m21;
        vboData[pointer++] = modelViewMatrix.m22;
        vboData[pointer++] = modelViewMatrix.m23;
        vboData[pointer++] = modelViewMatrix.m30;
        vboData[pointer++] = modelViewMatrix.m31;
        vboData[pointer++] = modelViewMatrix.m32;
        vboData[pointer++] = modelViewMatrix.m33;
    }

    private void updateTextureCoordInfo(Particle particle, float[] data) {
	    data[pointer++] = particle.getTextureOffset1().x;
        data[pointer++] = particle.getTextureOffset1().y;
        data[pointer++] = particle.getTextureOffset2().x;
        data[pointer++] = particle.getTextureOffset2().y;
        data[pointer++] = particle.getBlendFactor();
    }
	
	private void prepareRender() {
		shader.startProgram();

		GL30.glBindVertexArray(quad.getVaoID());

		GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);

		GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);

		GL30.glBindVertexArray(0); // unbind VAO

		shader.stopProgram();
	}

	public void free() {
		shader.free();
	}
}
