package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.LEGENDARY;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeAllAttributes extends AbstractAffixe {

    private static final long serialVersionUID = -4565320136195088139L;

    public AffixeAllAttributes() {
        type = LEGENDARY;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 2}, {5, 3, 4}, {10, 5, 6}, {15, 7, 8}, {20, 9, 10}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() + value);
        character.setStrength(character.getStrength() + value);
        character.setAgility(character.getAgility() + value);
        character.setDestiny(character.getDestiny() + value);
    }

    @Override
    public String getName() {
        return "+%d to @@ITEM_LEGENDARY@@All Attributes";
    }
}
