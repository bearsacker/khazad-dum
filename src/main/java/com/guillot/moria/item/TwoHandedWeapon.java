package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class TwoHandedWeapon extends AbstractItem {

    public TwoHandedWeapon() {
        this.type = TWO_HANDED_WEAPON;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("War Hammer", 5, 5, 9, 40, ITEMS.getSubImage(7, 8)), //
                new ItemRepresentation("Broad Axe", 8, 8, 20, 50, ITEMS.getSubImage(4, 7)), //
                new ItemRepresentation("Maul", 10, 6, 20, 55, ITEMS.getSubImage(7, 8)), //
                new ItemRepresentation("Battle Axe", 10, 10, 25, 65, ITEMS.getSubImage(4, 7)), //
                new ItemRepresentation("Great Axe", 12, 12, 30, 80, ITEMS.getSubImage(4, 7)), //
                new ItemRepresentation("Two-Handed Sword", 14, 8, 16, 65, ITEMS.getSubImage(1, 8)), //
                new ItemRepresentation("Great Sword", 17, 10, 20, 75, ITEMS.getSubImage(1, 8)));
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
        return character.getStrength() >= this.requirement;
    }
}
