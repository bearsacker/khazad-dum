package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.TMP1_WALL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class InnerRoomsFourRoom extends AbstractInnerRoomsRoom {

    private final static Logger logger = LogManager.getLogger(InnerRoomsFourRoom.class);

    public InnerRoomsFourRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room with inner rooms (four small rooms) at " + position + "...");
        placeFourSmallRooms();

        // Treasure in each one.
        dungeon.placeRandomObjectNear(position, 2 + RNG.get().randomNumber(2));

        // Gotta have some monsters.
        dungeon.placeVaultMonster(new Point(position.x - 4, position.y + 2), RNG.get().randomNumber(2));
        dungeon.placeVaultMonster(new Point(position.x + 4, position.y + 2), RNG.get().randomNumber(2));
        dungeon.placeVaultMonster(new Point(position.x - 4, position.y - 2), RNG.get().randomNumber(2));
        dungeon.placeVaultMonster(new Point(position.x + 4, position.y - 2), RNG.get().randomNumber(2));
    }

    private void placeFourSmallRooms() {
        for (int y = height; y <= depth; y++) {
            dungeon.setTile(y, position.x, TMP1_WALL);
        }

        for (int x = left; x <= right; x++) {
            dungeon.setTile(position.y, x, TMP1_WALL);
        }

        // place random secret door
        if (RNG.get().randomNumber(2) == 1) {
            int offset = RNG.get().randomNumber(10);
            dungeon.placeSecretDoor(new Point(position.x - offset, height - 1));
            dungeon.placeSecretDoor(new Point(position.x + offset, height - 1));
            dungeon.placeSecretDoor(new Point(position.x - offset, depth + 1));
            dungeon.placeSecretDoor(new Point(position.x + offset, depth + 1));
        } else {
            int offset = RNG.get().randomNumber(3);
            dungeon.placeSecretDoor(new Point(left - 1, position.y + offset));
            dungeon.placeSecretDoor(new Point(left - 1, position.y - offset));
            dungeon.placeSecretDoor(new Point(right + 1, position.y + offset));
            dungeon.placeSecretDoor(new Point(right + 1, position.y - offset));
        }
    }
}
