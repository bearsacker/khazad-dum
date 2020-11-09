package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.BOW;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Bow extends AbstractItem implements Equipable {

    public Bow() {
        type = BOW;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Short Bow", 1, 1, 4, 0, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Long Bow", 5, 1, 6, 30, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Hunter's Bow", 3, 2, 5, 35, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Composite Bow", 7, 3, 6, 40, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Short Battle Bow", 9, 3, 7, 50, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Long Battle Bow", 11, 1, 10, 60, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Short War Bow", 15, 4, 8, 70, ITEMS.getSubImage(11, 9)), //
                new ItemRepresentation("Long War Bow", 19, 1, 14, 80, ITEMS.getSubImage(11, 9)));
    }

    @Override
    public String getValueName() {
        return "Damage";
    }

    @Override
    public String getRequirementName() {
        return "Dexterity";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getAgility() >= requirement;
    }

    @Override
    public void equip(AbstractCharacter character) {
        character.setDamages(character.getDamages() + value);
        setAffixesPassiveEffects(character);
    }
}
