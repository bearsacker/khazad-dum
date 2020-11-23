package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class LevelingConfig extends Config {

    private static final LevelingConfig INSTANCE = new LevelingConfig("leveling.properties");

    private LevelingConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final Integer[] LEVELING_LEVELS = toIntegerArray(get().getString("leveling.levels"));

    public static final int LEVELING_LEVEL_MAX = get().getInt("leveling.level.max");

    public static final int LEVELING_POINTS_PER_LEVEL = get().getInt("leveling.points-per-level");
}
