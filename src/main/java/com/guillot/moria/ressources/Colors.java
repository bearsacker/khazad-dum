package com.guillot.moria.ressources;

import org.newdawn.slick.Color;

import com.guillot.engine.gui.GUI;

public enum Colors {
    WHITE("WHITE", new Color(255, 255, 255)), //
    BLACK("BLACK", new Color(0, 0, 0)), //
    GRAY("GRAY", new Color(128, 128, 128)), //
    DARK_GRAY("DARK_GRAY", new Color(64, 64, 64)), //
    DARKER_GRAY("DARKER_GRAY", new Color(38, 38, 38)), //
    LIGHT_GRAY("LIGHT_GRAY", new Color(220, 220, 220)), //
    YELLOW("YELLOW", new Color(223, 207, 134)), //
    ROSE_PALE("ROSE_PALE", new Color(240, 220, 220)), //
    RED_PALE("RED_PALE", new Color(255, 160, 122)), //
    BLUE_PALE("BLUE_PALE", new Color(173, 216, 230)), //
    YELLOW_PALE("YELLOW_PALE", new Color(255, 255, 204)), //
    GREEN_PALE("GREEN_PALE", new Color(200, 230, 201)), //

    TRANSPARENT("TRANSPARENT", new Color(0, 0, 0, 0)), //
    MAP_LOCKED_DOOR("MAP_LOCKED_DOOR", new Color(255, 0, 0)), //
    MAP_OPEN_DOOR("MAP_OPEN_DOOR", new Color(0, 255, 0)), //

    ITEM_BLOCK("ITEM_BLOCK", new Color(1f, 1f, 1f, .2f)), //
    ITEM_SELECTED("ITEM_SELECTED", new Color(255, 255, 255, 128)), //
    ITEM_NOT_EQUIPABLE("ITEM_NOT_EQUIPABLE", new Color(255, 0, 0, 128)), //
    ITEM_EQUIPED("ITEM_EQUIPED", new Color(255, 255, 0, 128)), //
    ITEM_MAGIC("ITEM_MAGIC", new Color(Color.cyan)), //
    ITEM_LEGENDARY("ITEM_LEGENDARY", new Color(Color.yellow));

    private Color color;

    private Colors(String name, Color color) {
        this.color = color;
        GUI.get().registerColor(name, color);
    }

    public Color getColor() {
        return color;
    }
}
