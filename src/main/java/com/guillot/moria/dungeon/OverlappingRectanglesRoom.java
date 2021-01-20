package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.ROOM_FLOOR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class OverlappingRectanglesRoom extends Room {

    private final static Logger logger = LogManager.getLogger(OverlappingRectanglesRoom.class);

    public OverlappingRectanglesRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room overlapping rectangles at " + position + "...");
        Tile floor = ROOM_FLOOR;

        int limit = 1 + RNG.get().randomNumber(2);

        for (int count = 0; count < limit; count++) {
            int height = position.y - RNG.get().randomNumber(4);
            int depth = position.y + RNG.get().randomNumber(3);
            int left = position.x - RNG.get().randomNumber(11);
            int right = position.x + RNG.get().randomNumber(11);

            // the x dim of rooms tends to be much larger than the y dim,
            // so don't bother rewriting the y loop.
            for (int y = height; y <= depth; y++) {
                for (int x = left; x <= right; x++) {
                    dungeon.setTile(y, x, floor);
                }
            }

            for (int y = (height - 1); y <= (depth + 1); y++) {
                if (dungeon.getTiles()[y][left - 1] != floor) {
                    dungeon.setTile(y, left - 1, GRANITE_WALL);
                }

                if (dungeon.getTiles()[y][right + 1] != floor) {
                    dungeon.setTile(y, right + 1, GRANITE_WALL);
                }
            }

            for (int x = left; x <= right; x++) {
                if (dungeon.getTiles()[height - 1][x] != floor) {
                    dungeon.setTile(height - 1, x, GRANITE_WALL);
                }

                if (dungeon.getTiles()[depth + 1][x] != floor) {
                    dungeon.setTile(depth + 1, x, GRANITE_WALL);
                }
            }
        }
    }

}
