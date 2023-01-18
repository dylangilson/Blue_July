/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * March 23, 2020
 */

package Shaders;

import Uniforms.Uniform;
import Uniforms.UniformBoolean;
import Uniforms.UniformMatrix;
import Utilities.InternalJarFile;

import org.lwjgl.util.vector.Matrix4f;

public class GUIShader extends ShaderProgram {

    private static final InternalJarFile VERTEX_SHADER = new InternalJarFile("res/Shaders/GUIVertexShader.glsl");
    private static final InternalJarFile FRAGMENT_SHADER = new InternalJarFile("res/Shaders/GUIFragmentShader.glsl");
    private static final String[] IN_VARIABLES = {"position"};
    private static final int[] IN_INDICES = {0};

    private UniformBoolean mirrorHorizontally = new UniformBoolean("mirrorHorizontally");
    private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");

    public GUIShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, IN_VARIABLES, IN_INDICES);

        super.storeAllUniformLocations(new Uniform[]{mirrorHorizontally, transformationMatrix});
    }

    public void loadMirrorHorizontally(boolean value) {
        mirrorHorizontally.loadBoolean(value);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        transformationMatrix.loadMatrix(matrix);
    }
}
