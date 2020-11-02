package com.guillot.moria.character;

import com.guillot.moria.configs.WarriorConfig;

public class Warrior extends AbstractCharacter {

    public Warrior(String name) {
        super(name);
    }

    @Override
    public String getClassName() {
        return "Warrior";
    }

    @Override
    public int getStrengthMin() {
        return WarriorConfig.STRENGTH;
    }

    @Override
    public int getAgilityMin() {
        return WarriorConfig.AGILITY;
    }

    @Override
    public int getSpiritMin() {
        return WarriorConfig.SPIRIT;
    }

    @Override
    public int getLightRadiusMin() {
        return WarriorConfig.LIGHT_RADIUS;
    }

    @Override
    public int getDestinyMin() {
        return WarriorConfig.DESITINY;
    }

    @Override
    public int getLifeMin() {
        return WarriorConfig.LIFE;
    }

    @Override
    public int getLifePerLevel() {
        return WarriorConfig.LIFE_PER_LEVEL;
    }

    @Override
    public int getLifePerSpirit() {
        return WarriorConfig.LIFE_PER_SPIRIT;
    }

    @Override
    public int getChanceHitMin() {
        return WarriorConfig.CHANCE_TO_HIT;
    }

    @Override
    public int getChanceMagicFindMin() {
        return WarriorConfig.CHANCE_MAGIC_FIND;
    }

    @Override
    public int getChanceLockPickingMin() {
        return WarriorConfig.CHANCE_LOCK_PICKING;
    }

}
