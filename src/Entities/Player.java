/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 15, 2021
 */

package Entities;

import Animation.AnimatedModel;
import Engine.Delay;
import Engine.Equipment;
import Engine.Friends;
import Engine.Ignore;
import Engine.Inventory;
import Engine.Stats;
import RenderEngine.DisplayManager;
import Terrains.Terrain;
import Utilities.GlobalConstants;
import Utilities.Physics;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

// TODO give player XP

public class Player extends AnimatedEntity {

    // scaling constants
    public static final float REGULAR_SCALE = 0.66f; // cowboy scale
    public static final float MINIMAP_SCALE = 2f;
    public static final float HEALTH_BAR_OFFSET = 6.5f;

    // inventory constants
    public static final int INVENTORY_SIZE = 28;
    public static final int NUMBER_OF_ROWS_INVENTORY = 7;
    public static final int NUMBER_OF_COLUMNS_INVENTORY = 4;
    public static final int INITIAL_X_POSITION_INVENTORY = 965;
    public static final int NUMBER_OF_PIXELS_BETWEEN_INVENTORY_SLOT_X = 70;
    public static final int INITIAL_Y_POSITION_INVENTORY = 370;
    public static final int NUMBER_OF_PIXELS_BETWEEN_INVENTORY_SLOT_Y = 45;

    public String username; // change and move to appropriate account file
    private float attackRange;
    private Delay attackDelay;
    private Inventory inventory;
    private Stats stats;
    private Equipment equipment;
    private Friends friends;
    private Ignore ignore;
    private NPC target;

    public Player(String username, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale, int textureIndex) {
        super(username, model, position, velocity, rotation, scale, textureIndex, EntityID.PLAYER);
        super.setHealthBarOffset(HEALTH_BAR_OFFSET);

        this.rotateNorth = GlobalConstants.ROTATE_NORTH_COWBOY;
        this.rotateEast = GlobalConstants.ROTATE_EAST_COWBOY;
        this.rotateSouth = GlobalConstants.ROTATE_SOUTH_COWBOY;
        this.rotateWest = GlobalConstants.ROTATE_WEST_COWBOY;

        this.attackRange = GlobalConstants.MELEE_ATTACK_RANGE;
        this.attackDelay = new Delay(GlobalConstants.NORMAL_DELAY_LENGTH);
        this.attackDelay.end();
        this.inventory = new Inventory(this, INVENTORY_SIZE, NUMBER_OF_ROWS_INVENTORY, NUMBER_OF_COLUMNS_INVENTORY, INITIAL_X_POSITION_INVENTORY,
                NUMBER_OF_PIXELS_BETWEEN_INVENTORY_SLOT_X, INITIAL_Y_POSITION_INVENTORY, NUMBER_OF_PIXELS_BETWEEN_INVENTORY_SLOT_Y);
        this.stats = new Stats(true);
        this.equipment = new Equipment(this);
        this.friends = new Friends();
        this.ignore = new Ignore();
        this.target = null;

        // TODO remove this as its just for test
        this.stats.getStrength().updateTemporaryLevel(2, false);
        this.stats.getHitpoints().addXP(10000);
        this.stats.getHitpoints().updateTemporaryLevel(-5, false);
    }

    // this method gets called every frame
    public void update() {
        super.update();

        if (this.stats.getHitpoints().getTemporaryLevel() == 0) {
            death();
        }

        if (target != null) {
            if (Physics.calculateDistanceBetweenEntities(target, this) <= attackRange) {
                if (attackDelay.isOver()) {
                    attack((AggressiveNPC)target);
                    attackDelay.start();
                }
            }
        }
    }

    public void attack(AggressiveNPC target) {
        faceTarget(target.getPosition());

        if (Physics.calculateDistanceBetweenEntities(target, this) > attackRange) {
            return;
        }

        target.getStats().getHitpoints().updateTemporaryLevel(-calculateAttackDamage(), false);
        restartAttackDelay();
    }

    public void restartAttackDelay() {
        attackDelay.start();
    }

    // TODO add a more random and intelligent damage calculation
    public int calculateAttackDamage() {
        return this.stats.getStrength().getTemporaryLevel();
    }

    // this method uses the keyboard as input
    public void move(Terrain terrain) {
        checkInputs();

        super.increaseRotation(new Vector3f(0, currentTurnSpeed * DisplayManager.getFrameTimeInSeconds(), 0));

        // out of bounds checks
        if (super.getPosition().x < 2) {
            super.getPosition().x = 3;
        }
        if (super.getPosition().x > 798) {
            super.getPosition().x = 797;
        }
        if (super.getPosition().z > -2) {
            super.getPosition().z = -3;
        }
        if (super.getPosition().z < -798) {
            super.getPosition().z = -797;
        }

        float distance = currentSpeed * DisplayManager.getFrameTimeInSeconds();
        float Dx = (float)(distance * Math.sin((Math.toRadians(super.getRotation().y))));
        float Dz = (float)(distance * Math.cos((Math.toRadians(super.getRotation().y))));
        super.increasePosition(Dx, 0, Dz);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

        upwardsSpeed += GlobalConstants.GRAVITY * DisplayManager.getFrameTimeInSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeInSeconds(), 0);

        if (super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.currentSpeed = GlobalConstants.PLAYER_WALK_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.currentSpeed = -GlobalConstants.PLAYER_WALK_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentTurnSpeed = GlobalConstants.TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentTurnSpeed = -GlobalConstants.TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }
    }

    public boolean addItem(Item item) {
        return this.inventory.addItem(item);
    }

    // TODO implement death mechanic
    public void death() {
         this.getStats().reset();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Stats getStats() {
        return stats;
    }

    public Friends getFriends() {
        return friends;
    }

    public Ignore getIgnore() {
        return ignore;
    }

    public NPC getTarget() {
        return target;
    }

    public void setTarget(NPC target) {
        this.target = target;
    }
}
