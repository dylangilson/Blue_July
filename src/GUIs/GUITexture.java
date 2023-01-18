/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * March 23, 2020
 */

package GUIs;

import Engine.Main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

public class GUITexture {

    private int textureID;
    private Vector2f position; // centre of quad
    private Vector2f scale; // x and y size of quad in relation to size of screen
    private float rotation; // most GUIs will not rotate, but for example, the compass will
    private boolean mirrorHorizontally = false;

    public GUITexture(int textureID, Vector2f position, Vector2f scale, float rotation, boolean mirrorHorizontally) {
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.mirrorHorizontally = mirrorHorizontally;

        Main.GUIS.add(this);
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean getMirrorHorizontally() {
        return mirrorHorizontally;
    }

    public void calculateRotation() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            float angleChange = 2f;
            rotation += angleChange;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            float angleChange = 2f;
            rotation -= angleChange;
        }
    }
}
