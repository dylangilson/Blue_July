/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 29, 2022
 */

package Entities;

import Animation.AnimatedModel;
import Engine.Main;
import Engine.Stats;
import Particles.ParticleSystem;
import Particles.ParticleTexture;

import org.lwjgl.util.vector.Vector3f;

public class AnimatedEntity extends Entity {

    private AnimatedModel model;
    private float healthBarOffset;
    private ParticleTexture healthBarTexture;
    private ParticleSystem healthBarParticleSystem;
    private Stats stats;

    public AnimatedEntity(String name, AnimatedModel model, Vector3f position, Vector3f velocity, Vector3f rotation, float scale, int textureIndex,
                          EntityID entityID) {
        super(name, position, velocity, rotation, scale, textureIndex, entityID);

        this.model = model;
        this.healthBarTexture = new ParticleTexture(Main.LOADER.loadTexture("32", "Health Bar Textures"), 1, false);
        this.healthBarParticleSystem = new ParticleSystem(healthBarTexture, 600, 1, 0, 0.001f, 2f, 2f);
        this.stats = new Stats(false);

        Main.ENTITIES.add(this);
        Main.ANIMATED_ENTITIES.add(this);
    }

    public void update() {
        String health = String.valueOf((int)((float)this.stats.getHitpoints().getTemporaryLevel() / (float)this.stats.getHitpoints().getLevel() * 32));
        if (health.equals("0")) {
            return;
        }

        Main.LOADER.TEXTURES.remove(this.healthBarTexture);

        this.healthBarTexture = new ParticleTexture(Main.LOADER.loadTexture(health, "Health Bar Textures"), 1, false);
        this.healthBarParticleSystem = new ParticleSystem(this.healthBarTexture, 600, 1, 0, 0.001f, 2f, 2f);
    }

    public AnimatedModel getModel() {
        return model;
    }

    public ParticleTexture getHealthBarTexture() {
        return healthBarTexture;
    }

    public void setHealthBarTexture(ParticleTexture healthBarTexture) {
        this.healthBarTexture = healthBarTexture;
    }

    public ParticleSystem getHealthBarParticleSystem() {
        return healthBarParticleSystem;
    }

    public void setHealthBarParticleSystem(ParticleSystem healthBarParticleSystem) {
        this.healthBarParticleSystem = healthBarParticleSystem;
    }

    public void setHealthBarOffset(float offset) {
        this.healthBarOffset = offset;
    }

    public float getHealthBarOffset() {
        return healthBarOffset;
    }

    public Stats getStats() {
        return stats;
    }
}
