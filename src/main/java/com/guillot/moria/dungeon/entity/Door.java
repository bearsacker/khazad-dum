package com.guillot.moria.dungeon.entity;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class Door extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -3966871541387982322L;

    private DoorState state;

    private boolean picked;

    public Door(Point position, DoorState state, Direction direction) {
        super(position);

        type = Entity.DOOR;
        image = Images.DOOR;

        this.state = state;
        this.direction = direction;
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        if (state != DoorState.SECRET) {
            if (direction == Direction.NORTH) {
                g.drawImage(Images.DOOR.getSubImage(1, 0), x, y, filter);
            } else if (direction == Direction.WEST) {
                g.drawImage(Images.DOOR.getSubImage(0, 0), x, y, filter);
            }
        }
    }

    @Override
    public void use(GameState game, GameView view) {
        switch (state) {
        case LOCKED:
            GUI.get().getInput().clearMousePressedRecord();
            GUI.get().getInput().clearKeyPressedRecord();
            view.getDoorDialog().setDoor(this);
            view.getDoorDialog().setVisible(true);
            break;
        case SECRET:
            state = DoorState.OPEN;
            game.addMessage("GREEN_PALE@@You@@WHITE@@ discover a secret door !");
        case OPEN:
            game.getPlayer().setPosition(getDirectionPosition(game.getPlayer().getPosition()));
            break;
        case STUCK:
            break;
        }
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

    public void setState(DoorState state) {
        this.state = state;
    }

    public DoorState getState() {
        return state;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    @Override
    public boolean isUsable() {
        return state != DoorState.SECRET;
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
