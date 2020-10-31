package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.engine.character.AbstractCharacter;

public class AffixeLightRadius extends AbstractAffixe {

    public AffixeLightRadius() {
        this.name = "+%dm to Light Radius";
        this.type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 1}, {5, 1, 2}, {10, 2, 2}, {15, 2, 3}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setLightRadius(character.getLightRadius() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setLightRadius(character.getLightRadius() - this.value);
    }
}
