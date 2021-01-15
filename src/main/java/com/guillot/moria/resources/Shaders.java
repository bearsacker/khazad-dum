package com.guillot.moria.resources;

import com.guillot.engine.opengl.Shader;

public enum Shaders {
    MAGICAL("shaders/postprocess.vert", "shaders/postprocess.frag");

    private Shader shader;

    private Shaders(String pathVertex, String pathFragment) {
        shader = new Shader();
        shader.load(pathVertex, pathFragment);
    }

    public Shader get() {
        return shader;
    }
}
