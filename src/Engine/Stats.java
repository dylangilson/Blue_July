/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * June 8, 2021
 */

package Engine;

import org.lwjgl.util.vector.Vector2f;

public class Stats {

    private long totalXP;
    private int totalLevel;
    private Skill attack;
    private Skill strength;
    private Skill defence;
    private Skill ranged;
    private Skill prayer;
    private Skill magic;
    private Skill runecrafting;
    private Skill hitpoints;
    private Skill herblore;
    private Skill crafting;
    private Skill fletching;
    private Skill slayer;
    private Skill mining;
    private Skill smithing;
    private Skill fishing;
    private Skill cooking;
    private Skill firemaking;
    private Skill woodcutting;

    public Stats(boolean isLevelable) {
        attack = new Skill("Attack", 1, new Vector2f(0f, 0f), isLevelable);
        strength = new Skill("Strength", 1, new Vector2f(0f, 0f), isLevelable);
        defence = new Skill("Defence", 1, new Vector2f(0f, 0f), isLevelable);
        ranged = new Skill("Ranged", 1, new Vector2f(0f, 0f), isLevelable);
        prayer = new Skill("Prayer", 1, new Vector2f(0f, 0f), isLevelable);
        magic = new Skill("Magic", 1, new Vector2f(0f, 0f), isLevelable);
        runecrafting = new Skill("Runecrafting", 1, new Vector2f(0f, 0f), isLevelable);
        hitpoints = new Skill("Hitpoints", 1000, new Vector2f(0f, 0f), isLevelable);
        herblore = new Skill("Herblore", 1, new Vector2f(0f, 0f), isLevelable);
        crafting = new Skill("Crafting", 1, new Vector2f(0f, 0f), isLevelable);
        fletching = new Skill("Fletching", 1, new Vector2f(0f, 0f), isLevelable);
        slayer = new Skill("Slayer", 1, new Vector2f(0f, 0f), isLevelable);
        mining = new Skill("Mining", 1, new Vector2f(0f, 0f), isLevelable);
        smithing = new Skill("Smithing", 1, new Vector2f(0f, 0f), isLevelable);
        fishing = new Skill("Fishing", 1, new Vector2f(0f, 0f), isLevelable);
        cooking = new Skill("Cooking", 1, new Vector2f(0f, 0f), isLevelable);
        firemaking = new Skill("Firemaking", 1, new Vector2f(0f, 0f), isLevelable);
        woodcutting = new Skill("Woodcutting", 1, new Vector2f(0f, 0f), isLevelable);

        calculateTotalXP();
        calculateTotalLevel();
    }

    public void calculateTotalXP() {
        totalXP = 0;

        totalXP += attack.getXP();
        totalXP += strength.getXP();
        totalXP += defence.getXP();
        totalXP += ranged.getXP();
        totalXP += prayer.getXP();
        totalXP += magic.getXP();
        totalXP += runecrafting.getXP();
        totalXP += hitpoints.getXP();
        totalXP += herblore.getXP();
        totalXP += crafting.getXP();
        totalXP += fletching.getXP();
        totalXP += slayer.getXP();
        totalXP += mining.getXP();
        totalXP += smithing.getXP();
        totalXP += fishing.getXP();
        totalXP += cooking.getXP();
        totalXP += firemaking.getXP();
        totalXP += woodcutting.getXP();
    }

    public void calculateTotalLevel() {
        totalLevel = 0;

        totalLevel += attack.getLevel();
        totalLevel += strength.getLevel();
        totalLevel += defence.getLevel();
        totalLevel += ranged.getLevel();
        totalLevel += prayer.getLevel();
        totalLevel += magic.getLevel();
        totalLevel += runecrafting.getLevel();
        totalLevel += hitpoints.getLevel();
        totalLevel += herblore.getLevel();
        totalLevel += crafting.getLevel();
        totalLevel += fletching.getLevel();
        totalLevel += slayer.getLevel();
        totalLevel += mining.getLevel();
        totalLevel += smithing.getLevel();
        totalLevel += fishing.getLevel();
        totalLevel += cooking.getLevel();
        totalLevel += firemaking.getLevel();
        totalLevel += woodcutting.getLevel();
    }

    // reset all temporary levels back to actual level
    public void reset() {
        this.attack.updateTemporaryLevel(this.attack.getLevel(), false);
        this.strength.updateTemporaryLevel(this.strength.getLevel(), false);
        this.defence.updateTemporaryLevel(this.defence.getLevel(), false);
        this.ranged.updateTemporaryLevel(this.ranged.getLevel(), false);
        this.prayer.updateTemporaryLevel(this.prayer.getLevel(), false);
        this.magic.updateTemporaryLevel(this.magic.getLevel(), false);
        this.runecrafting.updateTemporaryLevel(this.runecrafting.getLevel(), false);
        this.hitpoints.updateTemporaryLevel(this.hitpoints.getLevel(), false);
        this.herblore.updateTemporaryLevel(this.herblore.getLevel(), false);
        this.crafting.updateTemporaryLevel(this.crafting.getLevel(), false);
        this.fletching.updateTemporaryLevel(this.fletching.getLevel(), false);
        this.slayer.updateTemporaryLevel(this.slayer.getLevel(), false);
        this.mining.updateTemporaryLevel(this.mining.getLevel(), false);
        this.smithing.updateTemporaryLevel(this.smithing.getLevel(), false);
        this.fishing.updateTemporaryLevel(this.fishing.getLevel(), false);
        this.cooking.updateTemporaryLevel(this.cooking.getLevel(), false);
        this.firemaking.updateTemporaryLevel(this.firemaking.getLevel(), false);
        this.woodcutting.updateTemporaryLevel(this.woodcutting.getTemporaryLevel(), false);
    }

    public long getTotalXP() {
        return totalXP;
    }

    public int getTotalLevel() {
        return totalLevel;
    }

    public Skill getAttack() {
        return attack;
    }

    public Skill getStrength() {
        return strength;
    }

    public Skill getDefence() {
        return defence;
    }

    public Skill getRanged() {
        return ranged;
    }

    public Skill getPrayer() {
        return prayer;
    }

    public Skill getMagic() {
        return magic;
    }

    public Skill getRunecrafting() {
        return runecrafting;
    }

    public Skill getHitpoints() {
        return hitpoints;
    }

    public Skill getHerblore() {
        return herblore;
    }

    public Skill getCrafting() {
        return crafting;
    }

    public Skill getFletching() {
        return fletching;
    }

    public Skill getSlayer() {
        return slayer;
    }

    public Skill getMining() {
        return mining;
    }

    public Skill getSmithing() {
        return smithing;
    }

    public Skill getFishing() {
        return fishing;
    }

    public Skill getCooking() {
        return cooking;
    }

    public Skill getFiremaking() {
        return firemaking;
    }

    public Skill getWoodcutting() {
        return woodcutting;
    }
}
