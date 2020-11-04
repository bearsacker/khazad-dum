package com.guillot.moria.character;

import static com.guillot.moria.configs.DwarfConfig.AGILITY;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_LOCK_PICKING;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_MAGIC_FIND;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_TO_HIT;
import static com.guillot.moria.configs.DwarfConfig.DESITINY;
import static com.guillot.moria.configs.DwarfConfig.LIFE;
import static com.guillot.moria.configs.DwarfConfig.LIFE_PER_LEVEL;
import static com.guillot.moria.configs.DwarfConfig.LIFE_PER_SPIRIT;
import static com.guillot.moria.configs.DwarfConfig.LIGHT_RADIUS;
import static com.guillot.moria.configs.DwarfConfig.SPIRIT;
import static com.guillot.moria.configs.DwarfConfig.STRENGTH;

public class Dwarf extends AbstractCharacter {

    public Dwarf(String name) {
        super(name, null);
    }

    @Override
    public String getClassName() {
        return "Dwarf";
    }

    @Override
    public int getStrengthMin() {
        return STRENGTH;
    }

    @Override
    public int getAgilityMin() {
        return AGILITY;
    }

    @Override
    public int getSpiritMin() {
        return SPIRIT;
    }

    @Override
    public int getLightRadiusMin() {
        return LIGHT_RADIUS;
    }

    @Override
    public int getDestinyMin() {
        return DESITINY;
    }

    @Override
    public int getLifeMin() {
        return LIFE;
    }

    @Override
    public int getLifePerLevel() {
        return LIFE_PER_LEVEL;
    }

    @Override
    public int getLifePerSpirit() {
        return LIFE_PER_SPIRIT;
    }

    @Override
    public int getChanceHitMin() {
        return CHANCE_TO_HIT;
    }

    @Override
    public int getChanceMagicFindMin() {
        return CHANCE_MAGIC_FIND;
    }

    @Override
    public int getChanceLockPickingMin() {
        return CHANCE_LOCK_PICKING;
    }

}
