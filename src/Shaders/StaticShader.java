/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Shaders;

import Entities.Camera;
import Entities.Light;
import Uniforms.Uniform;
import Uniforms.UniformBoolean;
import Uniforms.UniformFloat;
import Uniforms.UniformMatrix;
import Uniforms.UniformVec2;
import Uniforms.UniformVec3;
import Uniforms.UniformVec4;
import Utilities.GlobalConstants;
import Utilities.InternalJarFile;
import Utilities.Mathematics;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class StaticShader extends ShaderProgram {

    private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/VertexShader.glsl");
    private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/FragmentShader.glsl");
    private static final String[] IN_VARIABLES = {"position", "textureCoords", "normal"};
    private static final int[] IN_INDICES = {0, 1, 2};

    protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat shineDamper = new UniformFloat("shineDamper");
    protected UniformFloat reflectivity = new UniformFloat("reflectivity");
    protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
    protected UniformVec3 skyColour = new UniformVec3("skyColour");
    protected UniformFloat numberOfRows = new UniformFloat("numberOfRows");
    protected UniformVec2 atlasOffset = new UniformVec2("atlasOffset");
    protected UniformVec4 plane = new UniformVec4("plane");
    protected UniformFloat brightness = new UniformFloat("brightness");
    protected int[] location_lightPosition;
    protected int[] location_lightColour;
    protected int[] location_attenuation;

    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

        super.storeAllUniformLocations(new Uniform[]{transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity, useFakeLighting, skyColour, numberOfRows, atlasOffset, plane, brightness});
        setArrayUniformLocations();
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

    public void loadClipPlane(Vector4f value) {
        plane.loadVec4(value);
    }

    public void loadNumberOfRows(int value) {
        numberOfRows.loadFloat(value);
    }

    public void loadAtlasOffset(Vector2f offset) {
        atlasOffset.loadVec2(offset);
    }

    public void loadSkyColour(Vector3f colour) {
        skyColour.loadVec3(colour);
    }

    public void loadFakeLightingVariable(boolean value) {
        useFakeLighting.loadBoolean(value);
    }

    public void loadShineVariables(float shine, float reflectiveness) {
        shineDamper.loadFloat(shine);
        reflectivity.loadFloat(reflectiveness);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        transformationMatrix.loadMatrix(matrix);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < GlobalConstants.MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.load3DVector(location_lightPosition[i], lights.get(i).getPosition());
                super.load3DVector(location_lightColour[i], lights.get(i).getColour());
                super.load3DVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                // default values (no effect)
                super.load3DVector(location_lightPosition[i], new Vector3f(0,0,0));
                super.load3DVector(location_lightColour[i], new Vector3f(0,0,0));
                super.load3DVector(location_attenuation[i], new Vector3f(1,0,0));
            }
        }
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        projectionMatrix.loadMatrix(matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Mathematics.createViewMatrix(camera);
        viewMatrix.loadMatrix(matrix);
    }
}
