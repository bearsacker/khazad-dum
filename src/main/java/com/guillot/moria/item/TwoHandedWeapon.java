package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;

import com.guillot.engine.character.AbstractCharacter;

public class TwoHandedWeapon extends AbstractItem {

    public TwoHandedWeapon() {
        this.type = TWO_HANDED_WEAPON;
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
