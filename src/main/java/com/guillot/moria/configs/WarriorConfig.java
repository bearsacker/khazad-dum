package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class WarriorConfig extends Config {

    private static final WarriorConfig INSTANCE = new WarriorConfig("warrior.properties");

    private WarriorConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int AGILITY = get().getInt("warrior.agility");

    public static final int SPIRIT = get().getInt("warrior.spirit");

    public static final int STRENGTH = get().getInt("warrior.strength");

    public static final int DESITINY = get().getInt("warrior.destiny");

    public static final int LIGHT_RADIUS = get().getInt("warrior.light-radius");

    public static final int LIFE = get().getInt("warrior.life");

    public static final int LIFE_PER_LEVEL = get().getInt("warrior.life-per-level");

    public static final int LIFE_PER_SPIRIT = get().getInt("warrior.life-per-spririt");

    public static final int CHANCE_TO_HIT = get().getInt("warrior.chance-hit");

    public static final int CHANCE_MAGIC_FIND = get().getInt("warrior.chance-magic-find");

    public static final int CHANCE_LOCK_PICKING = get().getInt("warrior.chance-lock-picking");
}
