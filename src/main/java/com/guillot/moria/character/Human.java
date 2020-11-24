package com.guillot.moria.character;

import static com.guillot.moria.configs.HumanConfig.AGILITY;
import static com.guillot.moria.configs.HumanConfig.CHANCE_CRITICAL_HIT;
import static com.guillot.moria.configs.HumanConfig.CHANCE_LOCK_PICKING;
import static com.guillot.moria.configs.HumanConfig.CHANCE_MAGIC_FIND;
import static com.guillot.moria.configs.HumanConfig.CHANCE_TO_HIT;
import static com.guillot.moria.configs.HumanConfig.DESITINY;
import static com.guillot.moria.configs.HumanConfig.INVENTORY_LIMIT;
import static com.guillot.moria.configs.HumanConfig.LIFE;
import static com.guillot.moria.configs.HumanConfig.LIGHT_RADIUS;
import static com.guillot.moria.configs.HumanConfig.SPIRIT;
import static com.guillot.moria.configs.HumanConfig.STRENGTH;
import static com.guillot.moria.ressources.Images.HUMAN;

public class Human extends AbstractCharacter {

    private static final long serialVersionUID = 5824346740386902008L;

    public Human(String name) {
        super(name, HUMAN);

        init();
    }

    @Override
    public String getClassName() {
        return "Human";
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
