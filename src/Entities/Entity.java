/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 26, 2020
 */

package Entities;

import RenderEngine.DisplayManager;
import Terrains.Terrain;
import Utilities.GlobalConstants;

import org.lwjgl.util.vector.Vector3f;

public abstract class Entity {

    // map constants
    public static final float MIN_POSITION = 2;
    public static final float MAX_POSITION = GlobalConstants.MAP_SIZE - MIN_POSITION;
    public static final float POSITION_THRESHOLD = 0.5f;
    public static final int ROTATION_FACTOR = 10;


    private String name;
    private Vector3f position;
    private Vector3f velocity;
    private Vector3f rotation;
    private float scale;
    public int textureIndex = 0; // index of the texture atlas that this should bind to; 0 -> not an atlas
    protected float currentSpeed = 0;
    protected float currentTurnSpeed = 0;
    protected float upwardsSpeed = 0;
    protected boolean isInAir = false;
    protected boolean isMoving = false;

    public Vector3f rotateNorth;
    public Vector3f rotateEast;
    public Vector3f rotateSouth;
    public Vector3f rotateWest;

    public enum EntityID {
        PLAYER, NPC, ITEM, ENVIRONMENT
    }

    public EntityID entityID;

    public Entity(String name, Vector3f position, Vector3f velocity, Vector3f rotation, float scale, int textureIndex, EntityID entityID) {
        this.name = name;
        this.position = position;
        this.velocity = velocity;
        this.rotation = rotation;
        this.scale = scale;
        this.textureIndex = textureIndex;
        this.entityID = entityID;
    }

    // this method uses the mouse as input
    public void moveTo(Vector3f position, Terrain terrain) {
        float walkSpeed = getWalkSpeed();

        // out of bounds checks
        if (position.x < MIN_POSITION) {
            position.x = MIN_POSITION + 1;
        }
        if (position.x > MAX_POSITION) {
            position.x = MAX_POSITION - 1;
        }
        if (position.z > -MIN_POSITION) {
            position.z = -MIN_POSITION - 1;
        }
        if (position.z < -MAX_POSITION) {
            position.z = -MAX_POSITION + 1;
        }

        // calculate position
        if (this.position.x < position.x) {
            increasePosition(walkSpeed * DisplayManager.getFrameTimeInSeconds(), 0, 0);
            if (Math.abs(this.position.x - position.x) > POSITION_THRESHOLD) {
                isMoving = true;
            }
        }
        if (this.position.z < position.z) {
            increasePosition(0, 0, walkSpeed * DisplayManager.getFrameTimeInSeconds());
            if (Math.abs(this.position.z - position.z) > POSITION_THRESHOLD) {
                isMoving = true;
            }
        }
        if (this.position.x > position.x) {
            increasePosition(-walkSpeed * DisplayManager.getFrameTimeInSeconds(), 0, 0);
            if (Math.abs(this.position.x - position.x) > POSITION_THRESHOLD) {
                isMoving = true;
            }
        }
        if (this.position.z > position.z) {
            increasePosition(0, 0, -walkSpeed * DisplayManager.getFrameTimeInSeconds());
            if (Math.abs(this.position.z - position.z) > POSITION_THRESHOLD) {
                isMoving = true;
            }
        }

        if (Math.abs(this.position.x - position.x) < POSITION_THRESHOLD && Math.abs(this.position.z - position.z) < POSITION_THRESHOLD) {
            isMoving = false;
        }

        // ensure entity is on ground, or moving towards it
        float terrainHeight = terrain.getHeightOfTerrain(this.position.x, this.position.z);

        upwardsSpeed += GlobalConstants.GRAVITY * DisplayManager.getFrameTimeInSeconds();
        increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeInSeconds(), 0);
        if (this.position.y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            this.position.y = terrainHeight;
        }
    }

    public float getWalkSpeed() {
        if (entityID == EntityID.PLAYER) {
            return GlobalConstants.PLAYER_WALK_SPEED;
        } else if (entityID == EntityID.NPC) {
            return GlobalConstants.NPC_WALK_SPEED;
        }

        return 0;
    }

    public int calculateAngleDirection(Vector3f position) {
        float deltaX = (float)Math.pow(this.position.x - position.x, 2); // ||x||
        float deltaZ = (float)Math.pow(this.position.z - position.z, 2); // ||z||

        return deltaX > deltaZ ? 0 : 1; // 0 indicates x vector has more magnitude; 1 is the opposite
    }

    public void faceTarget(Vector3f position) {
        int angleDirection = calculateAngleDirection(position);

        if (angleDirection == 0) {
            if (position.x < this.getPosition().x) {
                this.setRotation(this.rotateWest);
            } else {
                this.setRotation(this.rotateEast);
            }
        } else {
            if (position.z < this.getPosition().z) {
                this.setRotation(rotateNorth);
            } else {
                this.setRotation(rotateSouth);
            }
        }
    }

    public void increasePosition(float Dx, float Dy, float Dz) {
        this.position.x += Dx;
        this.position.y += Dy;
        this.position.z += Dz;
    }

    public void increaseRotation(Vector3f rotation) {
        this.rotation.x += rotation.x;
        this.rotation.y += rotation.y;
        this.rotation.z += rotation.z;
    }

    public String getName() {
        return this.name;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }
}
