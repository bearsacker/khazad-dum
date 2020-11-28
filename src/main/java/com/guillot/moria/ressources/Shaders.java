package com.guillot.moria.ressources;

import com.guillot.engine.opengl.Shader;

public enum Shaders {
    MAGICAL("shaders/magical.vert", "shaders/magical.frag");

    private Shader shader;

    private Shaders(String pathVertex, String pathFragment) {
        shader = new Shader();
        shader.load(pathVertex, pathFragment);
    }

    public Shader get() {
        return shader;
    }
}
