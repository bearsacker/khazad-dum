package com.guillot.moria.item;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public abstract class AbstractItem {

    protected ItemRarity rarity;

    protected ItemType type;

    protected ArrayList<AbstractAffixe> affixes;

    protected boolean generated;

    protected int qualityLevel;

    protected int value;

    protected int requirement;

    protected String name;

    protected Image image;

    protected Point position;

    public void generateBase() {
        List<ItemRepresentation> representations =
                this.getValuesPerLevel().stream().filter(x -> x.getLevel() <= this.qualityLevel).collect(toList());

        if (!representations.isEmpty()) {
            ItemRepresentation representation = representations.get(RNG.get().randomNumberBetween(1, representations.size()) - 1);

            this.value = representation.getRandomValue();
            this.requirement = representation.getRequirement();
            this.name = representation.getName();
            this.image = representation.getImage();
            this.generated = true;
        }
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

    public void setQualityLevel(int qualityLevel) {
        this.qualityLevel = qualityLevel + this.rarity.getBonusQualityLevel();
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
        return generated;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public abstract List<ItemRepresentation> getValuesPerLevel();

    @Override
    public String toString() {
        String text = this.name + " - " + this.type + " (" + this.rarity + ") - Quality lvl " + this.qualityLevel + "\n";
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
