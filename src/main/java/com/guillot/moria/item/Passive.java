package com.guillot.moria.item;

import com.guillot.moria.character.AbstractCharacter;

public interface Passive {

    void setPassiveEffect(AbstractCharacter character);

    void unsetPassiveEffect(AbstractCharacter character);
}
