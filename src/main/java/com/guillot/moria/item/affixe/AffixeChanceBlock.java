package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.item.affixe.AffixeRarity.NORMAL;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixeChanceBlock extends AbstractAffixe {

    public AffixeChanceBlock() {
        name = "+%d%% to Chance to block";
        type = NORMAL;
        excludedItemType = asList(new ItemType[] {ARMOR, HELMET, AMULET, BOW, ONE_HANDED_WEAPON, RING, STAFF, TWO_HANDED_WEAPON});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {5, 5, 10}, {10, 10, 15}, {15, 15, 20}, {20, 20, 30}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setChanceDodge(character.getChanceDodge() + value);
    }

}
