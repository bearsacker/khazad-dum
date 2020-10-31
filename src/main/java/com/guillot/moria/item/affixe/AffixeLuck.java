package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.item.affixe.AffixeRarity.RARE;

import java.util.Arrays;

import com.guillot.engine.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;


public class AffixeLuck extends AbstractAffixe {

    public AffixeLuck() {
        this.name = "+%d%% to Magic Find";
        this.type = RARE;
        this.excludedItemType = Arrays.asList(new ItemType[] {BOW, ONE_HANDED_WEAPON, STAFF, TWO_HANDED_WEAPON});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {10, 5, 10}, {20, 10, 15}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setMagicFind(character.getMagicFind() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setMagicFind(character.getMagicFind() - this.value);
    }

}
