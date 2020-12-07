package com.guillot.moria.item.affixe;


import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.utils.RNG;


public abstract class AbstractAffixe implements Cloneable, Serializable {

    private static final long serialVersionUID = -1095080410024129280L;

    public final static List<AbstractAffixe> AFFIXES = asList(
            new AffixeStrength(), //
            new AffixeAgility(), //
            new AffixeSpirit(), //
            new AffixeDestiny(), //
            new AffixeArmor(), //
            new AffixeAllAttributes(), //
            new AffixeChanceDodge(), //
            new AffixeChanceHit(), //
            new AffixeLife(), //
            new AffixeLightRadius(), //
            new AffixePhysicalDamage(), //
            new AffixeChanceBlock(), //
            new AffixeFireDamage(), //
            new AffixeFrostDamage(), //
            new AffixeLightningDamage(), //
            new AffixeChanceMagicFind(), //
            new AffixeChanceCriticalHit());

    protected AffixeRarity type;

    protected List<ItemType> excludedItemType;

    protected int value;

    protected float quality;

    public int getCost() {
        return type != null ? type.getCost() : 0;
    }

    public boolean allowItemType(ItemType type) {
        return excludedItemType != null ? !excludedItemType.contains(type) : true;
    }

    public AffixeRarity getType() {
        return type;
    }

    public float getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return format(getName(), value);
    }

    public void init(int qualityLevel) {
        int[][] values = getValuesPerLevel();
        int min = 0;
        int max = 0;

        for (int i = 0; i < values.length && qualityLevel >= values[i][0]; i++) {
            min = values[i][1];
            max = values[i][2];
        }

        quality = RNG.get().random();
        value = (int) (min + quality * (max - min));
    }

    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 1, 5}};
    }

    public AbstractAffixe clone() {
        try {
            return (AbstractAffixe) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public abstract void setPassiveEffect(AbstractCharacter character);

    public abstract String getName();
}
