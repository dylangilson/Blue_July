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
        firstFreeSlot = 0;
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

            player.getEquipment().equip(item);

            return;
        }

        // TODO change this from removing the object to performing the object's interaction of choice
        remove(index);
    }

    public boolean addItem(Item item) {
        if (firstFreeSlot == items.length) {
            return false; // inventory is full
        }

        items[firstFreeSlot] = item;
        item.loadTexture(new Vector2f(INITIAL_X_OFFSET + getOffsetX(firstFreeSlot), INITIAL_Y_OFFSET - getOffsetY(firstFreeSlot)));

        for (int i = firstFreeSlot + 1; i < items.length; i++) {
            if (items[i] == null) {
                firstFreeSlot = i;
                return true;
            }
        }

        firstFreeSlot = items.length;

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
