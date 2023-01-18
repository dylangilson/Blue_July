/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 31, 2021
 */

package Entities;

import Animation.AnimatedModel;
import Utilities.Physics;

import org.lwjgl.util.vector.Vector3f;

// TODO don't let NPCs leave their bounding volume

public class NPC extends AnimatedEntity {

    public static final float LOOK_DISTANCE = 10f;

    protected Player target;

    public enum MovementType {
        STAND_STILL, RANDOM
    }

    private MovementType movementType;

    public NPC(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale,
               int textureIndex, MovementType movementType, EntityID entityID) {
        super(name, model, position, velocity, rotation, scale, textureIndex, entityID);

        this.movementType = movementType;
    }

    public void update() {
        super.update();

        move();
    }

    public void idle() {
        // TODO stand still for 4? game ticks
    }

    protected void look(Player player) {
        if (Physics.calculateDistanceBetweenEntities(player, this) < LOOK_DISTANCE) {
            if (player.getTarget() == null) {
                target = player;
                player.setTarget(this);
            }
        }
    }



    // moves the NPC using the given movement type
    // NOTE: if a custom movement style is needed for a given NPC, override this method in the respective class
    public void move() {
        if (movementType == MovementType.STAND_STILL) {
            idle();
        } else if (movementType == MovementType.RANDOM) {
            // TODO either move randomly OR stand still (roll a random number x, each frame and if x % y = 0 -> move randomly, where y is a sufficiently large number)
        }
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
}
