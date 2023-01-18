/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * June 8, 2021
 */

package Engine;

import GUIs.GUITexture;
import org.lwjgl.util.vector.Vector2f;

public class Skill {

    private static final float TEXTURE_SCALE = 0.04f;

    private String name;
    private long xp;
    private int temporaryLevel; // can be altered by potions, effects etc.
    private int level; // actual level
    private boolean isLevelable; // true if stats can increase (e.g. player), false otherwise (e.g. enemies)
    private GUITexture texture;

    public Skill(String name, long xp, Vector2f texturePosition, boolean isLevelable) {
        this.name = name;
        this.xp = xp;
        calculateLevel();
        this.temporaryLevel = level;
        this.isLevelable = isLevelable;
        loadTexture(texturePosition);
    }

    public void addXP(int xp) {
        if (!isLevelable) {
            return;
        }

        this.xp += xp;
        calculateLevel();
    }

    public void calculateLevel() {
        this.level = (int)Math.cbrt(xp);
    }

    // input may be positive or negative
    // positive -> increase temporary level (e.g. eating food or drinking potion)
    // negative -> decrease temporary level (e.g. taking damage)
    public void updateTemporaryLevel(int value, boolean isOverLevelable) {
        this.temporaryLevel += value;

        if (this.temporaryLevel < 0) {
            this.temporaryLevel = 0;
        }

        // allows things like potions to boost stats to over maximum level
        if (!isOverLevelable) {
            if (this.temporaryLevel > 99) {
                this.temporaryLevel = 99;
            }
        }
    }

    public void loadTexture(Vector2f position) {
        int textureID = Main.LOADER.loadTexture(name, "Skill Textures");
        this.texture = new GUITexture(textureID, position, new Vector2f(TEXTURE_SCALE, TEXTURE_SCALE), 0, false);
    }

    public String getName() {
        return name;
    }

    public long getXP() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getTemporaryLevel() {
        return temporaryLevel;
    }
}
