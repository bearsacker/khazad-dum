package com.guillot.moria.utils;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

public class RNG {

    private static final RNG INSTANCE = new RNG();

    private Random random;

    private long seed;

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        random.setSeed(seed);
    }

    public Random getRandom() {
        return random;
    }

    // Generates a random integer x where 1 <= X <= max
    public int randomNumber(int max) {
        return random.nextInt(max) + 1;
    }

    // Generates a random integer x where min <= X <=max
    public int randomNumberBetween(int min, int max) {
        if (max == 0) {
            return 0;
        }

        return random.nextInt(max + 1 - min) + min;
    }

    // Generates a random integer number of NORMAL distribution
    public int randomNumberNormalDistribution(int mean, int standard) {
        NormalDistribution distribution = new NormalDistribution(mean, standard);
        distribution.reseedRandomGenerator(seed);

        return (int) distribution.sample();
    }

    private RNG() {
        seed = System.currentTimeMillis();

        random = new Random();
        random.setSeed(seed);
    }

    public static RNG get() {
        return INSTANCE;
    }
}
