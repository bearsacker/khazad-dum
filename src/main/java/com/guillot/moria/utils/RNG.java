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

    // Generates a random integer x where 1<=X<=MAXVA
    public int randomNumber(int max) {
        return random.nextInt(max) + 1;
    }

    public int randomNumberBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
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
