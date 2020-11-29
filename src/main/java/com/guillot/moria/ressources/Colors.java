package com.guillot.moria.ressources;

import org.newdawn.slick.Color;

public enum Colors {
    WHITE(new Color(255, 255, 255)), //
    LIGHT_GREY(new Color(220, 220, 220)), //
    YELLOW(new Color(223, 207, 134)), //
    ROSE_PALE(new Color(240, 220, 220)), //
    RED_PALE(new Color(255, 160, 122)), //
    BLUE_PALE(new Color(173, 216, 230)), //
    YELLOW_PALE(new Color(255, 255, 204)), //
    GREEN_PALE(new Color(200, 230, 201)), //

    TRANSPARENT(new Color(0, 0, 0, 0)), //
    MAP_LOCKED_DOOR(new Color(255, 0, 0)), //
    MAP_OPEN_DOOR(new Color(0, 255, 0)), //

    ITEM_BLOCK(new Color(1f, 1f, 1f, .2f)), //
    ITEM_SELECTED(new Color(255, 255, 255, 128)), //
    ITEM_NOT_EQUIPABLE(new Color(255, 0, 0, 128)), //
    ITEM_EQUIPED(new Color(255, 255, 0, 128)), //
    ITEM_MAGIC(new Color(Color.cyan)), //
    ITEM_LEGENDARY(new Color(Color.yellow));

    private Color color;

    private Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
