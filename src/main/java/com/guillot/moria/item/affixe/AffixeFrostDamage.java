package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.SHIELD;
import static java.util.Arrays.asList;

import com.guillot.engine.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;

public class AffixeFrostDamage extends AbstractAffixe {

    public AffixeFrostDamage() {
        this.name = "+%d to Frost Damage";
        this.type = AffixeRarity.NORMAL;
        this.excludedItemType = asList(new ItemType[] {ARMOR, HELMET, SHIELD, AMULET, RING});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {10, 5, 10}, {20, 10, 15}, {30, 15, 20}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setFrostDamage(character.getFrostDamage() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setFrostDamage(character.getFrostDamage() - this.value);
    }
}
