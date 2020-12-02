package com.guillot.moria.character;

import static com.guillot.moria.configs.ElfConfig.AGILITY;
import static com.guillot.moria.configs.ElfConfig.CHANCE_CRITICAL_HIT;
import static com.guillot.moria.configs.ElfConfig.CHANCE_LOCK_PICKING;
import static com.guillot.moria.configs.ElfConfig.CHANCE_MAGIC_FIND;
import static com.guillot.moria.configs.ElfConfig.CHANCE_TO_HIT;
import static com.guillot.moria.configs.ElfConfig.DESITINY;
import static com.guillot.moria.configs.ElfConfig.INVENTORY_LIMIT;
import static com.guillot.moria.configs.ElfConfig.LIFE;
import static com.guillot.moria.configs.ElfConfig.LIGHT_RADIUS;
import static com.guillot.moria.configs.ElfConfig.SPIRIT;
import static com.guillot.moria.configs.ElfConfig.STRENGTH;
import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.ressources.Images.ELF;

import java.util.ArrayList;

import com.guillot.moria.item.Bow;
import com.guillot.moria.item.Equipable;

public class Elf extends AbstractCharacter {

    private static final long serialVersionUID = 7444641645182321993L;

    public Elf(String name) {
        super(name, ELF);

        init();

        Bow bow = new Bow();
        bow.generateBaseFromRepresentation("Short Bow");
        bow.setAffixes(new ArrayList<>());
        bow.setRarity(NORMAL);
        if (bow.isGenerated()) {
            pickUpItem(bow);
            equipItem((Equipable) bow);
        }
    }

    @Override
    public String getRaceName() {
        return Race.ELF.getName();
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
