/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * June 23, 2021
 */

package Audio;

import Utilities.InternalJarFile;
import Utilities.ResourceStreamReader;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

public class AudioMaster {

    // TODO create a slider for a setting between min and max volume
    public static final float MIN_VOLUME = 0f; // mute
    public static final float MAX_VOLUME = 1f;

    public static List<Integer> buffers = new ArrayList<Integer>();

    public static void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void setListenerData(Vector3f position, Vector3f velocity) {
        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public static int loadSound(InternalJarFile file) {
        int bufferID = AL10.alGenBuffers();
        buffers.add(bufferID);

        BufferedInputStream is = ResourceStreamReader.getResourceStream(file.getPath());
        WaveData waveFile = WaveData.create(is);
        AL10.alBufferData(bufferID, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();

        return bufferID;
    }

    public static void free() {
        for (int i = 0; i < buffers.size(); i++) {
            AL10.alDeleteBuffers(buffers.get(i));
        }

        AL.destroy();
    }
}
