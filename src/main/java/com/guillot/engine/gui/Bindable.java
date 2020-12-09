package com.guillot.engine.gui;

import com.guillot.engine.opengl.FrameBuffer;

public interface Bindable {

    void bind(FrameBuffer layerFramerBuffer);

    void unbind();

}
