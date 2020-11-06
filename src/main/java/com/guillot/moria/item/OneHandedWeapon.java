package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class OneHandedWeapon extends AbstractItem implements Equipable {

    public OneHandedWeapon() {
        type = ONE_HANDED_WEAPON;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Dagger", 1, 1, 4, 0, ITEMS.getSubImage(0, 7)), //
                new ItemRepresentation("Small Axe", 2, 2, 10, 0, ITEMS.getSubImage(9, 6)), //
                new ItemRepresentation("Axe", 4, 4, 12, 22, ITEMS.getSubImage(9, 6)), //
                new ItemRepresentation("Large Axe", 6, 6, 16, 30, ITEMS.getSubImage(8, 6)), //
                new ItemRepresentation("Short Sword", 1, 2, 6, 18, ITEMS.getSubImage(0, 7)), //
                new ItemRepresentation("Sabre", 1, 1, 8, 17, ITEMS.getSubImage(1, 7)), //
                new ItemRepresentation("Scimitar", 4, 3, 7, 23, ITEMS.getSubImage(1, 7)), //
                new ItemRepresentation("Blade", 4, 3, 8, 25, ITEMS.getSubImage(1, 7)), //
                new ItemRepresentation("Falchion", 2, 4, 8, 30, ITEMS.getSubImage(1, 7)), //
                new ItemRepresentation("Long Sword", 6, 2, 10, 30, ITEMS.getSubImage(2, 7)), //
                new ItemRepresentation("Claymore", 5, 1, 12, 35, ITEMS.getSubImage(2, 7)), //
                new ItemRepresentation("Broad Sword", 8, 4, 12, 40, ITEMS.getSubImage(3, 7)), //
                new ItemRepresentation("Bastard Sword", 10, 6, 15, 50, ITEMS.getSubImage(3, 7)), //
                new ItemRepresentation("Mace", 2, 1, 8, 16, ITEMS.getSubImage(6, 8)), //
                new ItemRepresentation("Morning Star", 3, 1, 10, 26, ITEMS.getSubImage(6, 8)), //
                new ItemRepresentation("Flail", 7, 2, 12, 30, ITEMS.getSubImage(6, 8)));
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
        character.setDamages(character.getDamages() + value);
        setAffixesPassiveEffects(character);
    }

    @Override
    public void unequip(AbstractCharacter character) {
        character.setDamages(character.getDamages() - value);
        unsetAffixesPassiveEffects(character);
    }
}
