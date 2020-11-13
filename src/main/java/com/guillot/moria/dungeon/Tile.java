package com.guillot.moria.dungeon;

import org.newdawn.slick.Image;

import com.guillot.moria.ressources.Images;

public enum Tile {
    NULL, //
    TMP1_WALL(null, false, true), //
    TMP2_WALL(null, false, true), //
    DARK_FLOOR(Images.DARK_FLOOR.getImage(), true), //
    LIGHT_FLOOR(Images.LIGHT_FLOOR.getImage(), true), //
    RUBBLE(Images.RUBBLE.getImage(), false, true), //
    CORRIDOR_FLOOR(Images.DARK_FLOOR.getImage(), true), //
    PILLAR(Images.PILLAR.getImage(), false, true), //
    GRANITE_WALL(Images.GRANITE_WALL.getImage(), false, true), //
    MAGMA_WALL(Images.MAGMA_WALL.getImage(), false, true), //
    QUARTZ_WALL(Images.QUARTZ_WALL.getImage(), false, true), //
    UP_STAIR(Images.STAIRS.getSubImage(1, 0), false, false, true), //
    DOWN_STAIR(Images.STAIRS.getSubImage(0, 0), false, false, true);

    public boolean isFloor;

    public boolean isWall;

    public boolean isStairs;

    public Image image;

    private Tile() {
        this(null, false, false, false);
    }

    private Tile(Image image) {
        this(image, false, false, false);
    }

    private Tile(Image image, boolean isFloor) {
        this(image, isFloor, false, false);
    }

    private Tile(Image image, boolean isFloor, boolean isWall) {
        this(image, isFloor, isWall, false);
    }

    private Tile(Image image, boolean isFloor, boolean isWall, boolean isStairs) {
        this.image = image;
        this.isFloor = isFloor;
        this.isWall = isWall;
        this.isStairs = isStairs;
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
