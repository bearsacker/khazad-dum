package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.GOLD;

import java.util.ArrayList;

import com.guillot.moria.character.AbstractCharacter;


public class Gold extends AbstractItem {

    /**
    {"copper",                        0x00000000L, TV_GOLD,     '$', 0,  3,  1, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 399
    {"copper",                        0x00000000L, TV_GOLD,     '$', 0,  4,  2, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 400
    {"copper",                        0x00000000L, TV_GOLD,     '$', 0,  5,  3, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 401
    {"silver",                        0x00000000L, TV_GOLD,     '$', 0,  6,  4, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 402
    {"silver",                        0x00000000L, TV_GOLD,     '$', 0,  7,  5, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 403
    {"silver",                        0x00000000L, TV_GOLD,     '$', 0,  8,  6, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 404
    {"garnets",                       0x00000000L, TV_GOLD,     '*', 0,  9,  7, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 405
    {"garnets",                       0x00000000L, TV_GOLD,     '*', 0, 10,  8, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 406
    {"gold",                          0x00000000L, TV_GOLD,     '$', 0, 12,  9, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 407
    {"gold",                          0x00000000L, TV_GOLD,     '$', 0, 14, 10, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 408
    {"gold",                          0x00000000L, TV_GOLD,     '$', 0, 16, 11, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 409
    {"opals",                         0x00000000L, TV_GOLD,     '*', 0, 18, 12, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 410
    {"sapphires",                     0x00000000L, TV_GOLD,     '*', 0, 20, 13, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 411
    {"gold",                          0x00000000L, TV_GOLD,     '$', 0, 24, 14, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 412
    {"rubies",                        0x00000000L, TV_GOLD,     '*', 0, 28, 15, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 413
    {"diamonds",                      0x00000000L, TV_GOLD,     '*', 0, 32, 16, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 414
    {"emeralds",                      0x00000000L, TV_GOLD,     '*', 0, 40, 17, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 415
    {"mithril",                       0x00000000L, TV_GOLD,     '$', 0, 80, 18, 1, 0, 0, 0, 0, 0, {0, 0}, 1}, // 416
    */
    
    public Gold() {
        this.type = GOLD;
    }

    @Override
    public int[][] getValuesPerLevel() {
        return new int[][] {
            {1, 1, 3, 0}, //
            {2, 1, 4, 0}, //
            {3, 1, 5, 0}, //
            {4, 1, 6, 0}, //
            {5, 1, 7, 0}, //
            {6, 1, 8, 0}, //
            {7, 1, 9, 0}, //
            {8, 1, 10, 0}, //
            {9, 10, 12, 0}, //
            {10, 10, 14, 0}, //
            {11, 10, 16, 0}, //
            {12, 10, 18, 0}, //
            {13, 10, 20, 0}, //
            {14, 20, 24, 0}, //
            {15, 20, 28, 0}, //
            {16, 30, 32, 0}, //
            {17, 30, 40, 0}, //
            {18, 40, 80, 0}
        };
    }

    @Override
    public void setPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void unsetPassiveEffect(AbstractCharacter character) {

    }

    @Override
    public void use(AbstractCharacter character) {
        character.setGold(character.getGold() + this.value);
    }

    @Override
    public void generateBase() {
        super.generateBase();
        this.affixes = new ArrayList<>();
        this.rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return "Gold coins";
    }
}
