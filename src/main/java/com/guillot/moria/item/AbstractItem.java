package com.guillot.moria.item;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public abstract class AbstractItem implements Comparable<AbstractItem> {

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

    public void generateBase(int qualityLevel) {
        List<ItemRepresentation> representations = getValuesPerLevel().stream().filter(x -> x.getLevel() <= qualityLevel).collect(toList());

        if (!representations.isEmpty()) {
            ItemRepresentation representation = representations.get(RNG.get().randomNumberBetween(1, representations.size()) - 1);

            value = representation.getRandomValue();
            requirement = representation.getRequirement();
            name = representation.getName();
            image = representation.getImage();
            this.qualityLevel = representation.getLevel();
            generated = true;
        }
    }

    public void draw(DepthBufferedImage image, Point playerPosition) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + (int) image.getCenterOfRotationY() - 48;

        image.drawImage(this.image, x + 24, y + 36);
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
        this.qualityLevel = qualityLevel + rarity.getBonusQualityLevel();
    }

    public int getQualityLevel() {
        return qualityLevel;
    }

    public ArrayList<AbstractAffixe> getAffixes() {
        return affixes;
    }

    public void setAffixes(ArrayList<AbstractAffixe> affixes) {
        this.affixes = affixes;
    }

    public ItemBlock getBlock() {
        return type != null ? type.getBlock() : null;
    }

    public int getPoints() {
        return rarity != null ? rarity.getPoints() : 0;
    }

    public String getValueName() {
        return null;
    }

    public String getRequirementName() {
        return null;
    }

    public int getRequirement() {
        return requirement;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAffixesPassiveEffects(AbstractCharacter character) {
        for (AbstractAffixe affixe : affixes) {
            affixe.setPassiveEffect(character);
        }
    }

    public abstract List<ItemRepresentation> getValuesPerLevel();

    @Override
    public int compareTo(AbstractItem otherItem) {
        if (type != otherItem.type) {
            return type.compareTo(otherItem.type);
        }

        if (rarity != otherItem.rarity) {
            return otherItem.rarity.compareTo(rarity);
        }

        return Integer.compare(otherItem.qualityLevel, qualityLevel);
    }

    @Override
    public String toString() {
        String text = name + " - " + type + "\n";
        text += "Quality level " + qualityLevel + " (" + rarity + ")\n\n";
        if (getValueName() != null) {
            text += "     " + getValueName() + ": " + value + "\n";
        }
        if (getRequirementName() != null && requirement > 0) {
            text += "     " + getRequirementName() + " Requirement: " + requirement + "\n";
        }

        for (AbstractAffixe attribute : affixes) {
            text += "\n     " + attribute;
        }

        return text;
    }
}
