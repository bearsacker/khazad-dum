package com.guillot.moria.dungeon;

import org.newdawn.slick.Image;

import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.ImageBuffer;

public enum Tile {
    NULL, //
    TMP1_WALL(null, false), //
    TMP2_WALL(null, false), //
    ROOM_FLOOR(Images.ROOM_FLOOR.getImage(), true), //
    CORRIDOR_FLOOR(Images.CORRIDOR_FLOOR.getImage(), true), //
    GRANITE_WALL(Images.GRANITE_WALL.getImage(), false), //
    MAGMA_WALL(Images.MAGMA_WALL.getImage(), false), //
    QUARTZ_WALL(Images.QUARTZ_WALL.getImage(), false);

    public boolean isTraversable;

    public Image image;

    public ImageBuffer buffer;

    public ImageBuffer bufferAlternate;

    private Tile() {
        this(null, false);
    }

    private Tile(Image image, boolean isTraversable) {
        this.image = image;
        if (image != null) {
            this.buffer = new ImageBuffer(image.getSubImage(0, 0, 64, 96));
            this.bufferAlternate = new ImageBuffer(image.getSubImage(64, 0, 64, 96));
        }
        this.isTraversable = isTraversable;
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
