/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 29, 2022
 */

package Entities;

import Engine.Main;
import Models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class InanimateEntity extends Entity {

    private TexturedModel model;

    public InanimateEntity(String name, TexturedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale, int textureIndex, EntityID entityID) {
        super(name, position, velocity, rotation, scale, textureIndex, entityID);
        this.model = model;

        Main.ENTITIES.add(this);
        // Main.INANIMATE_ENTITIES.add(this);
    }

    public float getTextureOffsetX() {
        int column = textureIndex % model.getTexture().getNumberOfColumns();
        return (float)column / model.getTexture().getNumberOfColumns();
    }

    public float getTextureOffsetY() {
        int row = textureIndex / model.getTexture().getNumberOfColumns();
        return (float)row / model.getTexture().getNumberOfColumns();
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }
}
