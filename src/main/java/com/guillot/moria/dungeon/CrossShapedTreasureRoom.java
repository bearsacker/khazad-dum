package com.guillot.moria.dungeon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class CrossShapedTreasureRoom extends AbstractCrossShapedRoom {

    private final static Logger logger = LogManager.getLogger(CrossShapedTreasureRoom.class);

    public CrossShapedTreasureRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room cross shaped (treasure vault) at " + position + "...");
        dungeon.placeVault(position);

        // Place a secret door
        int random_offset = RNG.get().randomNumber(4);
        if (random_offset < 3) {
            dungeon.placeSecretDoor(new Point(position.x, position.y - 3 + (random_offset << 1)));
        } else {
            dungeon.placeSecretDoor(new Point(position.x - 7 + (random_offset << 1), position.y));
        }

        // Place a treasure in the vault
        dungeon.placeRandomObjectAt(position);

        // Let's guard the treasure well.
        dungeon.placeVaultMonster(position, 2 + RNG.get().randomNumber(2));

        // Traps naturally
        dungeon.placeVaultTrap(position, new Point(4, 4), 1 + RNG.get().randomNumber(3));
    }

}
