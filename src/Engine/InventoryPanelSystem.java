/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 4, 2022
 */

package Engine;

import Entities.Player;
import Utilities.MousePicker;
import Utilities.MousePicker.InventoryPanel;

import org.lwjgl.util.vector.Vector2f;

public class InventoryPanelSystem {

    public void displayPanel(Player player) {
        MousePicker.InventoryPanel panel = Main.INVENTORY_PANEL;

        // TODO implement this
        if (panel == InventoryPanel.INVENTORY) {
            for (int i = 0; i < player.getInventory().getItems().length; i++) {
                if (player.getInventory().getItem(i) != null) {
                    player.getInventory().getItem(i).loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(i),
                            player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(i)));
                }
            }
        } else if (panel == InventoryPanel.COMBAT) {

        } else if (panel == InventoryPanel.LEVELS) {

        } else if (panel == InventoryPanel.QUESTS) {

        } else if (panel == InventoryPanel.EQUIPMENT) {

        } else if (panel == InventoryPanel.PRAYER) {

        } else if (panel == InventoryPanel.MAGIC) {

        } else if (panel == InventoryPanel.FRIENDS) {

        } else if (panel == InventoryPanel.IGNORES) {

        } else if (panel == InventoryPanel.LOGOUT) {

        } else if (panel == InventoryPanel.SETTINGS) {

        } else if (panel == InventoryPanel.EMOTES) {

        } else if (panel == InventoryPanel.MUSIC) {

        }

        clearPanel(player, panel);
    }

    private void clearPanel(Player player, InventoryPanel panel) {
        if (panel != InventoryPanel.INVENTORY) {
            player.getInventory().clearPanel();
        }
        if (panel != InventoryPanel.COMBAT) {

        }
        if (panel != InventoryPanel.LEVELS) {

        }
        if (panel != InventoryPanel.QUESTS) {

        }
        if (panel != InventoryPanel.EQUIPMENT) {

        }
        if (panel != InventoryPanel.PRAYER) {

        }
        if (panel != InventoryPanel.MAGIC) {

        }
        if (panel != InventoryPanel.FRIENDS) {

        }
        if (panel != InventoryPanel.IGNORES) {

        }
        if (panel != InventoryPanel.LOGOUT) {

        }
        if (panel != InventoryPanel.SETTINGS) {

        }
        if (panel != InventoryPanel.EMOTES) {

        }
        if (panel != InventoryPanel.MUSIC) {

        }
    }
}
