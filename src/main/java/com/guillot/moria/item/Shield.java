package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.SHIELD;

import com.guillot.engine.character.AbstractCharacter;

public class Shield extends AbstractItem {

    public Shield() {
        this.type = SHIELD;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5, 0}, {5, 3, 8, 25}, {9, 5, 10, 40}, {14, 8, 15, 50}, {20, 12, 20, 60}, {23, 17, 30, 80}};
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
