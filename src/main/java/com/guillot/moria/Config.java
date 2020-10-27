package com.guillot.moria;


public class Config {

    public static final int SCREEN_WIDTH = 66;

    public static final int SCREEN_HEIGHT = 22;

    public static final int QUART_WIDTH = (SCREEN_WIDTH / 4);

    public static final int QUART_HEIGHT = (SCREEN_HEIGHT / 4);

    public static final int DUN_MAX_WIDTH = 198;

    public static final int DUN_MAX_HEIGHT = 66;

    public static final int DUN_RANDOM_DIR = 9; // 1/Chance of Random direction

    public static final int DUN_DIR_CHANGE = 70; // Chance of changing direction (99 max)

    public static final int DUN_TUNNELING = 15; // Chance of extra tunneling

    public static final int DUN_ROOMS_MEAN = 32; // Mean of # of rooms, standard dev2

    public static final int DUN_ROOM_DOORS = 25; // % chance of room doors

    public static final int DUN_TUNNEL_DOORS = 15; // % chance of doors at tunnel junctions

    public static final int DUN_STREAMER_DENSITY = 5; // Density of streamers

    public static final int DUN_STREAMER_WIDTH = 2; // Width of streamers

    public static final int DUN_MAGMA_STREAMER = 3; // Number of magma streamers

    public static final int DUN_MAGMA_TREASURE = 90; // 1/x chance of treasure per magma

    public static final int DUN_QUARTZ_STREAMER = 2; // Number of quartz streamers

    public static final int DUN_QUARTZ_TREASURE = 40; // 1/x chance of treasure per quartz

    public static final int DUN_UNUSUAL_ROOMS = 300; // Level/x chance of unusual room
}
