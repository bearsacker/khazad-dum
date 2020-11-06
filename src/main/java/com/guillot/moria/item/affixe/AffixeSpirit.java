package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeSpirit extends AbstractAffixe {

    public AffixeSpirit() {
        name = "+%d to Spirit";
        type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {5, 5, 10}, {10, 10, 15}, {15, 15, 20}, {20, 20, 25}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() + value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() - value);
    }
}
