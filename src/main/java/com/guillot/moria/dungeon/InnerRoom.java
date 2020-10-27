package com.guillot.moria.dungeon;

import com.guillot.moria.utils.RNG;

public enum InnerRoom {
    PLAIN, TREASURE_VAULT, PILLARS, MAZE, FOUR_SMALL_ROOMS;

    public static InnerRoom random() {
        int index = RNG.get().randomNumber(InnerRoom.values().length - 1);

        return InnerRoom.values()[index];
    }
}
