package com.guillot.moria.item;

import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.ressources.Images.ITEMS;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Ring extends AbstractItem implements Equipable {

    public Ring() {
        type = RING;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(new ItemRepresentation("Ring", 1, 0, 0, 0, ITEMS.getSubImage(2, 2)));
    }

    @Override
    public boolean isEligible() {
        return super.isEligible() && !rarity.equals(NORMAL);
    }

    @Override
    public void equip(AbstractCharacter character) {
        setAffixesPassiveEffects(character);
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return true;
    }

}
