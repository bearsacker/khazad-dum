package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.ROOM_FLOOR;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public abstract class AbstractCrossShapedRoom extends Room {

    public AbstractCrossShapedRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        Tile floor = ROOM_FLOOR;

        int random_offset = 2 + RNG.get().randomNumber(2);

        height = position.y - random_offset;
        depth = position.y + random_offset;
        left = position.x - 1;
        right = position.x + 1;

        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                dungeon.setTile(i, j, floor);
            }
        }

        for (int i = height - 1; i <= depth + 1; i++) {
            dungeon.setTile(i, left - 1, GRANITE_WALL);
            dungeon.setTile(i, right + 1, GRANITE_WALL);
        }

        for (int i = left; i <= right; i++) {
            dungeon.setTile(height - 1, i, GRANITE_WALL);
            dungeon.setTile(depth + 1, i, GRANITE_WALL);
        }

        random_offset = 2 + RNG.get().randomNumber(9);

        height = position.y - 1;
        depth = position.y + 1;
        left = position.x - random_offset;
        right = position.x + random_offset;

        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                dungeon.setTile(i, j, floor);
            }
        }

        for (int i = height - 1; i <= depth + 1; i++) {
            if (dungeon.getTiles()[i][left - 1] != floor) {
                dungeon.setTile(i, left - 1, GRANITE_WALL);
            }

            if (dungeon.getTiles()[i][right + 1] != floor) {
                dungeon.setTile(i, right + 1, GRANITE_WALL);
            }
        }

        for (int i = left; i <= right; i++) {
            if (dungeon.getTiles()[height - 1][i] != floor) {
                dungeon.setTile(height - 1, i, GRANITE_WALL);
            }

            if (dungeon.getTiles()[depth + 1][i] != floor) {
                dungeon.setTile(depth + 1, i, GRANITE_WALL);
            }
        }
    }

}
