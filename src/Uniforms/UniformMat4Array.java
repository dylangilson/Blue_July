/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.util.vector.Matrix4f;

public class UniformMat4Array extends Uniform {
	
	private UniformMatrix[] matrixUniforms;
	
	public UniformMat4Array(String name, int size) {
		super(name);

		matrixUniforms = new UniformMatrix[size];

		for (int i = 0; i < size; i++) {
			matrixUniforms[i] = new UniformMatrix(name + "[" + i + "]");
		}
	}
	
	@Override
	public void storeUniformLocation(int programID) {
		for (int i = 0; i < matrixUniforms.length; i++) {
			matrixUniforms[i].storeUniformLocation(programID);
		}
	}

	public void loadMatrixArray(Matrix4f[] matrices) {
		for (int i = 0; i < matrices.length; i++) {
			matrixUniforms[i].loadMatrix(matrices[i]);
		}
	}
}
