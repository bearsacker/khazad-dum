package com.guillot.moria.dungeon;

import com.guillot.moria.utils.Point;

public class Room {

    protected Dungeon dungeon;

    protected Point position;

    protected int height;

    protected int depth;

    protected int left;

    protected int right;

    public Room(Dungeon dungeon, Point position) {
        this.dungeon = dungeon;
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
