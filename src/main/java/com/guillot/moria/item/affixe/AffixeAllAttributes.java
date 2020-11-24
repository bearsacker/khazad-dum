package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.LEGENDARY;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeAllAttributes extends AbstractAffixe {

    private static final long serialVersionUID = -4565320136195088139L;

    public AffixeAllAttributes() {
        name = "+%d to All Attributes";
        type = LEGENDARY;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {5, 5, 10}, {10, 10, 15}, {15, 15, 20}, {20, 20, 25}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() + value);
        character.setStrength(character.getStrength() + value);
        character.setAgility(character.getAgility() + value);
        character.setDestiny(character.getDestiny() + value);
    }

}
