/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * June 23, 2021
 */

package Audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class Source {

    public static final float REFERENCE_DISTANCE = 6f; // minimum distance from source before the sound begins to get quieter
    public static final float MAX_DISTANCE = 50f; // maximum distance away that a sound can be heard from this source

    private int sourceID;

    public Source(float volume, Vector3f position, Vector3f velocity) {
        this.sourceID = AL10.alGenSources();

        AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, 1);
        AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, REFERENCE_DISTANCE);
        AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, MAX_DISTANCE);

        setVolume(volume);
        setPosition(position);
        setVelocity(velocity);
    }

    public void playSound(int bufferID) {
        stopSound();

        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);

        continueSound();
    }

    public void pauseSound() {
        AL10.alSourcePause(sourceID);
    }

    public void continueSound() {
        AL10.alSourcePlay(sourceID);
    }

    public void stopSound() {
        AL10.alSourceStop(sourceID);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceID, AL10.AL_GAIN, volume); // GAIN == VOLUME
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position) {
        AL10.alSource3f(sourceID, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public void setVelocity(Vector3f velocity) {
        AL10.alSource3f(sourceID, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public void setLooping(boolean value) {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, value ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void free() {
        stopSound();
        AL10.alDeleteSources(sourceID);
    }
}
