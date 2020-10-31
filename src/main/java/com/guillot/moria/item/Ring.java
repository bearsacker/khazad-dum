package com.guillot.moria.item;

import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.RING;

import com.guillot.engine.character.AbstractCharacter;

public class Ring extends AbstractItem {

    public Ring() {
        this.type = RING;
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
    public boolean isEligible() {
        return !this.rarity.equals(NORMAL);
    }
}
