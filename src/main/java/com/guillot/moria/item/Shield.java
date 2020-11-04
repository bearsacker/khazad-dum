package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.SHIELD;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Shield extends AbstractItem implements Passive {

    public Shield() {
        this.type = SHIELD;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Buckler", 1, 1, 5, 0, ITEMS.getSubImage(6, 11)), //
                new ItemRepresentation("Small Shield", 5, 3, 8, 25, ITEMS.getSubImage(6, 11)), //
                new ItemRepresentation("Large Shield", 9, 5, 10, 40, ITEMS.getSubImage(6, 11)), //
                new ItemRepresentation("Kite Shield", 14, 8, 15, 50, ITEMS.getSubImage(6, 11)), //
                new ItemRepresentation("Tower Shield", 20, 12, 20, 60, ITEMS.getSubImage(6, 11)), //
                new ItemRepresentation("Gothic Shield", 23, 17, 30, 80, ITEMS.getSubImage(6, 11)));
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setArmor(character.getArmor() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setArmor(character.getArmor() - this.value);
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
        return character.getStrength() >= this.requirement;
    }

}
