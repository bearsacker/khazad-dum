package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.ROOM_FLOOR;
import static com.guillot.moria.dungeon.Tile.TMP1_WALL;

import com.guillot.moria.utils.Point;

public abstract class AbstractInnerRoomsRoom extends Room {

    public AbstractInnerRoomsRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        Tile floor = ROOM_FLOOR;

        height = position.y - 4;
        depth = position.y + 4;
        left = position.x - 11;
        right = position.x + 11;

        // the x dim of rooms tends to be much larger than the y dim,
        // so don't bother rewriting the y loop.
        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                dungeon.setTile(i, j, floor);
            }
        }

        for (int i = (height - 1); i <= (depth + 1); i++) {
            dungeon.setTile(i, left - 1, GRANITE_WALL);
            dungeon.setTile(i, right + 1, GRANITE_WALL);
        }

        for (int i = left; i <= right; i++) {
            dungeon.setTile(height - 1, i, GRANITE_WALL);
            dungeon.setTile(depth + 1, i, GRANITE_WALL);
        }

        // The inner room
        height = height + 2;
        depth = depth - 2;
        left = left + 2;
        right = right - 2;

        for (int i = (height - 1); i <= (depth + 1); i++) {
            dungeon.setTile(i, left - 1, TMP1_WALL);
            dungeon.setTile(i, right + 1, TMP1_WALL);
        }

        for (int i = left; i <= right; i++) {
            dungeon.setTile(height - 1, i, TMP1_WALL);
            dungeon.setTile(depth + 1, i, TMP1_WALL);
        }
    }

    protected void eraseInnerRoom() {
        for (int i = (height - 1); i <= (depth + 1); i++) {
            dungeon.setTile(i, left - 1, ROOM_FLOOR);
            dungeon.setTile(i, right + 1, ROOM_FLOOR);
        }

        for (int i = left; i <= right; i++) {
            dungeon.setTile(height - 1, i, ROOM_FLOOR);
            dungeon.setTile(depth + 1, i, ROOM_FLOOR);
        }
    }

}
