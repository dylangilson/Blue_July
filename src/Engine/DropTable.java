/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 19, 2022
 */

package Engine;

import Engine.Equipment.EquipmentSlot;
import Entities.AggressiveNPC;
import Entities.Item;
import Models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.InvocationTargetException;

public class DropTable {

    public Item[] items;
    public int[] dropChances; // highest numbers to lowest numbers (most common to most rare)
    public int totalWeight;

    public DropTable(int size) {
        this.items = new Item[size];
        this.dropChances = new int[size];

    }

    public void calculateTotalWeight() {
        totalWeight = 0;

        for (int i = 0; i < dropChances.length; i++) {
            totalWeight += dropChances[i];
        }
    }

    public Item getRandomLoot(AggressiveNPC enemy) {
        int dropValue = Main.RANDOM_NUMBER_GENERATOR.nextInt(totalWeight);

        for (int i = 0; i < dropChances.length; i++) {
            if (dropValue < dropChances[i]) {
                try {
                    Class[] args = new Class[13];
                    args[0] = int.class;
                    args[1] = int.class;
                    args[2] = int.class;
                    args[3] = boolean.class;
                    args[4] = EquipmentSlot.class;
                    args[5] = String.class;
                    args[6] = TexturedModel.class;
                    args[7] = Vector3f.class;
                    args[8] = Vector3f.class;
                    args[9] = Vector3f.class;
                    args[10] = float.class;
                    args[11] = float.class;
                    args[12] = int.class;

                    return items[i].getClass().getDeclaredConstructor(args).newInstance(items[i].getLowValue(), items[i].getHighValue(), items[i].getExtremeValue(),
                            items[i].isEquipable(), items[i].getEquipmentSlot(), items[i].getName(), items[i].getModel(), enemy.getPosition(), items[i].getVelocity(),
                            items[i].getRotation(), items[i].getScale(), items[i].getTextureScale(), items[i].getTextureIndex());

                } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.err.println("Tried to create new instance of: " + items[i].getName() + ", didn't work");
                    System.exit(-1);
                }
            } else {
                dropValue -= dropChances[i];
            }
        }

        return null;
    }
}
