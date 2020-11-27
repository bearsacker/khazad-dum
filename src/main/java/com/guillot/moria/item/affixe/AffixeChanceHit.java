package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.affixe.AffixeRarity.RARE;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;

public class AffixeChanceHit extends AbstractAffixe {

    private static final long serialVersionUID = 6237313089637360419L;

    public AffixeChanceHit() {
        type = RARE;
        excludedItemType = asList(new ItemType[] {ARMOR, HELMET, SHIELD});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {10, 5, 10}, {20, 10, 15}, {30, 15, 20}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setChanceHit(character.getChanceHit() + value);
    }

    @Override
    public String getName() {
        return "+%d%% to Chance to hit";
    }
}
