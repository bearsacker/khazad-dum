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

    public static final int DEXTERITY = get().getInt("warrior.dexterity");

    public static final int VITALITY = get().getInt("warrior.vitality");

    public static final int STRENGTH = get().getInt("warrior.strength");

    public static final int LIGHT_RADIUS = get().getInt("warrior.light-radius");

    public static final int LUCK = get().getInt("warrior.luck");

}
