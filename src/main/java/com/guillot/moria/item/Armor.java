package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.ARMOR;

import com.guillot.moria.character.AbstractCharacter;

public class Armor extends AbstractItem {

    public Armor() {
        this.type = ARMOR;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 2, 6, 0}, {2, 3, 7, 0}, {3, 4, 7, 0}, {4, 7, 10, 0}, {6, 10, 13, 0}, {7, 11, 14, 0}, {9, 15, 17, 20},
                {11, 17, 20, 25}, {13, 18, 22, 30},
                {15, 23, 28, 35}, {16, 20, 24, 40}, {17, 30, 35, 65}, {21, 40, 45, 65}, {23, 50, 60, 80}, {25, 60, 75, 90}};
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
