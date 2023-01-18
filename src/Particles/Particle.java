/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 15, 2021
 */

package Particles;

import Entities.Camera;
import RenderEngine.DisplayManager;
import Utilities.GlobalConstants;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Particle {

    private ParticleTexture texture;
    private Vector3f position;
    private Vector3f velocity; // speed AND direction of particle
    private float gravityEffect; // 1 -> normal gravity ; value between 0 and 1 -> object "floats" more
    private float lifeLength; // length of particle effect in seconds
    private float rotation;
    private float scaleX;
    private float scaleY;
    private float elapsedTime = 0; // length of time the particle has been alive for
    private Vector2f textureOffset1 = new Vector2f(); // current frame offset
    private Vector2f textureOffset2 = new Vector2f(); // next frame offset
    private float blendFactor;
    private float distance; // distance from particle to camera
    private Vector3f velocityChange = new Vector3f();

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scaleX, float scaleY) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        ParticleMaster.addParticle(this);
    }

    // returns true if particle is still alive ; otherwise false
    protected boolean update(Camera camera) {
        velocity.y = GlobalConstants.GRAVITY * gravityEffect * DisplayManager.getFrameTimeInSeconds() * 10; // gravity only effects the y-axis
        velocityChange.set(velocity); // change in particle position during this frame
        velocityChange.scale(DisplayManager.getFrameTimeInSeconds());
        position = Vector3f.add(velocityChange, position, null);
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();

        updateTextureCoordInfo();

        elapsedTime += DisplayManager.getFrameTimeInSeconds();

        return elapsedTime < lifeLength;
    }

    private void updateTextureCoordInfo() {
        float lifeFactor = elapsedTime / lifeLength; // 0 -> new particle ; 1 -> dead particle
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows(); // num of frames in texture atlas
        float atlasProgression = lifeFactor * stageCount;

        int currentIndex = (int)Math.floor(atlasProgression);
        int nextIndex;
        if (currentIndex < stageCount - 1) {
            nextIndex = currentIndex + 1;
        } else {
            nextIndex = currentIndex;
        }

        this.blendFactor = atlasProgression % 1; // fractional component of atlasProgression

        setTextureOffset(textureOffset1, currentIndex);
        setTextureOffset(textureOffset2, nextIndex);
    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();

        offset.x = (float)column / texture.getNumberOfRows();
        offset.y = (float)row / texture.getNumberOfRows();
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public Vector2f getTextureOffset1() {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public float getBlendFactor() {
        return blendFactor;
    }

    public float getDistance() {
        return distance;
    }
}
