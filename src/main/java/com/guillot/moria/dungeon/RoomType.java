package com.guillot.moria.dungeon;

import com.guillot.engine.utils.RandomCollection;

public enum RoomType {
    NORMAL, //
    OVERLAPPING_RECTANGLES(6), //
    CROSS_SHAPED(2), //
    CROSS_SHAPED_PILLAR(2), //
    CROSS_SHAPED_TREASURE(2), //
    INNER_ROOMS_PLAIN, //
    INNER_ROOMS_PILLARS, //
    INNER_ROOMS_TREASURE, //
    INNER_ROOMS_MAZE, //
    INNER_ROOMS_FOUR_ROOMS;

    private static RandomCollection<RoomType> RANDOM;

    private int weight;

    private RoomType() {
        this(1);
    }

    private RoomType(int weight) {
        this.weight = weight;
    }

    public static RoomType randomSpecialRooms() {
        if (RANDOM == null) {
            RANDOM = new RandomCollection<>();
            for (int i = 1; i < RoomType.values().length; i++) {
                RANDOM.add(RoomType.values()[i].weight, RoomType.values()[i]);
            }
        }

        return RANDOM.next();
    }
}
