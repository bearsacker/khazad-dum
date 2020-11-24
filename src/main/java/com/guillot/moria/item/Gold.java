package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.guillot.moria.ressources.Images;


public class Gold extends AbstractItem {

    private static final long serialVersionUID = 2230424831663427107L;

    public Gold() {
        type = GOLD;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Copper", 1, 1, 3, 0, Images.COPPER), //
                new ItemRepresentation("Copper", 2, 1, 4, 0, Images.COPPER), //
                new ItemRepresentation("Copper", 3, 1, 5, 0, Images.COPPER), //
                new ItemRepresentation("Silver", 4, 5, 6, 0, Images.SILVER), //
                new ItemRepresentation("Silver", 5, 5, 7, 0, Images.SILVER), //
                new ItemRepresentation("Silver", 6, 5, 8, 0, Images.SILVER), //
                new ItemRepresentation("Garnets", 7, 8, 9, 0, Images.GARNETS), //
                new ItemRepresentation("Garnets", 8, 8, 10, 0, Images.GARNETS), //
                new ItemRepresentation("Gold", 9, 10, 12, 0, Images.GOLD), //
                new ItemRepresentation("Gold", 10, 10, 14, 0, Images.GOLD), //
                new ItemRepresentation("Gold", 11, 10, 16, 0, Images.GOLD), //
                new ItemRepresentation("Opals", 12, 15, 18, 0, Images.OPALS), //
                new ItemRepresentation("Sapphires", 13, 15, 20, 0, Images.SAPPHIRES), //
                new ItemRepresentation("Gold", 14, 20, 24, 0, Images.GOLD_COINS), //
                new ItemRepresentation("Rubies", 15, 25, 28, 0, Images.RUBIES), //
                new ItemRepresentation("Diamonds", 16, 30, 32, 0, Images.DIAMONDS), //
                new ItemRepresentation("Emeralds", 17, 30, 40, 0, Images.EMERALDS), //
                new ItemRepresentation("Mithril", 18, 40, 80, 0, Images.MITHRIL));
    }

    @Override
    public void generateBase(int qualityLevel) {
        super.generateBase(qualityLevel);
        affixes = new ArrayList<>();
        rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return "Gold coins";
    }
}
