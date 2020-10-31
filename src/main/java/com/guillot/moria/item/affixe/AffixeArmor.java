package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.item.affixe.AffixeRarity.NORMAL;
import static java.util.Arrays.asList;

import com.guillot.engine.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixeArmor extends AbstractAffixe {

    public AffixeArmor() {
        this.name = "+%d to Armor";
        this.type = NORMAL;
        this.excludedItemType = asList(new ItemType[] {BOW, ONE_HANDED_WEAPON, STAFF, TWO_HANDED_WEAPON});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 10}, {5, 5, 15}, {10, 10, 20}, {15, 15, 25}, {20, 20, 40}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setArmor(character.getArmor() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setArmor(character.getArmor() - this.value);
    }
}
