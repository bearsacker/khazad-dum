package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeStrength extends AbstractAffixe {

    private static final long serialVersionUID = 2806758303840204814L;

    public AffixeStrength() {
        type = RARE;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 2}, {5, 3, 4}, {10, 5, 6}, {15, 7, 8}, {20, 9, 10}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setStrength(character.getStrength() + value);
    }

    @Override
    public String getName() {
        return "+%d to @@RED_PALE@@Strength";
    }
}
