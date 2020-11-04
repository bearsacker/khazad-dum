package com.guillot.moria.item;

import org.newdawn.slick.Image;

import com.guillot.moria.utils.RNG;

public class ItemRepresentation {

    private int level;

    private int valueMin;

    private int valueMax;

    private int requirement;

    private String name;

    private Image image;

    public ItemRepresentation(String name, int level, int valueMin, int valueMax, int requirement) {
        this(name, level, valueMin, valueMax, requirement, null);
    }

    public ItemRepresentation(String name, int level, int valueMin, int valueMax, int requirement, Image image) {
        this.name = name;
        this.level = level;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.requirement = requirement;
        this.image = image;
    }

    public int getRandomValue() {
        System.out.println(name + " " + valueMin + " " + valueMax);
        return RNG.get().randomNumberBetween(valueMin, valueMax);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getValueMin() {
        return valueMin;
    }

    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
