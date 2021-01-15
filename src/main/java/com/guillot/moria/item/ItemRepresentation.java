package com.guillot.moria.item;

import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.RNG;

public class ItemRepresentation {

    private int level;

    private int valueMin;

    private int valueMax;

    private int requirement;

    private String name;

    private Images image;

    public ItemRepresentation(String name, int level, int valueMin, int valueMax, int requirement) {
        this(name, level, valueMin, valueMax, requirement, null);
    }

    public ItemRepresentation(String name, int level, int valueMin, int valueMax, int requirement, Images image) {
        this.name = name;
        this.level = level;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.requirement = requirement;
        this.image = image;
    }

    public int getRandomValue() {
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

    public Images getImage() {
        return image;
    }

    public void setImage(Images image) {
        this.image = image;
    }

}
