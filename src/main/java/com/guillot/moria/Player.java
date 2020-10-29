package com.guillot.moria;

import com.guillot.moria.utils.Point;

public class Player {

    private Point position;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }
}
