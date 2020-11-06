package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.STAFF;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Staff extends AbstractItem implements Equipable {

    public Staff() {
        type = STAFF;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Short Staff", 1, 2, 4, 0, ITEMS.getSubImage(0, 11)), //
                new ItemRepresentation("Long Staff", 4, 4, 8, 0, ITEMS.getSubImage(1, 11)), //
                new ItemRepresentation("Composite Staff", 6, 5, 10, 0, ITEMS.getSubImage(2, 11)), //
                new ItemRepresentation("Quarter Staff", 9, 6, 12, 20, ITEMS.getSubImage(3, 11)), //
                new ItemRepresentation("War Staff", 12, 8, 16, 30, ITEMS.getSubImage(4, 11)));
    }

    @Override
    public String getValueName() {
        return "Damage";
    }

    @Override
    public String getRequirementName() {
        return "Strength";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getStrength() >= requirement;
    }

    @Override
    public void equip(AbstractCharacter character) {
        setAffixesPassiveEffects(character);
    }

    @Override
    public void unequip(AbstractCharacter character) {
        unsetAffixesPassiveEffects(character);
    }
}
