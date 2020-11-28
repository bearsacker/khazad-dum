package com.guillot.moria.item;

import static com.guillot.moria.ressources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.ressources.Colors.ITEM_MAGIC;
import static com.guillot.moria.ressources.Colors.LIGHT_GREY;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.RandomCollection;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;

public abstract class AbstractItem implements Comparable<AbstractItem>, Serializable {

    private static final long serialVersionUID = -7309545761005359298L;

    protected ItemRarity rarity;

    protected ItemType type;

    protected ArrayList<AbstractAffixe> affixes;

    protected boolean generated;

    protected int qualityLevel;

    protected int value;

    protected int requirement;

    protected String name;

    protected Images image;

    protected Point position;

    public void generateBase(int qualityLevel) {
        List<ItemRepresentation> representations = getValuesPerLevel().stream().filter(x -> x.getLevel() <= qualityLevel).collect(toList());
        if (!representations.isEmpty()) {
            RandomCollection<ItemRepresentation> random = new RandomCollection<>();
            representations.forEach(x -> random.add(x.getLevel(), x));

            ItemRepresentation representation = random.next();

            value = representation.getRandomValue();
            requirement = representation.getRequirement();
            name = representation.getName();
            image = representation.getImage();
            this.qualityLevel = representation.getLevel();
            generated = true;
        }
    }

    public void draw(Graphics g, Point playerPosition) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(this.image.getImage(), x + 24, y + 36);
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
        return image.getImage();
    }

    public String geName() {
        return name;
    }

    public String getFormattedName() {
        String color = LIGHT_GREY.name();
        switch (rarity) {
        case LEGENDARY:
            color = ITEM_LEGENDARY.name();
            break;
        case MAGIC:
            color = ITEM_MAGIC.name();
            break;
        case NORMAL:
            break;
        }

        return color + "@@" + name;
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
        String text = getFormattedName() + " - " + type + "\n";
        text += LIGHT_GREY.name() + "@@Quality level " + qualityLevel + " (" + rarity + ")\n\n";
        if (getValueName() != null) {
            text += "ROSE_PALE@@     " + getValueName() + ": @@LIGHT_GREY@@" + value + "\n";
        }
        if (getRequirementName() != null && requirement > 0) {
            text += "ROSE_PALE@@     " + getRequirementName() + " Requirement: @@LIGHT_GREY@@" + requirement + "\n";
        }

        text += "\n";
        for (AbstractAffixe attribute : affixes) {
            text += "LIGHT_GREY@@     " + attribute + "\n";
        }

        return text;
    }
}
