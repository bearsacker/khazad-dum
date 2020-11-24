package com.guillot.moria.character;

import static com.guillot.moria.configs.DwarfConfig.AGILITY;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_CRITICAL_HIT;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_LOCK_PICKING;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_MAGIC_FIND;
import static com.guillot.moria.configs.DwarfConfig.CHANCE_TO_HIT;
import static com.guillot.moria.configs.DwarfConfig.DESITINY;
import static com.guillot.moria.configs.DwarfConfig.INVENTORY_LIMIT;
import static com.guillot.moria.configs.DwarfConfig.LIFE;
import static com.guillot.moria.configs.DwarfConfig.LIGHT_RADIUS;
import static com.guillot.moria.configs.DwarfConfig.SPIRIT;
import static com.guillot.moria.configs.DwarfConfig.STRENGTH;
import static com.guillot.moria.ressources.Images.DWARF;

public class Dwarf extends AbstractCharacter {

    private static final long serialVersionUID = 6304139810858513921L;

    public Dwarf(String name) {
        super(name, DWARF);

        init();
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

    @Override
    public int getChanceCriticalHitMin() {
        return CHANCE_CRITICAL_HIT;
    }

    @Override
    public int getInventoryLimitMin() {
        return INVENTORY_LIMIT;
    }

}
