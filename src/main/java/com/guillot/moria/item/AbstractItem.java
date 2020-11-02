package com.guillot.moria.item;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.utils.RNG;

public abstract class AbstractItem {

    protected ItemRarity rarity;

    protected ItemType type;

    protected ArrayList<AbstractAffixe> affixes;

    protected int qualityLevel;

    protected int value;

    protected int requirement;

    public void generateBase() {
        List<int[]> values = asList(this.getValuesPerLevel()).stream().filter(x -> x[0] <= this.qualityLevel).collect(toList());
        int[] valueSelected = values.get(RNG.get().randomNumberBetween(0, values.size() - 1));

        this.value = RNG.get().randomNumberBetween(valueSelected[1], valueSelected[2]);
        this.requirement = valueSelected[3];
    }

    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 0, 0, 0}};
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void setQualityLevel(int characterLevel) {
        this.qualityLevel = characterLevel + this.rarity.getBonusQualityLevel();
    }

    public int getQualityLevel() {
        return this.qualityLevel;
    }

    public ArrayList<AbstractAffixe> getAffixes() {
        return affixes;
    }

    public void setAffixes(ArrayList<AbstractAffixe> affixes) {
        this.affixes = affixes;
    }

    public ItemBlock getBlock() {
        return this.type != null ? this.type.getBlock() : null;
    }

    public int getPoints() {
        return this.rarity != null ? this.rarity.getPoints() : 0;
    }

    public String getValueName() {
        return null;
    }

    public String getRequirementName() {
        return null;
    }

    public int getRequirement() {
        return this.requirement;
    }

    public boolean isUsable(AbstractCharacter character) {
        return true;
    }

    public boolean isEquipable(AbstractCharacter character) {
        return true;
    }

    public boolean isEligible() {
        return true;
    }

    public abstract void setPassiveEffect(AbstractCharacter character);

    public abstract void unsetPassiveEffect(AbstractCharacter character);

    public abstract void use(AbstractCharacter character);

    @Override
    public String toString() {
        String text = this.type + " (" + this.rarity + ") - Quality lvl " + this.qualityLevel + "\n";
        if (this.getValueName() != null) {
            text += "\t" + this.getValueName() + ": " + this.value + "\n";
        }
        if (this.getRequirementName() != null && this.requirement > 0) {
            text += "\t" + this.getRequirementName() + " Requirement: " + this.requirement + "\n";
        }
        if (this.affixes != null) {
            for (AbstractAffixe attribute : this.affixes) {
                text += "\t" + attribute + "\n";
            }
        }

        return text;
    }
}
