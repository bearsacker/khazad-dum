package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.STAFF;

import com.guillot.moria.character.AbstractCharacter;

public class Staff extends AbstractItem {

    public Staff() {
        this.type = STAFF;
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
        return "Strength";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getStrength() >= this.requirement;
    }
}
