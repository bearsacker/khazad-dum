package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;

import org.lwjgl.util.vector.Vector2f;

import com.guillot.engine.gui.Bindable;
import com.guillot.moria.ressources.Shaders;

public class GlowShader implements Bindable {

    private Vector2f resolution;

    public GlowShader() {
        resolution = new Vector2f(WIDTH, HEIGHT);
    }

    @Override
    public String getName() {
        return "glowShader";
    }

    @Override
    public void bind() {
        Shaders.GLOW.get().bind();
        Shaders.GLOW.get().setUniform("resolution", resolution);
    }

    @Override
    public void unbind() {
        Shaders.GLOW.get().unbind();
    }

}
