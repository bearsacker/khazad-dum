package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class ScreenConfig extends Config {

    private static final ScreenConfig INSTANCE = new ScreenConfig("screen.properties");

    private ScreenConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int SCREEN_WIDTH = get().getInt("screen.width");

    public static final int SCREEN_HEIGHT = get().getInt("screen.height");

    public static final int QUART_WIDTH = (SCREEN_WIDTH / 4);

    public static final int QUART_HEIGHT = (SCREEN_HEIGHT / 4);

}
