package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.affixe.AffixeRarity.LEGENDARY;

import com.guillot.moria.character.AbstractCharacter;

public class AffixeAllAttributes extends AbstractAffixe {

    public AffixeAllAttributes() {
        this.name = "+%d to All Attributes";
        this.type = LEGENDARY;
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
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setSpirit(character.getSpirit() - value);
        character.setStrength(character.getStrength() - value);
        character.setAgility(character.getAgility() - value);
    }
}
