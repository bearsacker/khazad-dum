package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.HELMET;

import com.guillot.moria.character.AbstractCharacter;

public class Helmet extends AbstractItem {

    public Helmet() {
        this.type = HELMET;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 3, 0}, {4, 2, 4, 0}, {8, 4, 6, 25}, {12, 6, 8, 36}, {16, 8, 12, 0}, {20, 10, 15, 50}};
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
    public void use(AbstractCharacter character) {

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
