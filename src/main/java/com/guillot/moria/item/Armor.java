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
                new ItemRepresentation("Rags", 1, 2, 6, 0, Images.RAGS), //
                new ItemRepresentation("Cloak", 2, 3, 7, 0, Images.CLOAK), //
                new ItemRepresentation("Robe", 3, 4, 7, 0, Images.ROBE), //
                new ItemRepresentation("Quilted Armor", 4, 7, 10, 0, Images.QUILTED_ARMOR), //
                new ItemRepresentation("Leather Armor", 6, 10, 13, 0, Images.LEATHER_ARMOR), //
                new ItemRepresentation("Hard Leather Armor", 7, 11, 14, 0, Images.HARD_LEATHER_ARMOR), //
                new ItemRepresentation("Studded Leather Armor", 9, 15, 17, 20, Images.STUDDED_LEATHER_ARMOR), //
                new ItemRepresentation("Ring Mail", 11, 17, 20, 25, Images.RING_MAIL), //
                new ItemRepresentation("Chain Mail", 13, 18, 22, 30, Images.CHAIN_MAIL), //
                new ItemRepresentation("Scale Mail", 15, 23, 28, 35, Images.SCALE_MAIL), //
                new ItemRepresentation("Breast Plate", 16, 20, 24, 40, Images.BREAST_PLATE), //
                new ItemRepresentation("Splint Mail", 17, 30, 35, 65, Images.SPLINT_MAIL), //
                new ItemRepresentation("Field Plate", 21, 40, 45, 65, Images.FIELD_PLATE), //
                new ItemRepresentation("Gothic Plate", 23, 50, 60, 80, Images.GOTHIC_PLATE), //
                new ItemRepresentation("Full Plate Mail", 25, 60, 75, 90, Images.FULL_PLATE_MAIL));
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
