package com.guillot.moria.dungeon;

import static com.guillot.moria.configs.DungeonConfig.DUNGEON_UNUSUAL_ROOMS;
import static com.guillot.moria.configs.ScreenConfig.QUART_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.QUART_WIDTH;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_WIDTH;

import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class Room {

    private RoomType type;

    private Point position;

    public Room(int level, Point position) {
        if (level + RNG.get().randomNumberNormalDistribution(1, 2) > RNG.get().randomNumber(DUNGEON_UNUSUAL_ROOMS)) {
            type = RoomType.randomSpecialRooms();
        } else {
            type = RoomType.NORMAL;
        }

        this.position = new Point();
        this.position.y = (position.x * (SCREEN_HEIGHT >> 1) + QUART_HEIGHT);
        this.position.x = (position.y * (SCREEN_WIDTH >> 1) + QUART_WIDTH);
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
