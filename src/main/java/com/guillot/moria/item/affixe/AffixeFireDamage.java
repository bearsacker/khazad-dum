package com.guillot.moria.item.affixe;

import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.affixe.AffixeRarity.NORMAL;
import static java.util.Arrays.asList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;

public class AffixeFireDamage extends AbstractAffixe {

    public AffixeFireDamage() {
        this.name = "+%d to Fire Damage";
        this.type = NORMAL;
        this.excludedItemType = asList(new ItemType[] {ARMOR, HELMET, SHIELD, AMULET, RING});
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}, {10, 5, 10}, {20, 10, 15}, {30, 15, 20}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {
        character.setFireDamage(character.getFireDamage() + this.value);
    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {
        character.setFireDamage(character.getFireDamage() - this.value);
    }
}
