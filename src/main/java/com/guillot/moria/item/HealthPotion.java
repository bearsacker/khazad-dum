package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.HEALTH_POTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;


public class HealthPotion extends AbstractItem implements Usable {

    public HealthPotion() {
        type = HEALTH_POTION;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Minor Health Potion", 1, 30, 30, 0, ITEMS.getSubImage(11, 4)), //
                new ItemRepresentation("Lesser Health Potion", 5, 75, 75, 0, ITEMS.getSubImage(11, 4)), //
                new ItemRepresentation("Health potion", 10, 100, 100, 0, ITEMS.getSubImage(11, 4)), //
                new ItemRepresentation("Greater Health Potion", 20, 150, 150, 0, ITEMS.getSubImage(11, 4)), //
                new ItemRepresentation("Major Health Potion", 25, 200, 200, 0, ITEMS.getSubImage(11, 4)));
    }

    @Override
    public boolean use(AbstractCharacter character) {
        character.setCurrentLife(character.getCurrentLife() + value);

        return true;
    }

    @Override
    public void generateBase(int qualityLevel) {
        super.generateBase(qualityLevel);
        affixes = new ArrayList<>();
        rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return "Life points";
    }
}
