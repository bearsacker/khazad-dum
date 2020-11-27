package com.guillot.moria.ressources;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    LOGO("sprites/logo.png"), //
    LOGO_TEXT("sprites/logo-text.png"), //

    GRANITE_WALL("sprites/granite_wall.png", 64, 96), //
    MAGMA_WALL("sprites/magma_wall.png", 64, 96), //
    QUARTZ_WALL("sprites/quartz_wall.png", 64, 96), //
    ROOM_FLOOR("sprites/room_floor.png"), //
    CORRIDOR_FLOOR("sprites/corridor_floor.png"), //
    STAIRS("sprites/stairs.png", 64, 96), //

    DOOR("sprites/door.png", 64, 96), //

    PILLAR("sprites/pillar.png"), //
    RUBBLE("sprites/rubble.png", 64, 96), //

    HUMAN("sprites/human.png", 64, 96), //
    ELF("sprites/human.png", 64, 96), //
    DWARF("sprites/human.png", 64, 96), //
    HOBBIT("sprites/human.png", 64, 96), //
    MONSTER("sprites/monster.png", 64, 96), //

    CURSOR("sprites/cursor.png", 64, 96), //
    PARCHMENT("sprites/parchment.png"), //
    MAP_CURSOR("sprites/map_cursor.png"), //

    DICE("sprites/items.png", 16, 16, 12, 11), //
    COPPER("sprites/items.png", 16, 16, 1, 5), //
    SILVER("sprites/items.png", 16, 16, 2, 5), //
    GARNETS("sprites/items.png", 16, 16, 0, 3), //
    GOLD("sprites/items.png", 16, 16, 1, 6), //
    OPALS("sprites/items.png", 16, 16, 6, 3), //
    SAPPHIRES("sprites/items.png", 16, 16, 1, 3), //
    GOLD_COINS("sprites/items.png", 16, 16, 8, 3), //
    RUBIES("sprites/items.png", 16, 16, 3, 3), //
    DIAMONDS("sprites/items.png", 16, 16, 5, 3), //
    EMERALDS("sprites/items.png", 16, 16, 2, 3), //
    MITHRIL("sprites/items.png", 16, 16, 6, 5), //
    DAGGER("sprites/items.png", 16, 16, 0, 7), //
    AXE("sprites/items.png", 16, 16, 9, 6), //
    LARGE_AXE("sprites/items.png", 16, 16, 8, 6), //
    SWORD("sprites/items.png", 16, 16, 1, 7), //
    LONG_SWORD("sprites/items.png", 16, 16, 2, 7), //
    BASTARD_SWORD("sprites/items.png", 16, 16, 3, 7), //
    MACE("sprites/items.png", 16, 16, 6, 8), //
    SHORT_STAFF("sprites/items.png", 16, 16, 0, 11), //
    LONG_STAFF("sprites/items.png", 16, 16, 1, 11), //
    COMPOSITE_STAFF("sprites/items.png", 16, 16, 2, 11), //
    QUARTER_STAFF("sprites/items.png", 16, 16, 3, 11), //
    WAR_STAFF("sprites/items.png", 16, 16, 4, 11), //
    HAMMER("sprites/items.png", 16, 16, 7, 8), //
    TWO_HANDED_AXE("sprites/items.png", 16, 16, 4, 7), //
    TWO_HANDED_SWORD("sprites/items.png", 16, 16, 1, 8), //
    BAG("sprites/bag.png"), //
    AMULET("sprites/items.png", 16, 16, 7, 1), //
    RING("sprites/items.png", 16, 16, 2, 2), //
    KEY("sprites/items.png", 16, 16, 11, 3), //
    HEALTH_POTION("sprites/items.png", 16, 16, 11, 4), //
    ARMOR("sprites/items.png", 16, 16, 9, 9), //
    BOW("sprites/items.png", 16, 16, 11, 9), //
    HELMET("sprites/items.png", 16, 16, 8, 9), //
    SHIELD("sprites/items.png", 16, 16, 6, 11), //
    LARGE_SHIELD("sprites/items.png", 16, 16, 8, 11);

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

    private Images(String path, int frameWidth, int frameHeight, int x, int y) {
        try {
            image = new Image(path).getSubImage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
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
