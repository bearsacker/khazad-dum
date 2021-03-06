package com.guillot.moria.item;

import com.guillot.moria.character.AbstractCharacter;

public interface Equipable {

    void equip(AbstractCharacter character);

    boolean isEquipable(AbstractCharacter character);
}
