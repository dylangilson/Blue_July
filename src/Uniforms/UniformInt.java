/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * July 3, 2021
 */

package Uniforms;

import org.lwjgl.opengl.GL20;

public class UniformInt extends Uniform {

    private int currentValue;
    private boolean used = false;

    public UniformInt(String name) {
        super(name);
    }

    public void loadInt(int value) {
        if (!used || currentValue != value) {
            GL20.glUniform1i(super.getLocation(), value);
            used = true;
            currentValue = value;
        }
    }
}