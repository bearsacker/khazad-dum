package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class MonstersConfig extends Config {

    private static final MonstersConfig INSTANCE = new MonstersConfig("monsters.properties");

    private MonstersConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int MON_MIN_PER_LEVEL = get().getInt("monsters.min.level");

    public static final int MON_SUMMONED_LEVEL_ADJUST = get().getInt("monsters.summoned.level.adjust");

    public static final int MON_CHANCE_OF_NASTY = get().getInt("monsters.chance.nasty");

}
