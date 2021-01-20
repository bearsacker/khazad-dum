package com.guillot.moria.dungeon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.dungeon.entity.Pillar;
import com.guillot.moria.dungeon.entity.Rubble;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class InnerRoomsMazeRoom extends AbstractInnerRoomsRoom {

    private final static Logger logger = LogManager.getLogger(InnerRoomsMazeRoom.class);

    public InnerRoomsMazeRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room with inner rooms (maze) at " + position + "...");
        eraseInnerRoom();

        placeMazeInsideRoom();

        // Monsters just love mazes.
        dungeon.placeVaultMonster(new Point(position.x - 6, position.y), RNG.get().randomNumber(3));
        dungeon.placeVaultMonster(new Point(position.x + 6, position.y), RNG.get().randomNumber(3));

        // Traps make them entertaining.
        dungeon.placeVaultTrap(new Point(position.x - 2, position.y), new Point(8, 2), RNG.get().randomNumber(3));
        dungeon.placeVaultTrap(new Point(position.x + 2, position.y), new Point(8, 2), RNG.get().randomNumber(3));

        // Mazes should have some treasure too..
        for (int i = 0; i < 3; i++) {
            dungeon.placeRandomObjectNear(position, 1);
        }
    }

    private void placeMazeInsideRoom() {
        for (int y = height; y <= depth; y += 2) {
            for (int x = left; x <= right; x += 2) {
                if ((0x1 & (x + y)) != 0) {
                    dungeon.getEntities().add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
                }
            }
        }
    }

}
