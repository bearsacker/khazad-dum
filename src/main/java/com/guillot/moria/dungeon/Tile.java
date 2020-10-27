package com.guillot.moria.dungeon;


public enum Tile {
    NULL, //
    TMP1_WALL, //
    TMP2_WALL, //
    DARK_FLOOR, //
    LIGHT_FLOOR, //
    CORRIDOR_FLOOR, //
    PILLAR(true, false), //
    GRANITE_WALL(true, false), //
    MAGMA_WALL(true, false), //
    QUARTZ_WALL(true, false), //
    BOUNDARY_WALL(true, false), //
    SECRET_DOOR(false, true), //
    OPEN_DOOR(false, true), //
    CLOSED_DOOR(false, true), //
    UP_STAIR, //
    DOWN_STAIR;

    public boolean isWall;

    public boolean isDoor;

    private Tile() {
        this.isWall = false;
        this.isDoor = false;
    }

    private Tile(boolean isWall, boolean isDoor) {
        this.isWall = isWall;
        this.isDoor = isDoor;
    }
}
