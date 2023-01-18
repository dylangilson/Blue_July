/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 23, 2021
 */

package Entities;

import Engine.Equipment.EquipmentSlot;
import Engine.Main;
import Font.TextMaster;
import GUIs.GUITexture;
import Models.RawModel;
import Models.TexturedModel;
import OBJLoader.ModelData;
import OBJLoader.OBJFileLoader;
import Textures.ModelTexture;
import Utilities.InternalJarFile;
import Utilities.Physics;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

// TODO if item has been on the ground for more than 60 seconds, remove the item

public class Item extends InanimateEntity {

    public static final float PICKUP_RADIUS = 1f;

    private int lowValue;
    private int highValue;
    private int extremeValue;
    private boolean isEquipable;
    private EquipmentSlot equipmentSlot;
    private GUITexture texture;
    private float textureScale;

    public Item(int lowValue, int highValue, int extremeValue, boolean isEquipable, EquipmentSlot equipmentSlot, String name,
                TexturedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale3D, float textureScale, int textureIndex) {
        super(name, model, position, velocity, rotation, scale3D, textureIndex, EntityID.ITEM);

        this.lowValue = lowValue;
        this.highValue = highValue;
        this.extremeValue = extremeValue;
        this.isEquipable = isEquipable;
        this.equipmentSlot = equipmentSlot;
        this.textureScale = textureScale;
    }

    public static void updateItems(Player player) {
        for (int i = 0; i < Main.ITEMS_ON_GROUND.size(); i++) {
            Main.ITEMS_ON_GROUND.get(i).update(player);
        }
    }

    // return true if object needs to be removed ; false otherwise
    public boolean update(Player player) {
        // TODO change this to test if player wants to pick up the item
        if (Physics.calculateDistanceBetweenEntities(this, player) < PICKUP_RADIUS) {
            pickUp(player);
            return true;
        }

        return false;
    }

    // called whenever an item is being picked up
    public boolean pickUp(Player player) {
        if (player.addItem(this)) {
            TextMaster.updateChatLog("Just picked up: " + getName() + " (" + extremeValue + " gp)", TextMaster.VALUABLE_TEXT_COLOUR);
            removeFromGround();
            return true;
        } else {
            TextMaster.updateChatLog("Inventory is full.", TextMaster.NORMAL_TEXT_COLOUR);
            return false;
        }
    }

    // remove this instance of the item from the 3D world
    public void removeFromGround() {
        for (int i = 0; i < Main.ITEMS_ON_GROUND.size(); i++) {
            if (Main.ITEMS_ON_GROUND.get(i) == this) {
                Main.ITEMS_ON_GROUND.remove(this);
                Main.INANIMATE_ENTITIES.remove(this);
                Main.ENTITIES.remove(this);

                return;
            }
        }
    }

    public void loadTexture(Vector2f position) {
        int textureID = Main.LOADER.loadTexture(getName(), "Item Inventory Textures");
        this.texture = new GUITexture(textureID, position, new Vector2f(textureScale, textureScale), 0, false);
    }

    public int getLowValue() {
        return lowValue;
    }

    public void setLowValue(int lowValue) {
        this.lowValue = lowValue;
    }

    public int getHighValue() {
        return highValue;
    }

    public void setHighValue(int highValue) {
        this.highValue = highValue;
    }

    public int getExtremeValue() {
        return extremeValue;
    }

    public void setExtremeValue(int extremeValue) {
        this.extremeValue = extremeValue;
    }

    public int getEquipmentSlotInteger() {
        return equipmentSlot.ordinal();
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public boolean isEquipable() {
        return isEquipable;
    }

    public GUITexture getTexture() {
        return texture;
    }

    public float getTextureScale() {
        return textureScale;
    }

    public void setTextureScale(float textureScale) {
        this.textureScale = textureScale;
    }

    // items
    // TODO: get the 2D texture for whip from wiki
    private static final ModelData abyssalWhipData = OBJFileLoader.loadOBJ(new InternalJarFile("res/Objects/Abyssal Whip.obj"));
    private static final RawModel abyssalWhipModel = Main.LOADER.loadToVAO(abyssalWhipData.getVertices(), abyssalWhipData.getTextureCoords(),
            abyssalWhipData.getNormals(), abyssalWhipData.getIndices());
    private static final ModelTexture abyssalWhipTexture = new ModelTexture(Main.LOADER.loadTexture("Abyssal Whip Texture", "Model Textures"));
    public static final TexturedModel ABYSSAL_WHIP = new TexturedModel(abyssalWhipModel, abyssalWhipTexture);
    public static final float ABYSSAL_WHIP_SCALE = 0.05f;
}
