package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.BAG;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.utils.RNG;


public class Bag extends AbstractItem implements Usable {

    public Bag() {
        type = BAG;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return asList(new ItemRepresentation("Mysterious bag", 1, 0, 0, 0, ITEMS.getSubImage(12, 11)));
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

    @Override
    public boolean use(AbstractCharacter character) {
        AbstractItem item = ItemGenerator.generateItem(character.getChanceMagicFind(), character.getLevel() + RNG.get().randomNumber(5));
        if (item != null) {
            return character.pickUpItem(item);
        }

        return false;
    }
}
