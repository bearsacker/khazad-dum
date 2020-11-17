package com.guillot.engine.configs;

import org.apache.commons.configuration2.Configuration;

public final class GUIConfig extends Config {

    private static final GUIConfig INSTANCE = new GUIConfig("gui.properties");

    public final static String FONT = get().getString("gui.font");

    public final static String FONT_SIZES = get().getString("gui.font-sizes");

    private GUIConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }
}
