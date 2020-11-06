package com.guillot.moria.item.affixe;

public enum AffixeRarity {
    LEGENDARY(5), RARE(2), NORMAL(1);

    private int cost;

    private AffixeRarity(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
