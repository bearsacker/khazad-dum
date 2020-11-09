package com.guillot.moria.dungeon;

import com.guillot.moria.utils.Point;

public class Door {

    private Point position;

    private DoorState state;

    private Direction direction;

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
