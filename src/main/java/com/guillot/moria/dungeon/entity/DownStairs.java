package com.guillot.moria.dungeon.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;


public class DownStairs extends AbstractEntity {

    private static final long serialVersionUID = -8801640645163663221L;

    private int level;

    public DownStairs(Point position, int level) {
        super(position);

        type = Entity.DOWNSTAIRS;
        image = Images.STAIRS;
        this.level = level;
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getSubImage(0, 0), x, y, filter);
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public void use(GameState game, GameView view) {
        game.toGoDownstairs();
    }

    @Override
    public String toString() {
        return "Go to level " + level;
    }
}
