package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Images.MAGICAL_OVERLAY;
import static java.lang.Math.random;

import org.lwjgl.util.vector.Vector2f;

import com.guillot.engine.gui.Bindable;
import com.guillot.moria.ressources.Shaders;

public class MagicalShader implements Bindable {

    private Vector2f shaderPosition;

    private Vector2f shaderVelocity;

    private Vector2f resolution;

    private Vector2f overlayDimension;

    private long lastShader;

    public MagicalShader() {
        resolution = new Vector2f(WIDTH, HEIGHT);
        overlayDimension = new Vector2f(MAGICAL_OVERLAY.getImage().getWidth(), MAGICAL_OVERLAY.getImage().getHeight());
        shaderPosition = new Vector2f();
        shaderVelocity = new Vector2f((float) random() / 2 + .5f, (float) random() / 2 + .5f);
    }

    @Override
    public String getName() {
        return "magicalShader";
    }

    @Override
    public void bind() {
        long time = System.currentTimeMillis();
        if (time - lastShader > 10) {
            shaderPosition.x += shaderVelocity.x;
            shaderPosition.y += shaderVelocity.y;

            if (shaderPosition.x < 0) {
                shaderPosition.x = MAGICAL_OVERLAY.getImage().getWidth() - 1;
            } else if (shaderPosition.x > MAGICAL_OVERLAY.getImage().getWidth()) {
                shaderPosition.x = 0;
            }

            if (shaderPosition.y < 0) {
                shaderPosition.y = MAGICAL_OVERLAY.getImage().getHeight() - 1;
            } else if (shaderPosition.y > MAGICAL_OVERLAY.getImage().getHeight()) {
                shaderPosition.y = 0;
            }

            lastShader = time;
        }

        Shaders.MAGICAL.get().bind();
        Shaders.MAGICAL.get().setUniform("overlay", MAGICAL_OVERLAY.getImage(), 1);
        Shaders.MAGICAL.get().setUniform("position", new Vector2f(shaderPosition.x, shaderPosition.y));
        Shaders.MAGICAL.get().setUniform("resolution", resolution);
        Shaders.MAGICAL.get().setUniform("dimension", overlayDimension);
    }

    @Override
    public void unbind() {
        Shaders.MAGICAL.get().unbind();
    }

}
