package com.guillot.moria.item;

import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.ItemType.AMULET;

import com.guillot.engine.character.AbstractCharacter;

public class Amulet extends AbstractItem {

    public Amulet() {
        this.type = AMULET;
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
