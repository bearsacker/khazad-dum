package com.guillot.moria.dungeon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class InnerRoomsTreasureRoom extends AbstractInnerRoomsRoom {

    private final static Logger logger = LogManager.getLogger(InnerRoomsTreasureRoom.class);

    public InnerRoomsTreasureRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room with inner rooms (treasure vault) at " + position + "...");
        dungeon.placeTreasureVault(position, depth, height, left, right);

        // Guard the treasure well
        dungeon.placeVaultMonster(position, 2 + RNG.get().randomNumber(3));

        // If the monsters don't get 'em.
        dungeon.placeVaultTrap(position, new Point(10, 4), 2 + RNG.get().randomNumber(3));
    }

}
