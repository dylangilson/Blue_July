/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 31, 2021
 */

package Entities;

import Animation.AnimatedModel;
import Engine.Main;

import org.lwjgl.util.vector.Vector3f;

public class FriendlyNPC extends NPC {

    public FriendlyNPC(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale, int textureIndex, MovementType movementType) {
        super(name, model, position, velocity, rotation, scale, textureIndex, movementType, EntityID.NPC);

        Main.NPCS.add(this);
        Main.FRIENDLY_NPCS.add(this);
    }
}
