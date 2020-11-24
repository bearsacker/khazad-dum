package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.HELMET;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;

public class Helmet extends AbstractItem implements Equipable {

    private static final long serialVersionUID = -9079746128902282679L;

    public Helmet() {
        type = HELMET;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Cap", 1, 1, 3, 0, Images.HELMET), //
                new ItemRepresentation("Skull Cap", 4, 2, 4, 0, Images.HELMET), //
                new ItemRepresentation("Helm", 8, 4, 6, 25, Images.HELMET), //
                new ItemRepresentation("Full Helm", 12, 6, 8, 36, Images.HELMET), //
                new ItemRepresentation("Crown", 16, 8, 12, 0, Images.HELMET), //
                new ItemRepresentation("Great Helm", 20, 10, 15, 50, Images.HELMET));
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
