package com.guillot.moria.dungeon;

import org.newdawn.slick.Image;

import com.guillot.moria.ressources.Images;

public enum Entity {
    PILLAR(Images.PILLAR.getImage()), //
    RUBBLE(Images.RUBBLE.getImage());

    private Image image;

    private Entity(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
