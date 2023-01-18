/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 16, 2021
 */

package Particles;

import Entities.Camera;
import RenderEngine.ParticleRenderer;
import Utilities.InsertionSort;

import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParticleMaster {

    private static Map<ParticleTexture, List<Particle>> particles = new LinkedHashMap<ParticleTexture, List<Particle>>();
    private static ParticleRenderer renderer;

    public static void init(Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer(projectionMatrix);
    }

    public static void addParticle(Particle particle) {
        List<Particle> list = particles.get(particle.getTexture());

        if (list == null) {
            list = new ArrayList<Particle>();

            particles.put(particle.getTexture(), list);
        }

        list.add(particle);
    }

    public static void update(Camera camera) {
        Iterator<Map.Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();

        while (mapIterator.hasNext()) {
            Map.Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
            List<Particle> list = entry.getValue();
            Iterator<Particle> iterator = list.iterator();

            while (iterator.hasNext()) {
                Particle temp = iterator.next();
                boolean stillAlive = temp.update(camera);

                if (!stillAlive) {
                    iterator.remove();

                    if (list.isEmpty()) {
                        mapIterator.remove();
                    }
                }
            }

            if (!entry.getKey().usesAdditiveBlending()) {
                InsertionSort.sortHighToLow(list);
            }
        }
    }

    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);
    }

    public static void free() {
        renderer.free();
    }
}
