package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class ObjectsConfig extends Config {

    private static final ObjectsConfig INSTANCE = new ObjectsConfig("dungeon.properties");

    private ObjectsConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int MAX_GOLD_TYPES = get().getInt("objects.max.gold-types");

    public static final int MAX_TRAPS = get().getInt("objects.max.traps");

    public static final int LEVEL_OBJECTS_PER_ROOM = get().getInt("level.room.objects");

    public static final int LEVEL_OBJECTS_PER_CORRIDOR = get().getInt("level.corridor.objects");

    public static final int LEVEL_TOTAL_GOLD_AND_GEMS = get().getInt("level.total.gold");
}
