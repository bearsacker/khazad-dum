package com.guillot.moria.item.affixe;


import static java.util.Arrays.asList;

import java.util.List;

import com.guillot.engine.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.utils.RNG;


public abstract class AbstractAffixe {

    public final static List<AbstractAffixe> AFFIXES = asList(
            new AffixeStrength(), //
            new AffixeDexterity(), //
            new AffixeVitality(), //
            new AffixeArmor(), //
            new AffixeAllAttributes(), //
            new AffixeChanceDodge(), //
            new AffixeChanceHit(), //
            new AffixeLife(), //
            new AffixeSpeed(), //
            new AffixeLightRadius(), //
            new AffixePhysicalDamage(), //
            new AffixeChanceBlock(), //
            new AffixeFireDamage(), //
            new AffixeFrostDamage(), //
            new AffixeLightningDamage(), //
            new AffixeLuck());

    protected String name;

    protected AffixeRarity type;

    protected List<ItemType> excludedItemType;

    protected int value;

    public int getCost() {
        return this.type != null ? this.type.getCost() : 0;
    }

    public boolean allowItemType(ItemType type) {
        return this.excludedItemType != null ? !this.excludedItemType.contains(type) : true;
    }

    public AffixeRarity getType() {
        return this.type != null ? this.type : null;
    }

    @Override
    public String toString() {
        return String.format(this.name, this.value);
    }

    public void init(int qualityLevel) {
        int[][] values = this.getValuesPerLevel();
        int i = values.length;
        int min = 0;
        int max = 0;

        do {
            i--;
            min = values[i][1];
            max = values[i][2];
        } while (qualityLevel < values[i][0]);

        this.value = RNG.get().randomNumberBetween(min, max);
    }

    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}};
    }

    public abstract void setPassiveEffect(AbstractCharacter character);

    public abstract void unsetPassiveEffect(AbstractCharacter character);
}
