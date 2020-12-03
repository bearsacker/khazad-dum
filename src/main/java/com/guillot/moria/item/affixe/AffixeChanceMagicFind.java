package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import java.util.Arrays;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixeChanceMagicFind extends AbstractAffixe {

    private static final long serialVersionUID = 2815321090989489537L;

    public AffixeChanceMagicFind() {
        type = RARE;
        excludedItemType = Arrays.asList(new ItemType[] {BOW, ONE_HANDED_WEAPON, STAFF, TWO_HANDED_WEAPON});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 3}, {5, 3, 5}, {10, 5, 8}, {15, 8, 10}, {20, 10, 20}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setChanceMagicFind(character.getChanceMagicFind() + value);
    }

    @Override
    public String getName() {
        return "+%d%% to Chance to magic find";
    }
}
