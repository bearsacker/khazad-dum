package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.RING;

import java.util.Arrays;
import java.util.List;

public class Ring extends AbstractItem {

    public Ring() {
        this.type = RING;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(new ItemRepresentation("Ring", 1, 0, 0, 0, ITEMS.getSubImage(2, 2)));
    }

    @Override
    public boolean isEligible() {
        return super.isEligible() && !this.rarity.equals(NORMAL);
    }
}
