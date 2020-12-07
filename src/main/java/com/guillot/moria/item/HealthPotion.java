package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.HEALTH_POTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;


public class HealthPotion extends AbstractItem implements Usable {

    private static final long serialVersionUID = 4983786362549786282L;

    public HealthPotion() {
        type = HEALTH_POTION;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Minor Health Potion", 1, 30, 30, 0, Images.MINOR_HEALTH_POTION), //
                new ItemRepresentation("Lesser Health Potion", 3, 75, 75, 0, Images.LESSER_HEALTH_POTION), //
                new ItemRepresentation("Health potion", 6, 100, 100, 0, Images.HEALTH_POTION), //
                new ItemRepresentation("Greater Health Potion", 9, 150, 150, 0, Images.GREATER_HEALTH_POTION), //
                new ItemRepresentation("Major Health Potion", 12, 200, 200, 0, Images.MAJOR_HEALTH_POTION));
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
