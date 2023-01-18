/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * January 2, 2021
 */

package Utilities;

import RenderEngine.DisplayManager;

import java.util.Random;

public class RandomNumberGenerator extends Random {

    public static final int NUM_STATES = 16;
    public static final int RANGE = 128;

    private long[] state; // initialize state to random bits
    private int index;

    // use this constructor if the given seed is irrelevant
    public RandomNumberGenerator() {
        this((int)DisplayManager.getCurrentTimeInMilliseconds());
    }

    // use this constructor to seed the game with the desired seed
    public RandomNumberGenerator(int seed) {
        state = new long[NUM_STATES];
        index = 0;

        calculateSeed(seed);
    }

    private void calculateSeed(int seed) {
        seed = Math.abs(seed);

        for (int i = 0; i < NUM_STATES; i++) {
            state[i] = (seed + 1) * ((seed + 1) << 2) * i;
        }
    }

    // Well512 algorithm (returns a 32-bit random number)
    @Override
    protected int next(int bits) {
        long a;
        long b;
        long c;
        long d;

        a = Math.abs(state[index]);
        c = Math.abs(state[(index + 13) & 15]);
        b = Math.abs(a ^ Math.abs(c ^ (a << 16) ^ (c << 15)));
        c = Math.abs(state[(index + 9) & 15]);
        c ^= Math.abs((c >> 11));
        a = state[index] = b ^ c;
        d = Math.abs(a ^ Math.abs(((a << 5) & 0xDA442D24L)));
        index = (index + 15) & 15;
        a = state[index];
        state[index] = Math.abs(a ^ Math.abs(b ^ Math.abs(d ^ (a << 2) ^ (b << 18) ^ (c << 28))));
        return (int)state[index] / RANGE;
    }

    @Override
    public float nextFloat() {
        return Math.abs(super.nextFloat());
    }

    @Override
    public int nextInt() {
        return Math.abs(super.nextInt());
    }
}
