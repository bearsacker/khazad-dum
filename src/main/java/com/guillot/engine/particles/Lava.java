package com.guillot.engine.particles;

import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.random;
import static org.apache.commons.math3.util.FastMath.sin;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.ParticlesConfig;
import com.guillot.engine.utils.NumberGenerator;


public class Lava implements Particle, Serializable {

    private static final long serialVersionUID = -3757856673705062231L;

    private final static int SIZE = ParticlesConfig.get().getInt("lava.size");

    private final static Color COLOR = ParticlesConfig.getColor("lava.color");

    private final static double RATIODEGRAD = 0.0174533;

    private Generator generator;

    private float x;

    private float y;

    private float angle;

    private int length;

    private float remainingDuration;

    private float duration;

    private int speed;

    private Color color;

    public Lava(Generator generator, float duration, int speed, int length) {
        this.generator = generator;
        x = NumberGenerator.get().randomInt(0, length);
        y = 0;
        angle = -90f;

        this.speed = speed;
        this.length = length;
        remainingDuration = (int) (random() * duration / 2) + .75f * duration;
        this.duration = remainingDuration;

        color = new Color((int) (COLOR.r * 255),
                (int) (COLOR.g * 255) + NumberGenerator.get().randomInt(-32, 64),
                (int) (COLOR.b * 255) + NumberGenerator.get().randomInt(-16, 32));
    }

    @Override
    public Particle create() {
        return new Lava(generator, duration, speed, length);
    }

    @Override
    public boolean isDead() {
        return remainingDuration <= 0.0f;
    }

    @Override
    public void update(float delta) {
        remainingDuration -= delta;
        x += delta * speed * cos(RATIODEGRAD * angle);
        y += delta * speed * sin(RATIODEGRAD * angle);

        angle += NumberGenerator.get().randomInt(-4, 4);
    }

    @Override
    public void draw(Graphics g, boolean alternate) {
        g.setColor(color);
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }
}
