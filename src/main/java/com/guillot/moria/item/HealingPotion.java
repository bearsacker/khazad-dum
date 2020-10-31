package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.HEALING_POTION;

import java.util.ArrayList;

import com.guillot.engine.character.AbstractCharacter;


public class HealingPotion extends AbstractItem {

    public HealingPotion() {
        this.type = HEALING_POTION;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {{1, 30, 30, 0}, {5, 60, 60, 0}, {10, 90, 90, 0}, {15, 120, 120, 0}, {20, 150, 150, 0}, {25, 175, 175, 0}};
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void use(AbstractCharacter character) {
        character.setCurrentLife(character.getCurrentLife() + this.value);
    }

    @Override
    public void generateBase() {
        super.generateBase();
        this.affixes = new ArrayList<>();
        this.rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return "Healing";
    }
}
