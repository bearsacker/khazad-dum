package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.NORMAL;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeLife extends AbstractAffixe {

    public AffixeLife() {
        name = "+%d to Life";
        type = NORMAL;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 10}, {5, 10, 20}, {10, 20, 30}, {15, 30, 40}, {20, 40, 60}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setLife(character.getLife() + value);
    }

}
