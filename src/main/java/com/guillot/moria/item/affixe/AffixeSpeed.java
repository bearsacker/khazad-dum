package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.item.affixe.AffixeRarity.RARE;
import static java.util.Arrays.asList;

import com.guillot.engine.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixeSpeed extends AbstractAffixe {

    public AffixeSpeed() {
        this.name = "+%d%% to Speed";
        this.type = RARE;
        this.excludedItemType = asList(new ItemType[] {BOW, ONE_HANDED_WEAPON, STAFF, TWO_HANDED_WEAPON});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {5, 5, 10}, {10, 10, 15}, {15, 15, 20}, {20, 20, 25}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setSpeed(character.getSpeed() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setSpeed(character.getSpeed() - this.value);
    }
}
