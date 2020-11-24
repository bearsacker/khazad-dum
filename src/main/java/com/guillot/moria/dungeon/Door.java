package com.guillot.moria.dungeon;

import java.io.Serializable;

import com.guillot.moria.utils.Point;

public class Door implements Serializable {

    private static final long serialVersionUID = -3966871541387982322L;

    private Point position;

    private DoorState state;

    private Direction direction;

    private boolean picked;

    public Door(Point position, DoorState state, Direction direction) {
        this.position = position;
        this.state = state;
        this.direction = direction;
    }

    public Point getDirectionPosition(Point playerPosition) {
        Point directionPosition = new Point(position);

        switch (direction) {
        case NORTH:
            directionPosition.decrementX();
            if (directionPosition.is(playerPosition)) {
                directionPosition.incrementX(2);
            }
            break;
        case WEST:
            directionPosition.decrementY();
            if (directionPosition.is(playerPosition)) {
                directionPosition.incrementY(2);
            }
            break;
        default:
            break;
        }

        return directionPosition;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public DoorState getState() {
        return state;
    }

    public void setState(DoorState state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    @Override
    public String toString() {
        switch (state) {
        case LOCKED:
            return "Locked door";
        case OPEN:
            return "Door";
        case STUCK:
            return "Stuck door";
        default:
            return "";
        }
    }

}
