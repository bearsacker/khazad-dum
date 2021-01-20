package com.guillot.moria.dungeon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.utils.Point;

public class InnerRoomsPlainRoom extends AbstractInnerRoomsRoom {

    private final static Logger logger = LogManager.getLogger(InnerRoomsPlainRoom.class);

    public InnerRoomsPlainRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room with inner rooms (plain) at " + position + "...");
        eraseInnerRoom();
        dungeon.placeVaultMonster(position, 1);
    }

}
