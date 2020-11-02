package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.BOW;

import com.guillot.moria.character.AbstractCharacter;

public class Bow extends AbstractItem {

    public Bow() {
        this.type = BOW;
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void use(AbstractCharacter character) {

    }

    @Override
    public String getValueName() {
        return "Damage";
    }

    @Override
    public String getRequirementName() {
        return "Dexterity";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getAgility() >= this.requirement;
    }
}
