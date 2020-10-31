package com.guillot.moria.item;

import com.guillot.moria.utils.Drawable;

/**
 * Enumération représentant la rareté d'un objet. Elle peut être soit: Normale, magique, légendaire. Un objet a 1% de chances d'être
 * légendaire et 20% de chances (plus le bonus magique) d'être magique.
 */
public enum ItemRarity implements Drawable {
    NORMAL("Normal", 79, 0, 0), //
    MAGIC("Magic", 20, 3, 1), //
    LEGENDARY("Legendary", 1, 9, 3);

    /**
     * Le nom de la rareté.
     */
    private String name;

    /**
     * Le nombre de chances sur 100 de trouver l'objet de cette rareté.
     */
    private int probability;

    /**
     * Le bonus de niveau pour la qualityLevel de l'objet.
     */
    private int bonusQualityLevel;

    /**
     * Le nombre de points utilisables pour les attributs des objets.
     */
    private int points;

    private ItemRarity(String name, int probability, int points, int bonusQualityLevel) {
        this.name = name;
        this.probability = probability;
        this.points = points;
        this.bonusQualityLevel = bonusQualityLevel;
    }

    public int getPoints() {
        return this.points;
    }

    public int getBonusQualityLevel() {
        return this.bonusQualityLevel;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int getProbability() {
        return this.probability;
    }
}
