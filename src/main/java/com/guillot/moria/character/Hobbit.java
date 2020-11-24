package com.guillot.moria.character;

import static com.guillot.moria.configs.HobbitConfig.AGILITY;
import static com.guillot.moria.configs.HobbitConfig.CHANCE_CRITICAL_HIT;
import static com.guillot.moria.configs.HobbitConfig.CHANCE_LOCK_PICKING;
import static com.guillot.moria.configs.HobbitConfig.CHANCE_MAGIC_FIND;
import static com.guillot.moria.configs.HobbitConfig.CHANCE_TO_HIT;
import static com.guillot.moria.configs.HobbitConfig.DESITINY;
import static com.guillot.moria.configs.HobbitConfig.INVENTORY_LIMIT;
import static com.guillot.moria.configs.HobbitConfig.LIFE;
import static com.guillot.moria.configs.HobbitConfig.LIGHT_RADIUS;
import static com.guillot.moria.configs.HobbitConfig.SPIRIT;
import static com.guillot.moria.configs.HobbitConfig.STRENGTH;
import static com.guillot.moria.ressources.Images.HOBBIT;

public class Hobbit extends AbstractCharacter {

    private static final long serialVersionUID = -8888918109708411305L;

    public Hobbit(String name) {
        super(name, HOBBIT);

        init();
    }

    @Override
    public String getClassName() {
        return "Hobbit";
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
