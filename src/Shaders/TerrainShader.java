/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 7, 2021
 */

package Shaders;

import Entities.Camera;
import Entities.Light;
import Uniforms.Uniform;
import Uniforms.UniformFloat;
import Uniforms.UniformMatrix;
import Uniforms.UniformSampler;
import Uniforms.UniformVec3;
import Uniforms.UniformVec4;
import Utilities.GlobalConstants;
import Utilities.InternalJarFile;
import Utilities.Mathematics;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class TerrainShader extends ShaderProgram {

    private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/TerrainVertexShader.glsl");
    private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/TerrainFragmentShader.glsl");
    private static final String[] IN_VARIABLES = {"position", "textureCoords", "normal"};
    private static final int[] IN_INDICES = {0, 1, 2};

    private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
    private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    private UniformFloat shineDamper = new UniformFloat("shineDamper");
    private UniformFloat reflectivity = new UniformFloat("reflectivity");
    private UniformVec3 skyColour = new UniformVec3("skyColour");
    private UniformSampler backgroundTexture = new UniformSampler("backgroundTexture");
    private UniformSampler redTexture = new UniformSampler("redTexture");
    private UniformSampler greenTexture = new UniformSampler("greenTexture");
    private UniformSampler blueTexture = new UniformSampler("blueTexture");
    private UniformSampler blendMap = new UniformSampler("blendMap");
    private UniformVec4 plane = new UniformVec4("plane");
    private UniformMatrix toShadowMapSpace = new UniformMatrix("toShadowMapSpace");
    private UniformSampler shadowMap = new UniformSampler("shadowMap");
    private UniformFloat shadowDistance = new UniformFloat("shadowDistance");
    private UniformFloat shadowMapSize = new UniformFloat("shadowMapSize");
    private UniformFloat brightness = new UniformFloat("brightness");
    private int[] location_lightPosition;
    private int[] location_lightColour;
    private int[] location_attenuation;

    public TerrainShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

        super.storeAllUniformLocations(new Uniform[]{transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity, skyColour, backgroundTexture, redTexture,
                greenTexture, blueTexture, blendMap, plane, toShadowMapSpace, shadowMap, shadowDistance, shadowMapSize, brightness});
        setArrayUniformLocations();
    }

    public void connectTextureUnits() {
        backgroundTexture.loadTexUnit(0);
        redTexture.loadTexUnit(1);
        greenTexture.loadTexUnit(2);
        blueTexture.loadTexUnit(3);
        blendMap.loadTexUnit(4);
        shadowMap.loadTexUnit(5);
    }

    public void setArrayUniformLocations() {
        location_lightPosition = new int[GlobalConstants.MAX_LIGHTS];
        location_lightColour = new int[GlobalConstants.MAX_LIGHTS];
        location_attenuation = new int[GlobalConstants.MAX_LIGHTS];

        for (int i = 0; i < GlobalConstants.MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadBrightness(float value) {
        brightness.loadFloat(value);
    }

    public void loadShadowMapSize(float size) {
        shadowMapSize.loadFloat(size);
    }

    public void loadShadowDistance(float distance) {
        shadowDistance.loadFloat(distance);
    }

    public void loadToShadowSpaceMatrix(Matrix4f matrix) {
        toShadowMapSpace.loadMatrix(matrix);
    }

    public void loadClipPlane(Vector4f value) {
        plane.loadVec4(value);
    }

    public void loadSkyColour(Vector3f colour) {
        skyColour.loadVec3(colour);
    }

    public void loadShineVariables(float shine, float reflectiveness) {
        shineDamper.loadFloat(shine);
        reflectivity.loadFloat(reflectiveness);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        transformationMatrix.loadMatrix(matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        projectionMatrix.loadMatrix(matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Mathematics.createViewMatrix(camera);
        viewMatrix.loadMatrix(matrix);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < GlobalConstants.MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.load3DVector(location_lightPosition[i], lights.get(i).getPosition());
                super.load3DVector(location_lightColour[i], lights.get(i).getColour());
                super.load3DVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                // default values (no effect)
                super.load3DVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }
}
