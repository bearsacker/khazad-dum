package com.guillot.moria.dungeon;

import com.guillot.moria.utils.RNG;

public enum Direction {
    NORTHWEST(0), NORTH(1), NORTHEAST(2), EAST(3), SOUTHEAST(4), SOUTH(5), SOUTHWEST(6), WEST(7);

    private int value;

    private Direction(int value) {
        this.value = value;
    }

    public static Direction random() {
        int index = RNG.get().randomNumber(Direction.values().length - 1);

        return Direction.values()[index];
    }

    public int getValue() {
        return value;
    }
}
