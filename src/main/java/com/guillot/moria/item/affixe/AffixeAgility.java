package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeAgility extends AbstractAffixe {

    public AffixeAgility() {
        name = "+%d to Agility";
        type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {5, 5, 10}, {10, 10, 15}, {15, 15, 20}, {20, 20, 25}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setAgility(character.getAgility() + value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setAgility(character.getAgility() - value);
    }
}
