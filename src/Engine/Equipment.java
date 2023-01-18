/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 2, 2021
 */

package Engine;

import Entities.Item;
import Entities.Player;

// TODO use equipment pane to display equipment
// TODO make equipment altar stats

public class Equipment {

    public enum EquipmentSlot {
        HELMET, CAPE, NECKLACE, QUIVER, SWORD, BODY, SHIELD, LEGS, GLOVES, BOOTS, RING, NONE
    }

    public static final int EQUIPMENT_COUNT = 11;

    private Player player;
    private Item[] items; // these items will always be equipable items

    public Equipment(Player player) {
        this.player = player;
        this.items = new Item[EQUIPMENT_COUNT];
    }

    public void equip(Item item) {
        if (items[item.getEquipmentSlotInteger()] == null) {
            items[item.getEquipmentSlotInteger()] = item;
            return;
        }

        // put the currently worn equipment back into inventory
        player.getInventory().addItem(items[item.getEquipmentSlotInteger()]);
        items[item.getEquipmentSlotInteger()] = item;
    }
}
