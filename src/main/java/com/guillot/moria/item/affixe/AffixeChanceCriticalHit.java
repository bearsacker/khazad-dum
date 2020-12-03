package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.affixe.AffixeRarity.LEGENDARY;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;

public class AffixeChanceCriticalHit extends AbstractAffixe {

    private static final long serialVersionUID = -387211472651229146L;

    public AffixeChanceCriticalHit() {
        type = LEGENDARY;
        excludedItemType = asList(new ItemType[] {ARMOR, SHIELD});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 3}, {5, 3, 5}, {10, 5, 8}, {15, 8, 10}, {20, 10, 20}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setChanceHit(character.getChanceHit() + value);
    }

    @Override
    public String getName() {
        return "+%d%% to Chance to critical hit";
    }
}
