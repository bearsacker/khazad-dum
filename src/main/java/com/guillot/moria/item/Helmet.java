package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.HELMET;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.resources.Images;

public class Helmet extends AbstractItem implements Equipable {

    private static final long serialVersionUID = -9079746128902282679L;

    public Helmet() {
        type = HELMET;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Cap", 1, 1, 3, 0, Images.CAP), //
                new ItemRepresentation("Great Cap", 4, 2, 4, 0, Images.GREAT_CAP), //
                new ItemRepresentation("Helm", 8, 4, 6, 25, Images.HELM), //
                new ItemRepresentation("Full Helm", 12, 6, 8, 36, Images.FULL_HELM), //
                new ItemRepresentation("Crown", 16, 8, 12, 0, Images.CROWN), //
                new ItemRepresentation("Great Helm", 20, 10, 15, 50, Images.GREAT_HELM));
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
