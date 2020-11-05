package com.guillot.moria.item;

import com.guillot.moria.character.AbstractCharacter;

public interface Equipable {

    void setPassiveEffect(AbstractCharacter character);

    void unsetPassiveEffect(AbstractCharacter character);

    boolean isEquipable(AbstractCharacter character);
}
