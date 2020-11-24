package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.ARMOR;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;

public class Armor extends AbstractItem implements Equipable {

    private static final long serialVersionUID = -5949470823903068270L;

    public Armor() {
        type = ARMOR;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Rags", 1, 2, 6, 0, Images.ARMOR), //
                new ItemRepresentation("Cloak", 2, 3, 7, 0, Images.ARMOR), //
                new ItemRepresentation("Robe", 3, 4, 7, 0, Images.ARMOR), //
                new ItemRepresentation("Quilted Armor", 4, 7, 10, 0, Images.ARMOR), //
                new ItemRepresentation("Leather Armor", 6, 10, 13, 0, Images.ARMOR), //
                new ItemRepresentation("Hard Leather Armor", 7, 11, 14, 0, Images.ARMOR), //
                new ItemRepresentation("Studded Leather Armor", 9, 15, 17, 20, Images.ARMOR), //
                new ItemRepresentation("Ring Mail", 11, 17, 20, 25, Images.ARMOR), //
                new ItemRepresentation("Chain Mail", 13, 18, 22, 30, Images.ARMOR), //
                new ItemRepresentation("Scale Mail", 15, 23, 28, 35, Images.ARMOR), //
                new ItemRepresentation("Breast Plate", 16, 20, 24, 40, Images.ARMOR), //
                new ItemRepresentation("Splint Mail", 17, 30, 35, 65, Images.ARMOR), //
                new ItemRepresentation("Field Plate", 21, 40, 45, 65, Images.ARMOR), //
                new ItemRepresentation("Gothic Plate", 23, 50, 60, 80, Images.ARMOR), //
                new ItemRepresentation("Full Plate Mail", 25, 60, 75, 90, Images.ARMOR));
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
