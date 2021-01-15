package com.guillot.moria.item;

import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.RING;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.resources.Images;

public class Ring extends AbstractItem implements Equipable {

    private static final long serialVersionUID = -6474860957858264849L;

    public Ring() {
        type = RING;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Ring", 1, 0, 0, 0, Images.RING_1), //
                new ItemRepresentation("Ring", 1, 0, 0, 0, Images.RING_2), //
                new ItemRepresentation("Ring", 1, 0, 0, 0, Images.RING_3), //
                new ItemRepresentation("Ring", 1, 0, 0, 0, Images.RING_4), //
                new ItemRepresentation("Ring", 1, 0, 0, 0, Images.RING_5));
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
