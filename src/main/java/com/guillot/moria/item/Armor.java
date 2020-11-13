package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.ressources.Images.ITEMS;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Armor extends AbstractItem implements Equipable {

    public Armor() {
        type = ARMOR;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Rags", 1, 2, 6, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Cloak", 2, 3, 7, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Robe", 3, 4, 7, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Quilted Armor", 4, 7, 10, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Leather Armor", 6, 10, 13, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Hard Leather Armor", 7, 11, 14, 0, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Studded Leather Armor", 9, 15, 17, 20, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Ring Mail", 11, 17, 20, 25, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Chain Mail", 13, 18, 22, 30, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Scale Mail", 15, 23, 28, 35, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Breast Plate", 16, 20, 24, 40, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Splint Mail", 17, 30, 35, 65, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Field Plate", 21, 40, 45, 65, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Gothic Plate", 23, 50, 60, 80, ITEMS.getSubImage(9, 9)), //
                new ItemRepresentation("Full Plate Mail", 25, 60, 75, 90, ITEMS.getSubImage(9, 9)));
    }

    @Override
    public void equip(AbstractCharacter character) {
        character.setArmor(character.getArmor() + value);
        setAffixesPassiveEffects(character);
    }

    @Override
    public String getValueName() {
        return "Armor";
    }

    @Override
    public String getRequirementName() {
        return "Strength";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getStrength() >= requirement;
    }
}
