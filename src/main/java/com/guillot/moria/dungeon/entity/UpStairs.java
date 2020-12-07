package com.guillot.moria.dungeon.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;


public class UpStairs extends AbstractEntity {

    private static final long serialVersionUID = -7817436629345670171L;

    private int level;

    public UpStairs(Point position, int level) {
        super(position);

        type = Entity.UPSTAIRS;
        image = Images.STAIRS;
        this.level = level;
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getSubImage(1, 0), x, y, filter);
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public void use(GameState game, GameView view) {
        game.toGoUpstairs();
    }

    @Override
    public String toString() {
        return "Back to level " + level;
    }
}
