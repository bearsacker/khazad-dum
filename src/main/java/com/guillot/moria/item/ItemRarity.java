package com.guillot.moria.item;


public enum ItemRarity {
    NORMAL("Normal", 79, 0, 0), //
    MAGIC("Magic", 20, 3, 1), //
    LEGENDARY("Legendary", 1, 9, 3);

    private String name;

    private int probability;

    private int bonusQualityLevel;

    private int points;

    private ItemRarity(String name, int probability, int points, int bonusQualityLevel) {
        this.name = name;
        this.probability = probability;
        this.points = points;
        this.bonusQualityLevel = bonusQualityLevel;
    }

    public int getPoints() {
        return points;
    }

    public int getBonusQualityLevel() {
        return bonusQualityLevel;
    }

    public int getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return name;
    }

}
