package com.guillot.moria.item;

import static com.guillot.moria.Images.ITEMS;
import static com.guillot.moria.item.ItemType.GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Gold extends AbstractItem {

    public Gold() {
        type = GOLD;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Copper", 1, 1, 3, 0, ITEMS.getSubImage(1, 5)), //
                new ItemRepresentation("Copper", 2, 1, 4, 0, ITEMS.getSubImage(1, 5)), //
                new ItemRepresentation("Copper", 3, 1, 5, 0, ITEMS.getSubImage(1, 5)), //
                new ItemRepresentation("Silver", 4, 5, 6, 0, ITEMS.getSubImage(2, 5)), //
                new ItemRepresentation("Silver", 5, 5, 7, 0, ITEMS.getSubImage(2, 5)), //
                new ItemRepresentation("Silver", 6, 5, 8, 0, ITEMS.getSubImage(2, 5)), //
                new ItemRepresentation("Garnets", 7, 8, 9, 0, ITEMS.getSubImage(0, 3)), //
                new ItemRepresentation("Garnets", 8, 8, 10, 0, ITEMS.getSubImage(0, 3)), //
                new ItemRepresentation("Gold", 9, 10, 12, 0, ITEMS.getSubImage(1, 6)), //
                new ItemRepresentation("Gold", 10, 10, 14, 0, ITEMS.getSubImage(1, 6)), //
                new ItemRepresentation("Gold", 11, 10, 16, 0, ITEMS.getSubImage(1, 6)), //
                new ItemRepresentation("Opals", 12, 15, 18, 0, ITEMS.getSubImage(6, 3)), //
                new ItemRepresentation("Sapphires", 13, 15, 20, 0, ITEMS.getSubImage(1, 3)), //
                new ItemRepresentation("Gold", 14, 20, 24, 0, ITEMS.getSubImage(8, 3)), //
                new ItemRepresentation("Rubies", 15, 25, 28, 0, ITEMS.getSubImage(3, 3)), //
                new ItemRepresentation("Diamonds", 16, 30, 32, 0, ITEMS.getSubImage(5, 3)), //
                new ItemRepresentation("Emeralds", 17, 30, 40, 0, ITEMS.getSubImage(2, 3)), //
                new ItemRepresentation("Mithril", 18, 40, 80, 0, ITEMS.getSubImage(6, 5)));
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
