package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeLightRadius extends AbstractAffixe {

    private static final long serialVersionUID = -1018255241631544465L;

    public AffixeLightRadius() {
        type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 1}, {5, 1, 2}, {10, 2, 2}, {15, 2, 3}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setLightRadius(character.getLightRadius() + value);
    }

    @Override
    public String getName() {
        return "+%dm to Light Radius";
    }
}
