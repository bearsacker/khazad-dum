package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.AMULET;

import java.util.Arrays;
import java.util.List;

public class Amulet extends AbstractItem {

    public Amulet() {
        this.type = AMULET;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(new ItemRepresentation("Amulet", 1, 0, 0, 0, ITEMS.getSubImage(7, 1)));
    }

    @Override
    public boolean isEligible() {
        return super.isEligible() && !this.rarity.equals(NORMAL);
    }

}
