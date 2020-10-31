package com.guillot.engine.character;

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
    public int getDexterityMin() {
        return WarriorConfig.DEXTERITY;
    }

    @Override
    public int getVitalityMin() {
        return WarriorConfig.VITALITY;
    }

    @Override
    public int getLightRadiusMin() {
        return WarriorConfig.LIGHT_RADIUS;
    }

    @Override
    public int getLuckMin() {
        return WarriorConfig.LUCK;
    }

}
