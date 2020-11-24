package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.KEY;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.guillot.moria.ressources.Images;


public class Key extends AbstractItem {

    private static final long serialVersionUID = -5807750943760648132L;

    public Key() {
        type = KEY;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return asList(new ItemRepresentation("Key", 1, 0, 0, 0, Images.KEY));
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
