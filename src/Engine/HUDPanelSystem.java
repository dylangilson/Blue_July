/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 4, 2022
 */

package Engine;

import Entities.Player;
import GUIs.GUITexture;
import Utilities.MousePicker;
import Utilities.MousePicker.HUDPanel;

import org.lwjgl.util.vector.Vector2f;

public class HUDPanelSystem {

    private static final float INVENTORY_TEXTURE_SCALE_X = 0.06f;
    private static final float INVENTORY_TEXTURE_SCALE_Y = 0.08f;
    private static final float COMBAT_TEXTURE_SCALE_X = 0.05f;
    private static final float COMBAT_TEXTURE_SCALE_Y = 0.07f;
    private static final float LEVELS_TEXTURE_SCALE_X = 0.085f;
    private static final float LEVELS_TEXTURE_SCALE_Y = 0.118f;
    private static final float QUESTS_TEXTURE_SCALE_X = 0.093f;
    private static final float QUESTS_TEXTURE_SCALE_Y = 0.123f;
    private static final float EQUIPMENT_TEXTURE_SCALE_X = 0.086f;
    private static final float EQUIPMENT_TEXTURE_SCALE_Y = 0.115f;
    private static final float PRAYER_TEXTURE_SCALE_X = 0.083f;
    private static final float PRAYER_TEXTURE_SCALE_Y = 0.11f;
    private static final float MAGIC_TEXTURE_SCALE_X = 0.0855f;
    private static final float MAGIC_TEXTURE_SCALE_Y = 0.115f;
    private static final float FRIENDS_TEXTURE_SCALE_X = 0.086f;
    private static final float FRIENDS_TEXTURE_SCALE_Y = 0.112f;
    private static final float IGNORE_TEXTURE_SCALE_X = 0.086f;
    private static final float IGNORE_TEXTURE_SCALE_Y = 0.112f;
    private static final float LOGOUT_TEXTURE_SCALE_X = 0.085f;
    private static final float LOGOUT_TEXTURE_SCALE_Y = 0.1135f;
    private static final float SETTINGS_TEXTURE_SCALE_X = 0.086f;
    private static final float SETTINGS_TEXTURE_SCALE_Y = 0.1133f;
    private static final float EMOTES_TEXTURE_SCALE_X = 0.086f;
    private static final float EMOTES_TEXTURE_SCALE_Y = 0.1133f;
    private static final float MUSIC_TEXTURE_SCALE_X = 0.0865f;
    private static final float MUSIC_TEXTURE_SCALE_Y = 0.1162f;

    private GUITexture iconTexture;

    public void loadTexture(String name, Vector2f position, float textureScaleX, float textureScaleY) {
        int textureID =  Main.LOADER.loadTexture(name, "GUI Elements");
        this.iconTexture = new GUITexture(textureID, position, new Vector2f(textureScaleX, textureScaleY), 0, false);
    }

    public void removeTexture() {
        Main.GUIS.remove(this.iconTexture);
    }

    public void displayPanel(Player player) {
        MousePicker.HUDPanel panel = Main.HUD_PANEL;

        // TODO finish implementing panels
        if (panel == HUDPanel.INVENTORY) {
            player.getInventory().renderPanel();
            loadTexture("RedInventoryIcon", new Vector2f(0.721f, 0.128f), INVENTORY_TEXTURE_SCALE_X, INVENTORY_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.COMBAT) {
            loadTexture("RedCombatIcon", new Vector2f(0.47f, 0.127f), COMBAT_TEXTURE_SCALE_X, COMBAT_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.LEVELS) {
            player.getStats().renderPanel();
            loadTexture("RedLevelsIcon", new Vector2f(0.55f, 0.133f), LEVELS_TEXTURE_SCALE_X, LEVELS_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.QUESTS) {
            loadTexture("RedQuestIcon", new Vector2f(0.6235f, 0.135f), QUESTS_TEXTURE_SCALE_X, QUESTS_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.EQUIPMENT) {
            loadTexture("RedEquipmentIcon", new Vector2f(0.815f, 0.132f), EQUIPMENT_TEXTURE_SCALE_X, EQUIPMENT_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.PRAYER) {
            loadTexture("RedPrayerIcon", new Vector2f(0.888f, 0.135f), PRAYER_TEXTURE_SCALE_X, PRAYER_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.MAGIC) {
            loadTexture("RedMagicIcon", new Vector2f(0.965f, 0.135f), MAGIC_TEXTURE_SCALE_X, MAGIC_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.FRIENDS) {
            player.getFriends().renderPanel();
            loadTexture("RedFriendsIcon", new Vector2f(0.548f, -0.94f), FRIENDS_TEXTURE_SCALE_X, FRIENDS_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.IGNORES) {
            player.getIgnore().renderPanel();
            loadTexture("RedIgnoreIcon", new Vector2f(0.6225f, -0.945f), IGNORE_TEXTURE_SCALE_X, IGNORE_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.LOGOUT) {
            loadTexture("RedLogoutIcon", new Vector2f(0.72f, -0.945f), LOGOUT_TEXTURE_SCALE_X, LOGOUT_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.SETTINGS) {
            loadTexture("RedSettingsIcon", new Vector2f(0.815f, -0.94f), SETTINGS_TEXTURE_SCALE_X, SETTINGS_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.EMOTES) {
            loadTexture("RedEmotesIcon", new Vector2f(0.89f, -0.942f), EMOTES_TEXTURE_SCALE_X, EMOTES_TEXTURE_SCALE_Y);
        } else if (panel == HUDPanel.MUSIC) {
            loadTexture("RedMusicIcon", new Vector2f(0.9742f, -0.9413f), MUSIC_TEXTURE_SCALE_X, MUSIC_TEXTURE_SCALE_Y);
        }

        clearPanel(player, panel);
    }

    public void clearPanel(Player player, HUDPanel panel) {
        if (panel != HUDPanel.INVENTORY) {
            player.getInventory().clearPanel();
        }
        if (panel != HUDPanel.COMBAT) {

        }
        if (panel != HUDPanel.LEVELS) {
            player.getStats().clearPanel();
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
            if (player.getFriends().getText() != null) {
                player.getFriends().clearPanel();
            }
        }
        if (panel != HUDPanel.IGNORES) {
            if (player.getIgnore().getText() != null) {
                player.getIgnore().clearPanel();
            }
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
