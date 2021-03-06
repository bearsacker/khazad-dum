package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.SHIELD;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.resources.Images;

public class Shield extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 1874320653164444891L;

    public Shield() {
        type = SHIELD;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Buckler", 1, 1, 5, 0, Images.BUCKLER), //
                new ItemRepresentation("Small Shield", 5, 3, 8, 25, Images.SMALL_SHIELD), //
                new ItemRepresentation("Large Shield", 9, 5, 10, 40, Images.LARGE_SHIELD), //
                new ItemRepresentation("Kite Shield", 14, 8, 15, 50, Images.KITE_SHIELD), //
                new ItemRepresentation("Tower Shield", 20, 12, 20, 60, Images.TOWER_SHIELD), //
                new ItemRepresentation("Gothic Shield", 23, 17, 30, 80, Images.GOTHIC_SHIELD));
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
