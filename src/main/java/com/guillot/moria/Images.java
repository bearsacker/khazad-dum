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
    HUMAN_WARRIOR("sprites/human_warrior.png");

    private Image image;

    private Images(String path) {
        try {
            image = new Image(path);
            image.setFilter(Image.FILTER_NEAREST);
        } catch (SlickException e) {
        }
    }

    public Image getImage() {
        return image;
    }
}
