/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 4, 2022
 */

package Engine;

import Entities.Player;
import Utilities.MousePicker;
import Utilities.MousePicker.HUDPanel;

import org.lwjgl.util.vector.Vector2f;

public class HUDPanelSystem {

    public void displayPanel(Player player) {
        MousePicker.HUDPanel panel = Main.INVENTORY_PANEL;

        // TODO implement this
        if (panel == HUDPanel.INVENTORY) {
            for (int i = 0; i < player.getInventory().getItems().length; i++) {
                if (player.getInventory().getItem(i) != null) {
                    player.getInventory().getItem(i).loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(i),
                            player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(i)));
                }
            }
        } else if (panel == HUDPanel.COMBAT) {

        } else if (panel == HUDPanel.LEVELS) {

        } else if (panel == HUDPanel.QUESTS) {

        } else if (panel == HUDPanel.EQUIPMENT) {

        } else if (panel == HUDPanel.PRAYER) {

        } else if (panel == HUDPanel.MAGIC) {

        } else if (panel == HUDPanel.FRIENDS) {

        } else if (panel == HUDPanel.IGNORES) {

        } else if (panel == HUDPanel.LOGOUT) {

        } else if (panel == HUDPanel.SETTINGS) {

        } else if (panel == HUDPanel.EMOTES) {

        } else if (panel == HUDPanel.MUSIC) {

        }

        clearPanel(player, panel);
    }

    private void clearPanel(Player player, HUDPanel panel) {
        if (panel != HUDPanel.INVENTORY) {
            player.getInventory().clearPanel();
        }
        if (panel != HUDPanel.COMBAT) {

        }
        if (panel != HUDPanel.LEVELS) {

        }
        if (panel != HUDPanel.QUESTS) {

        }
        if (panel != HUDPanel.EQUIPMENT) {

        }
        if (panel != HUDPanel.PRAYER) {

        }
        if (panel != HUDPanel.MAGIC) {

        }
        if (panel != HUDPanel.FRIENDS) {

        }
        if (panel != HUDPanel.IGNORES) {

        }
        if (panel != HUDPanel.LOGOUT) {

        }
        if (panel != HUDPanel.SETTINGS) {

        }
        if (panel != HUDPanel.EMOTES) {

        }
        if (panel != HUDPanel.MUSIC) {

        }
    }
}
