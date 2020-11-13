package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.KEY;
import static com.guillot.moria.ressources.Images.ITEMS;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;


public class Key extends AbstractItem {

    public Key() {
        type = KEY;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return asList(new ItemRepresentation("Key", 1, 0, 0, 0, ITEMS.getSubImage(11, 3)));
    }

    @Override
    public void generateBase(int qualityLevel) {
        super.generateBase(qualityLevel);
        affixes = new ArrayList<>();
        rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return null;
    }
}
