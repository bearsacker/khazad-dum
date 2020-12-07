package com.guillot.moria.dungeon.entity;

import java.util.List;

import com.guillot.moria.item.ItemType;

public class MerchantRepresentation {

    private String name;

    private int minCoins;

    private int maxCoins;

    private int minItems;

    private int maxItems;

    private int magicBonus;

    private List<ItemType> itemTypes;

    public MerchantRepresentation(String name, int minCoins, int maxCoins, int minItems, int maxItems, int magicBonus,
            List<ItemType> itemTypes) {
        this.name = name;
        this.minCoins = minCoins;
        this.maxCoins = maxCoins;
        this.minItems = minItems;
        this.maxItems = maxItems;
        this.magicBonus = magicBonus;
        this.itemTypes = itemTypes;
    }

    public String getName() {
        return name;
    }

    public int getMinCoins() {
        return minCoins;
    }

    public int getMaxCoins() {
        return maxCoins;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public int getMagicBonus() {
        return magicBonus;
    }

    public List<ItemType> getItemTypes() {
        return itemTypes;
    }

}
