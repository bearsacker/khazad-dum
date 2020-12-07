package com.guillot.moria.dungeon.entity;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 6795919556377396214L;

    protected Entity type;

    protected Point position;

    protected Direction direction;

    protected Images image;

    public AbstractEntity(Point position) {
        this.position = new Point(position);
        this.direction = Direction.NORTH;
    }

    public void use(GameState game, GameView view) {}

    public int usableRadius() {
        return 1;
    }

    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getImage(), x, y, filter);
    }

    public Point getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Entity getType() {
        return type;
    }

    public boolean isUsable() {
        return false;
    }

}
