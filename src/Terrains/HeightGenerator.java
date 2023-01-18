/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * May 18, 2021
 */

package Terrains;

import java.util.Random;

public class HeightGenerator {

    private static final float AMPLITUDE = 20f; // height of terrain -> [-AMPLITUDE, AMPLITUDE]
    private static final int OCTAVES = 4; // number of noise functions
    private static final float ROUGHNESS = 0.3f; // higher roughness value -> more mountainous

    private Random random;
    private int seed;

    public HeightGenerator(int seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public float generateHeight(int worldX, int worldZ) {
        float total = 0;
        float d = (float)Math.pow(2, OCTAVES - 1);

        for (int i = 0; i < OCTAVES; i++) {
            float frequency = (float)(Math.pow(2, i) / d);
            float actualAmplitude = (float)Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += getInterpolatedNoise(worldX * frequency, worldZ * frequency) * actualAmplitude;
        }

        return total;
    }

    private float getInterpolatedNoise(float worldX, float worldZ) {
        int intWorldX = (int)worldX;
        float fracWorldX = worldX - intWorldX;

        int intWorldZ = (int)worldZ;
        float fracWorldZ = worldZ - intWorldZ;

        float v1 = getSmoothNoise(intWorldX, intWorldZ);
        float v2 = getSmoothNoise(intWorldX + 1, intWorldZ);
        float i1 = interpolation(v1, v2, fracWorldX);

        float v3 = getSmoothNoise(intWorldX, intWorldZ + 1);
        float v4 = getSmoothNoise(intWorldX + 1, intWorldZ + 1);
        float i2 = interpolation(v3, v4, fracWorldX);

        return interpolation(i1, i2, fracWorldZ);
    }

    // cosine interpolation
    private float interpolation(float a, float b, float blendFactor) {
        double theta = blendFactor * Math.PI;
        float f = (float)(1f - Math.cos(theta)) * 0.5f; // refactored blendFactor still in [0, 1]

        return a * (1f - f) + b * f;
    }

    // gets noise of current vertex AND all neighbouring vertices for a more averagely distributed height
    private float getSmoothNoise(int worldX, int worldZ) {
        float corners = (getNoise(worldX - 1, worldZ - 1) + getNoise(worldX + 1, worldZ - 1)
                + getNoise(worldX - 1, worldZ + 1) + getNoise(worldX + 1, worldZ + 1)) / 16f;

        float sides = (getNoise(worldX - 1, worldZ) + getNoise(worldX + 1, worldZ)
                + getNoise(worldX, worldZ - 1) + getNoise(worldX, worldZ + 1)) / 8f;

        float centre = getNoise(worldX, worldZ) / 4f;

        return corners + sides + centre;
    }

    private float getNoise(int worldX, int worldZ) {
        random.setSeed(worldX * 49632 + worldZ * 325176 + seed);
        return random.nextFloat() * 2f - 1f; // return a value in [-1, 1]
    }
}
