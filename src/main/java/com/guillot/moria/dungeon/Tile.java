package com.guillot.moria.dungeon;

import org.newdawn.slick.Image;

import com.guillot.moria.Images;

public enum Tile {
    NULL, //
    TMP1_WALL, //
    TMP2_WALL, //
    DARK_FLOOR(Images.LIGHT_FLOOR.getImage(), true, false, false), //
    LIGHT_FLOOR(Images.LIGHT_FLOOR.getImage(), true, false, false), //
    CORRIDOR_FLOOR(Images.DARK_FLOOR.getImage(), true, false, false), //
    PILLAR(Images.PILLAR.getImage(), false, true, false), //
    GRANITE_WALL(Images.GRANITE_WALL.getImage(), false, true, false), //
    MAGMA_WALL(Images.MAGMA_WALL.getImage(), false, true, false), //
    QUARTZ_WALL(Images.QUARTZ_WALL.getImage(), false, true, false), //
    SECRET_DOOR(Images.GRANITE_WALL.getImage(), false, false, true), //
    OPEN_DOOR(Images.DOOR.getImage(), false, false, true), //
    CLOSED_DOOR(Images.DOOR.getImage(), false, false, true), //
    UP_STAIR(Images.UP_STAIRS.getImage(), false, false, true), //
    DOWN_STAIR(Images.DOWN_STAIRS.getImage(), false, false, true);

    public boolean isFloor;

    public boolean isWall;

    public boolean isDoor;

    public Image image;

    private Tile() {
        this(null, false, false, false);
    }

    private Tile(boolean isFloor, boolean isWall, boolean isDoor) {
        this(null, isFloor, isWall, isDoor);
    }

    private Tile(Image image, boolean isFloor, boolean isWall, boolean isDoor) {
        this.image = image;
        this.isFloor = isFloor;
        this.isWall = isWall;
        this.isDoor = isDoor;
    }

    public static int indexOf(Tile tile) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == tile) {
                return i;
            }
        }

        return 0;
    }
}
