package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class HobbitConfig extends Config {

    private static final HobbitConfig INSTANCE = new HobbitConfig("hobbit.properties");

    private HobbitConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int AGILITY = get().getInt("agility");

    public static final int SPIRIT = get().getInt("spirit");

    public static final int STRENGTH = get().getInt("strength");

    public static final int DESITINY = get().getInt("destiny");

    public static final int LIGHT_RADIUS = get().getInt("light-radius");

    public static final int LIFE = get().getInt("life");

    public static final int CHANCE_TO_HIT = get().getInt("chance-hit");

    public static final int CHANCE_MAGIC_FIND = get().getInt("chance-magic-find");

    public static final int CHANCE_LOCK_PICKING = get().getInt("chance-lock-picking");

    public static final int CHANCE_CRITICAL_HIT = get().getInt("chance-critical-hit");

    public static final int INVENTORY_LIMIT = get().getInt("inventory-limit");
}
