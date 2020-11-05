package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.HELMET;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;

public class Helmet extends AbstractItem implements Equipable {

    public Helmet() {
        this.type = HELMET;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Cap", 1, 1, 3, 0, ITEMS.getSubImage(8, 9)), //
                new ItemRepresentation("Skull Cap", 4, 2, 4, 0, ITEMS.getSubImage(8, 9)), //
                new ItemRepresentation("Helm", 8, 4, 6, 25, ITEMS.getSubImage(8, 9)), //
                new ItemRepresentation("Full Helm", 12, 6, 8, 36, ITEMS.getSubImage(8, 9)), //
                new ItemRepresentation("Crown", 16, 8, 12, 0, ITEMS.getSubImage(8, 9)), //
                new ItemRepresentation("Great Helm", 20, 10, 15, 50, ITEMS.getSubImage(8, 9)));
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
