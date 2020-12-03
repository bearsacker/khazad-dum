package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.affixe.AffixeRarity.LEGENDARY;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixePhysicalDamage extends AbstractAffixe {

    private static final long serialVersionUID = 1936480382270881871L;

    public AffixePhysicalDamage() {
        type = LEGENDARY;
        excludedItemType = asList(new ItemType[] {ARMOR, HELMET, SHIELD});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 2}, {5, 3, 4}, {10, 5, 6}, {15, 7, 8}, {20, 9, 10}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setPhysicalDamage(character.getPhysicalDamage() + value);
    }

    @Override
    public String getName() {
        return "+%d%% to Pysical Damage";
    }
}
