package com.guillot.moria.item;

public enum ItemBlock {
    LEFT_HAND("Left hand"), //
    RIGHT_HAND("Right hand"), //
    TWO_HANDS("Two hands"), //
    FINGER("Finger"), //
    NECK("Neck"), //
    HEAD("Head"), //
    BODY("Body"), //
    NONE("None");

    private String name;

    private ItemBlock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
