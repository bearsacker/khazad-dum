package com.guillot.moria.dungeon;

import com.guillot.moria.utils.RNG;

public enum Direction {
    NORTHWEST, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST;

    public static Direction random() {
        int index = RNG.get().randomNumber(Direction.values().length - 1);

        return Direction.values()[index];
    }
}
