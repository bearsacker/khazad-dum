package com.guillot.moria.item.affixe;


import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.SHIELD;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;

public class AffixeFrostDamage extends AbstractAffixe {

    private static final long serialVersionUID = 3710812042416955626L;

    public AffixeFrostDamage() {
        type = AffixeRarity.NORMAL;
        excludedItemType = asList(new ItemType[] {ARMOR, HELMET, SHIELD, AMULET, RING});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 2}, {5, 3, 4}, {10, 5, 6}, {15, 7, 8}, {20, 9, 10}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setFrostDamage(character.getFrostDamage() + value);
    }

    @Override
    public String getName() {
        return "+%d to Frost Damage";
    }
}
