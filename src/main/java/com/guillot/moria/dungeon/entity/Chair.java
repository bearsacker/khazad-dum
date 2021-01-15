package com.guillot.moria.dungeon.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.Point;

public class Chair extends AbstractEntity {

    private static final long serialVersionUID = -3414296673206389378L;

    public Chair(Point position, Direction direction) {
        super(position);

        this.direction = direction;
        type = Entity.CHAIR;
        image = Images.CHAIR;
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getSubImage(direction.getValue(), 0), x, y, filter);
    }
}
