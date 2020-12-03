package com.guillot.moria.dungeon.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.particles.Fire;
import com.guillot.engine.particles.Generator;
import com.guillot.engine.particles.Particles;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;


public class FireCamp extends AbstractEntity {

    private static final long serialVersionUID = -8720085262878028620L;

    private Generator fire;

    public FireCamp(Point position) {
        super(position);

        type = Entity.FIRECAMP;
        image = Images.FIRECAMP;

        fire = new Generator(25, 16);
        fire.setPattern(new Fire(fire, 3f, 16));
        fire.setRunning(true);
        fire.setAngle(-90);
        Particles.get().add(fire);
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getImage(), x, y, filter);
        fire.setX(x + 32);
        fire.setY(y + 40);
        fire.draw(g);
    }

}
