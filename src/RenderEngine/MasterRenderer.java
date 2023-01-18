/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 6, 2021
 */

package RenderEngine;

import Engine.Main;
import Entities.Camera;
import Entities.InanimateEntity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.TerrainShader;
import Terrains.Terrain;
import Utilities.GlobalConstants;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    public static final float FOV = 70; // field of view
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;
    public static final float ENTITY_BRIGHTNESS = 0.75f;
    public static final float TERRAIN_BRIGHTNESS = 0.4f;

    private Matrix4f projectionMatrix;
    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();
    private Map<TexturedModel, List<InanimateEntity>> inanimateEntities = new HashMap<TexturedModel, List<InanimateEntity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();
    private SkyboxRenderer skyboxRenderer;
    private ShadowMapMasterRenderer shadowMapRenderer;
    private AnimatedModelRenderer animatedModelRenderer;

    public MasterRenderer(Camera camera) {
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        shadowMapRenderer = new ShadowMapMasterRenderer(camera);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix, shadowMapRenderer.getShadowDistance());
        skyboxRenderer = new SkyboxRenderer(projectionMatrix);
        animatedModelRenderer = new AnimatedModelRenderer(projectionMatrix);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void renderScene(Terrain terrain, List<Light> lights, Camera camera, Vector4f clipPlane) {
        processTerrain(terrain);

        for (int i = 0; i < Main.INANIMATE_ENTITIES.size(); i++) {
            processEntity(Main.INANIMATE_ENTITIES.get(i));
        }

        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        // Entity Rendering
        prepare();
        shader.startProgram();

        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(GlobalConstants.NORMAL_COLOUR);

        shader.loadLights(lights);
        shader.loadViewMatrix(camera);

        entityRenderer.render(inanimateEntities);

        // Animated Model Rendering
        for (int i = 0; i < Main.ANIMATED_ENTITIES.size(); i++) {
            animatedModelRenderer.render(Main.ANIMATED_ENTITIES.get(i), camera);
        }

        shader.stopProgram();

        // Terrain Rendering
        terrainShader.startProgram();

        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(GlobalConstants.NORMAL_COLOUR);

        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);

        terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());

        terrainShader.stopProgram();

        // Skybox Rendering
        skyboxRenderer.render(camera, GlobalConstants.SKY_BLUE);

        // Clean up
        terrains.clear();
        inanimateEntities.clear();
    }

    public void processEntity(InanimateEntity entity) {
        TexturedModel entityModel = entity.getModel();
        List<InanimateEntity> batch = inanimateEntities.get(entityModel);

        if (batch != null) {
            batch.add(entity);
        } else {
            List<InanimateEntity> newBatch = new ArrayList<InanimateEntity>();
            newBatch.add(entity);
            inanimateEntities.put(entityModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void renderShadowMap(Light sun) {
        for (int i = 0; i < Main.INANIMATE_ENTITIES.size(); i++) {
            processEntity(Main.INANIMATE_ENTITIES.get(i));
        }

        shadowMapRenderer.render(inanimateEntities, Main.ANIMATED_ENTITIES, sun);
        inanimateEntities.clear();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(GlobalConstants.NORMAL_COLOUR.x, GlobalConstants.NORMAL_COLOUR.y, GlobalConstants.NORMAL_COLOUR.z, 1); // black
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL13.glActiveTexture(GL13.GL_TEXTURE5); // 5 -> shadowMap
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f();

        float aspectRatio = (float) Display.getWidth() / (float)Display.getHeight();
        float yScale = (float)(1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return  projectionMatrix;
    }

    public int getShadowMapTexture() {
        return shadowMapRenderer.getShadowMap();
    }

    public void free() {
        shader.free();
        terrainShader.free();
        shadowMapRenderer.free();
    }
}
