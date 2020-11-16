package com.guillot.moria.ressources;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    GRANITE_WALL("sprites/granite_wall.png", 64, 96), //
    MAGMA_WALL("sprites/magma_wall.png", 64, 96), //
    QUARTZ_WALL("sprites/quartz_wall.png", 64, 96), //
    LIGHT_FLOOR("sprites/light_floor.png"), //
    DARK_FLOOR("sprites/dark_floor.png"), //
    PILLAR("sprites/pillar.png"), //
    STAIRS("sprites/stairs.png", 64, 96), //
    DOOR("sprites/door.png", 64, 96), //
    HUMAN("sprites/human.png", 64, 96), //
    ELF("sprites/human.png", 64, 96), //
    DWARF("sprites/human.png", 64, 96), //
    HOBBIT("sprites/human.png", 64, 96), //
    CURSOR("sprites/cursor.png", 64, 96), //
    ITEMS("sprites/items.png", 16, 16), //
    BAG("sprites/bag.png"), //
    RUBBLE("sprites/rubble.png", 64, 96), //
    PARCHMENT("sprites/parchment.png");

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
