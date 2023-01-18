/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 14, 2022
 */

package Enemies;

import Animation.AnimatedModel;
import Engine.Equipment;
import Entities.AggressiveNPC;
import Entities.Item;
import Entities.NPC;
import Utilities.GlobalConstants;

import org.lwjgl.util.vector.Vector3f;

public class GreaterDemon extends AggressiveNPC {

    public static final float REGULAR_SCALE3D = 0.02f; // greater demon scale
    public static final float HEALTH_BAR_OFFSET = 9.5f;

    public GreaterDemon(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale,
                        int textureIndex, NPC.MovementType movementType, boolean isAggressive, AggressiveNPC.AttackType attackType, int dropTableSize) {
        super(name, model, position, velocity, rotation, scale, textureIndex, movementType, isAggressive, attackType, GlobalConstants.NORMAL_DELAY_LENGTH, dropTableSize);
        super.setHealthBarOffset(HEALTH_BAR_OFFSET);

        this.rotateNorth = GlobalConstants.ROTATE_NORTH_COWBOY;
        this.rotateEast = GlobalConstants.ROTATE_EAST_COWBOY;
        this.rotateSouth = GlobalConstants.ROTATE_SOUTH_COWBOY;
        this.rotateWest = GlobalConstants.ROTATE_WEST_COWBOY;

        initializeDropTable();

        this.getStats().getAttack().addXP(1000);
    }

    @Override
    public void initializeDropTable() {
        this.dropTable.items[0] = new Item(0, 0, 1000000, true, Equipment.EquipmentSlot.SWORD,
                "Abyssal Whip", Item.ABYSSAL_WHIP, getPosition(), new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0), Item.ABYSSAL_WHIP_SCALE, 0.05f, 0);
        this.dropTable.dropChances[0] = 100;

        this.dropTable.calculateTotalWeight();
    }
}
