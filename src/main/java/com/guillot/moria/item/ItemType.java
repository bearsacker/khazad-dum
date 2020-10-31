package com.guillot.moria.item;

import static com.guillot.moria.item.ItemBlock.BODY;
import static com.guillot.moria.item.ItemBlock.FINGER;
import static com.guillot.moria.item.ItemBlock.HEAD;
import static com.guillot.moria.item.ItemBlock.LEFT_HAND;
import static com.guillot.moria.item.ItemBlock.NECK;
import static com.guillot.moria.item.ItemBlock.NONE;
import static com.guillot.moria.item.ItemBlock.RIGHT_HAND;
import static com.guillot.moria.item.ItemBlock.TWO_HANDS;

public enum ItemType {
    RING("Ring", FINGER), //
    AMULET("Amulet", NECK), //
    ONE_HANDED_WEAPON("One handed", RIGHT_HAND), //
    TWO_HANDED_WEAPON("Two handed", TWO_HANDS), //
    BOW("Bow", TWO_HANDS), //
    STAFF("Staff", TWO_HANDS), //
    HELMET("Helmet", HEAD), //
    SHIELD("Shield", LEFT_HAND), //
    ARMOR("Armor", BODY), //
    HEALING_POTION("Healing Potion", NONE);

    private String name;

    private ItemBlock block;

    private ItemType(String name, ItemBlock block) {
        this.name = name;
        this.block = block;
    }

    public ItemBlock getBlock() {
        return this.block;
    }

    @Override
    public String toString() {
        return this.name + " [" + this.block + "]";
    }
}
