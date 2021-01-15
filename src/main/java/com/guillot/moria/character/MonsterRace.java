package com.guillot.moria.character;

import java.io.Serializable;
import java.util.List;

import com.guillot.moria.item.ItemType;
import com.guillot.moria.resources.Images;

public class MonsterRace implements Serializable {

    private static final long serialVersionUID = -7167073544202458260L;

    private String name;

    private Images image;

    private int level;

    private int strength;

    private int agility;

    private int life;

    private int lightRadius;

    private int chanceHit;

    private int chanceCriticalHit;

    private List<ItemType> equipedItemsType;

    public MonsterRace(String name, int level, int strength, int agility, int life, int lightRadius, int chanceHit,
            int chanceCriticalHit, List<ItemType> equipedItemsType, Images image) {
        this.name = name;
        this.image = image;
        this.level = level;
        this.strength = strength;
        this.agility = agility;
        this.life = life;
        this.lightRadius = lightRadius;
        this.chanceHit = chanceHit;
        this.chanceCriticalHit = chanceCriticalHit;
        this.equipedItemsType = equipedItemsType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int lightRadius) {
        this.lightRadius = lightRadius;
    }

    public int getChanceHit() {
        return chanceHit;
    }

    public void setChanceHit(int chanceHit) {
        this.chanceHit = chanceHit;
    }

    public int getChanceCriticalHit() {
        return chanceCriticalHit;
    }

    public void setChanceCriticalHit(int chanceCriticalHit) {
        this.chanceCriticalHit = chanceCriticalHit;
    }

    public List<ItemType> getEquipedItemsType() {
        return equipedItemsType;
    }

    public void setEquipedItemsType(List<ItemType> equipedItemsType) {
        this.equipedItemsType = equipedItemsType;
    }

    public Images getImage() {
        return image;
    }

}
