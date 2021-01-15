package com.guillot.moria.item;

import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.AMULET;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.resources.Images;

public class Amulet extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 6899538142375698145L;

    public Amulet() {
        type = AMULET;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Amulet", 1, 0, 0, 0, Images.AMULET_1), //
                new ItemRepresentation("Amulet", 1, 0, 0, 0, Images.AMULET_2), //
                new ItemRepresentation("Amulet", 1, 0, 0, 0, Images.AMULET_3), //
                new ItemRepresentation("Amulet", 1, 0, 0, 0, Images.AMULET_4), //
                new ItemRepresentation("Amulet", 1, 0, 0, 0, Images.AMULET_5));
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
