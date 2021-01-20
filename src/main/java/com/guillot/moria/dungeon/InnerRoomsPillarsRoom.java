package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.TMP1_WALL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.dungeon.entity.Pillar;
import com.guillot.moria.dungeon.entity.Rubble;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class InnerRoomsPillarsRoom extends AbstractInnerRoomsRoom {

    private final static Logger logger = LogManager.getLogger(InnerRoomsPillarsRoom.class);

    public InnerRoomsPillarsRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room with inner rooms (pillars) at " + position + "...");
        eraseInnerRoom();

        placeInnerPillars();

        if (RNG.get().randomNumber(3) == 1) {
            // Inner rooms
            for (int i = position.x - 5; i <= position.x + 5; i++) {
                dungeon.setTile(position.y - 1, i, TMP1_WALL);
                dungeon.setTile(position.y + 1, i, TMP1_WALL);
            }
            dungeon.setTile(position.y, position.x - 5, TMP1_WALL);
            dungeon.setTile(position.y, position.x + 5, TMP1_WALL);

            dungeon.placeSecretDoor(new Point(position.x - 3, position.y - 3 + (RNG.get().randomNumber(2) << 1)));
            dungeon.placeSecretDoor(new Point(position.x + 3, position.y - 3 + (RNG.get().randomNumber(2) << 1)));

            if (RNG.get().randomNumber(3) == 1) {
                dungeon.placeRandomObjectAt(new Point(position.x - 2, position.y));
            }

            if (RNG.get().randomNumber(3) == 1) {
                dungeon.placeRandomObjectAt(new Point(position.x + 2, position.y));
            }

            dungeon.placeVaultMonster(new Point(position.x - 2, position.y), RNG.get().randomNumber(2));
            dungeon.placeVaultMonster(new Point(position.x + 2, position.y), RNG.get().randomNumber(2));
        }
    }

    private void placeInnerPillars() {
        logger.info("\t-> Placing inner pillars at " + position + "...");
        for (int y = position.y - 1; y <= position.y + 1; y++) {
            for (int x = position.x - 1; x <= position.x + 1; x++) {
                dungeon.setTile(y, x, GRANITE_WALL);
            }
        }

        if (RNG.get().randomNumber(2) != 1) {
            return;
        }

        int offset = RNG.get().randomNumber(2);

        for (int y = position.y - 1; y <= position.y + 1; y++) {
            for (int x = position.x - 5 - offset; x <= position.x - 3 - offset; x++) {
                dungeon.getEntities().add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
            }
        }

        for (int y = position.y - 1; y <= position.y + 1; y++) {
            for (int x = position.x + 3 + offset; x <= position.x + 5 + offset; x++) {
                dungeon.getEntities().add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
            }
        }
    }

}
