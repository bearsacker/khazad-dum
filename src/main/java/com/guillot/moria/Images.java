package com.guillot.moria;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    GRANITE_WALL("sprites/granite_wall.png"), //
    MAGMA_WALL("sprites/magma_wall.png"), //
    QUARTZ_WALL("sprites/quartz_wall.png"), //
    LIGHT_FLOOR("sprites/light_floor.png"), //
    DARK_FLOOR("sprites/dark_floor.png"), //
    PILLAR("sprites/pillar.png"), //
    UP_STAIRS("sprites/up_stairs.png"), //
    DOWN_STAIRS("sprites/down_stairs.png"), //
    DOOR("sprites/door.png"), //
    HUMAN("sprites/human.png"), //
    CURSOR("sprites/cursor.png", 64, 96), //
    ITEMS("sprites/items.png", 16, 16);

    private Image image;

    private int frameWidth;

    private int frameHeight;

    private Images(String path) {
        this(path, 0, 0);
    }

    private Images(String path, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        try {
            image = new Image(path);
            image.setFilter(Image.FILTER_NEAREST);
        } catch (SlickException e) {
        }
    }

    public Image getImage() {
        return image;
    }

    public Image getSubImage(int x, int y) {
        return image.getSubImage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
    }
}
