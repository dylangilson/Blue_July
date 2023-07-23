/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 25, 2021
 */

package Engine;

import Entities.Item;
import Entities.Player;
import Utilities.MousePicker;

import org.lwjgl.util.vector.Vector2f;

public class Inventory {

    public static final float DISTANCE_BETWEEN_EACH_INDEX_X = 0.44f;
    public static final float DISTANCE_BETWEEN_EACH_INDEX_Y = 0.52f;
    public static final float INITIAL_X_OFFSET = 0.55f;
    public static final float INITIAL_Y_OFFSET = -0.015f;

    private Player player;
    private Item[] items;
    private int numberOfRows;
    private int numberOfColumns;
    private int firstFreeSlot;
    private int initialXPosition;
    private int pixelsBetweenEachIndexX;
    private int initialYPosition;
    private int pixelsBetweenEachIndexY;

    public Inventory(Player player, int size, int numberOfRows, int numberOfColumns, int initialXPosition, int pixelsBetweenEachIndexX,
                     int initialYPosition, int pixelsBetweenEachIndexY) {
        this.player = player;
        this.items = new Item[size];
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.initialXPosition = initialXPosition;
        this.pixelsBetweenEachIndexX = pixelsBetweenEachIndexX;
        this.initialYPosition = initialYPosition;
        this.pixelsBetweenEachIndexY = pixelsBetweenEachIndexY;
        this.firstFreeSlot = 0;
    }

    public void interactWithInventorySlot(Vector2f position) {
        if (position.y > MousePicker.TOP_BAR_HEIGHT || position.y < MousePicker.BOTTOM_BAR_HEIGHT) {
            return;
        }

        int x = 0;
        for (int i = 0; i <= numberOfColumns; i++) {
            if (position.x < initialXPosition + (pixelsBetweenEachIndexX * i)) {
                x = i - 1;
                break;
            }
        }

        int y = 0;
        for (int i = numberOfRows; i >= 0; i--) {
            if (position.y < initialYPosition - (pixelsBetweenEachIndexY * i)) {
                y = i;
                break;
            }
        }

        int index = y * numberOfColumns + x;

        if (index == -1 || index > items.length - 1) {
            return;
        }

        if (items[index] == null) {
            return;
        }

        if (items[index].isEquipable()) {
            Item item = items[index];
            remove(index);

            player.getEquipment().equip(item, index);

            return;
        }

        // TODO change this from removing the object to performing the object's interaction of choice
        remove(index);
    }

    public boolean swap(int lastRightClickX, int lastRightClickY, int rightClickX, int rightClickY) {
        if (rightClickY > MousePicker.TOP_BAR_HEIGHT || rightClickY < MousePicker.BOTTOM_BAR_HEIGHT ) {
            return false;
        }

        if (rightClickX > MousePicker.RIGHT_BAR_WIDTH || rightClickX < MousePicker.LEFT_BAR_WIDTH) {
            return false;
        }

        int lastX = 0;
        for (int i = 0; i <= numberOfColumns; i++) {
            if (lastRightClickX < initialXPosition + (pixelsBetweenEachIndexX * i)) {
                lastX = i - 1;
                break;
            }
        }

        int lastY = 0;
        for (int i = numberOfRows; i >= 0; i--) {
            if (lastRightClickY < initialYPosition - (pixelsBetweenEachIndexY * i)) {
                lastY = i;
                break;
            }
        }

        int lastIndex = lastY * numberOfColumns + lastX;

        int x = 0;
        for (int i = 0; i <= numberOfColumns; i++) {
            if (rightClickX < initialXPosition + (pixelsBetweenEachIndexX * i)) {
                x = i - 1;
                break;
            }
        }

        int y = 0;
        for (int i = numberOfRows; i >= 0; i--) {
            if (rightClickY < initialYPosition - (pixelsBetweenEachIndexY * i)) {
                y = i;
                break;
            }
        }

        if (x < 0 || y < 0 || x > numberOfColumns || y > numberOfRows) {
            return false;
        }

        int index = y * numberOfColumns + x;

        return swapItems(lastIndex, index);
    }

    public boolean addItem(Item item) {
        if (firstFreeSlot == items.length) {
            return false; // inventory is full
        }

        items[firstFreeSlot] = item;
        if (Main.HUD_PANEL == MousePicker.HUDPanel.INVENTORY) {
            item.loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(firstFreeSlot),
                    player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(firstFreeSlot)));
        }

        for (int i = firstFreeSlot + 1; i < items.length; i++) {
            if (items[i] == null) {
                firstFreeSlot = i;
                return true;
            }
        }

        firstFreeSlot = items.length;

        return true;
    }

    public boolean addItemAtIndex(Item item, int index) {
        items[index] = item;
        if (Main.HUD_PANEL == MousePicker.HUDPanel.INVENTORY) {
            item.loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(index),
                    player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(index)));
        }

        return true;
    }

    public void remove(int index) {
        if (items[index] == null) {
            return;
        }

        Main.GUIS.remove(items[index].getTexture());

        items[index] = null;
        if (index < firstFreeSlot) {
            firstFreeSlot = index;
        }
    }

    private boolean swapItems(int indexA, int indexB) {
        if (indexA >= numberOfRows * numberOfColumns || indexB >= numberOfRows * numberOfColumns || indexA < 0 || indexB < 0 || indexA == indexB) {
            return false;
        }

        if (items[indexA] == null) {
            return false;
        }

        Main.GUIS.remove(items[indexA].getTexture());

        if (items[indexB] != null) {
            Main.GUIS.remove(items[indexB].getTexture());
        }

        Item temp = items[indexA];
        items[indexA] = items[indexB];
        items[indexB] = temp;

        if (items[indexA] != null) {
            items[indexA].loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(indexA),
                    player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(indexA)));
        }

        if (items[indexB] != null) {
            items[indexB].loadTexture(new Vector2f(player.getInventory().INITIAL_X_OFFSET + player.getInventory().getOffsetX(indexB),
                    player.getInventory().INITIAL_Y_OFFSET - player.getInventory().getOffsetY(indexB)));
        }

        findFirstFreeSlot();

        return true;
    }

    public void findFirstFreeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                firstFreeSlot = i;
                return;
            }
        }

        firstFreeSlot = items.length;
    }

    public void clearPanel() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                Main.GUIS.remove(items[i].getTexture());
            }
        }
    }

    public Item getItem(int index) {
        return items[index];
    }

    public float getOffsetX(int index) {
        int column = index % numberOfColumns;
        return (float)column / numberOfColumns * DISTANCE_BETWEEN_EACH_INDEX_X;
    }

    public float getOffsetY(int index) {
        int row = index / numberOfColumns;
        return (float)row / numberOfColumns * DISTANCE_BETWEEN_EACH_INDEX_Y;
    }

    public Item[] getItems() {
        return items;
    }
}
