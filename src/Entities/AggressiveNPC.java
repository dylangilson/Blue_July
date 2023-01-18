/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * December 31, 2021
 */

package Entities;

import Animation.AnimatedModel;
import Engine.Delay;
import Engine.DropTable;
import Engine.Main;
import Terrains.Terrain;
import Utilities.GlobalConstants;
import Utilities.Physics;

import org.lwjgl.util.vector.Vector3f;

public abstract class AggressiveNPC extends NPC {

    private boolean isAggressive; // true if NPC will auto-attack the player, false otherwise
    private float attackRange;
    private Delay attackDelay;
    public DropTable dropTable;

    public enum AttackType {
        MELEE, RANGED
    }

    private AttackType attackType;

    private class CustomThread extends Thread {
        @Override
        public void run() {
            pause();

            respawn();
        }

        public void pause() {
            try {
                // TODO make the wait time more appropriate (~30 seconds?)
                this.sleep(3 * GlobalConstants.CONVERT_MILLISECONDS_TO_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("Tried to wait on Thread: " + this.getName() + ", didn't work");
                System.exit(-1);
            }
        }
    }

    private CustomThread thread;

    public AggressiveNPC(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale,
                         int textureIndex, MovementType movementType, boolean isAggressive, AttackType attackType, int attackDelayLength, int dropTableSize) {
        super(name, model, position, velocity, rotation, scale, textureIndex, movementType, EntityID.NPC);

        this.target = null;
        this.isAggressive = isAggressive;
        this.attackType = attackType;
        this.attackDelay = new Delay(attackDelayLength);
        this.attackDelay.end();
        this.dropTable = new DropTable(dropTableSize);

        attackRange = attackType == AttackType.MELEE ? GlobalConstants.MELEE_ATTACK_RANGE : GlobalConstants.RANGED_ATTACK_RANGE;

        Main.NPCS.add(this);
        Main.ENEMIES.add(this);
    }

    public abstract void initializeDropTable();

    // this method gets called every frame
    public void update(Player player, Terrain terrain) {
        if (this.getStats().getHitpoints().getTemporaryLevel() == 0) {
            death();
        }

        if (target == null) {
            if (isAggressive) {
                look(player);
            }
        } else {
            if (Physics.calculateDistanceBetweenEntities(player, this) <= attackRange) {
                if (attackDelay.isOver()) {
                    attack(player);
                    attackDelay.start();
                }
            } else {
                chase(terrain);
            }
        }
    }

    private void chase(Terrain terrain) {
        Vector3f targetPosition = target.getPosition();

        float newX = targetPosition.getX();
        float newY = targetPosition.getY();
        float newZ = targetPosition.getZ();
        float offset = 0.1f; // NPC becomes slightly too far away

        newZ = newZ < 0 ? newZ - attackRange + offset : newZ + attackRange - offset; // move close to the player, but not right on his position

        moveTo(new Vector3f(newX, newY, newZ), terrain);
    }

    private void attack(Player player) {
        faceTarget(player.getPosition());

        if (Physics.calculateDistanceBetweenEntities(player, this) > attackRange) {
            return;
        }

        target.getStats().getHitpoints().updateTemporaryLevel(-calculateAttackDamage(), false);
        restartAttackDelay();
    }

    public void restartAttackDelay() {
        attackDelay.start();
    }

    // TODO add a more random and intelligent damage calculation
    public int calculateAttackDamage() {
        return getStats().getStrength().getTemporaryLevel();
    }

    // TODO drop items
    private void death() {
        target.setTarget(null);

        Item item = dropTable.getRandomLoot(this);
        Main.ITEMS_ON_GROUND.add(item);
        Main.INANIMATE_ENTITIES.add(item);

        Main.ENTITIES.remove(this);
        Main.ANIMATED_ENTITIES.remove(this);
        Main.NPCS.remove(this);
        Main.ENEMIES.remove(this);

        this.thread = new CustomThread();
        thread.start();
    }

    public void respawn() {
        this.getStats().reset();
        // TODO change position to random position somewhere in the bounding volume of given NPC
        this.setPosition(new Vector3f(50, getPosition().y, -50));
        this.target = null;

        Main.ENTITIES.add(this);
        Main.ANIMATED_ENTITIES.add(this);
        Main.NPCS.add(this);
        Main.ENEMIES.add(this);
    }

    @Override
    public void move() {
        if (target == null) {
            return;
        }

        super.move();
    }

    public boolean isAggressive() {
        return isAggressive;
    }

    public void setAggressive(boolean aggressive) {
        isAggressive = aggressive;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public Delay getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(Delay attackDelay) {
        this.attackDelay = attackDelay;
    }

    public DropTable getDropTable() {
        return dropTable;
    }

    public void setDropTable(DropTable dropTable) {
        this.dropTable = dropTable;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }
}
