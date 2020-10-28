package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class DungeonConfig extends Config {

    private static final DungeonConfig INSTANCE = new DungeonConfig("dungeon.properties");

    private DungeonConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int DUN_MAX_WIDTH = get().getInt("dungeon.max.width");

    public static final int DUN_MAX_HEIGHT = get().getInt("dungeon.max.height");

    public static final int DUN_RANDOM_DIR = get().getInt("dungeon.random.direction");

    public static final int DUN_DIR_CHANGE = get().getInt("dungeon.direction.change");

    public static final int DUN_TUNNELING = get().getInt("dungeon.tunneling");

    public static final int DUN_ROOMS_MEAN = get().getInt("dungeon.rooms.mean");

    public static final int DUN_ROOM_DOORS = get().getInt("dungeon.room.doors");

    public static final int DUN_TUNNEL_DOORS = get().getInt("dungeon.tunnel.doors");

    public static final int DUN_STREAMER_DENSITY = get().getInt("dungeon.streamer.density");

    public static final int DUN_STREAMER_WIDTH = get().getInt("dungeon.streamer.width");

    public static final int DUN_MAGMA_STREAMER = get().getInt("dungeon.magma.streamer");

    public static final int DUN_MAGMA_TREASURE = get().getInt("dungeon.magma.treasure");

    public static final int DUN_QUARTZ_STREAMER = get().getInt("dungeon.quartz.streamer");

    public static final int DUN_QUARTZ_TREASURE = get().getInt("dungeon.quartz.treasure");

    public static final int DUN_UNUSUAL_ROOMS = get().getInt("dungeon.unusual.rooms");
}
