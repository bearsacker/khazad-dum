package com.guillot.moria.dungeon;

import com.guillot.moria.utils.RNG;

public enum Direction {
    NORTHWEST(4), NORTH(0), NORTHEAST(5), EAST(1), SOUTHEAST(6), SOUTH(2), SOUTHWEST(7), WEST(3);

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
