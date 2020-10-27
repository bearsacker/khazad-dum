package com.guillot.moria.utils;

public enum Direction {
    NORTHWEST, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST;

    public static Direction random() {
        int index = RNG.get().randomNumber(Direction.values().length - 1);

        return Direction.values()[index];
    }
}
