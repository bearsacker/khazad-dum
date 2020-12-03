package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeSpirit extends AbstractAffixe {

    private static final long serialVersionUID = 1961334588726173217L;

    public AffixeSpirit() {
        type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 2}, {5, 3, 4}, {10, 5, 6}, {15, 7, 8}, {20, 9, 10}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() + value);
    }

    @Override
    public String getName() {
        return "+%d to @@YELLOW_PALE@@Spirit";
    }
}
