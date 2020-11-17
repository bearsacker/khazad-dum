package com.guillot.engine.configs;

import static java.lang.Integer.parseInt;

import org.apache.commons.configuration2.Configuration;
import org.newdawn.slick.Color;

public final class GUIConfig extends Config {

    private static final GUIConfig INSTANCE = new GUIConfig("gui.properties");

    public final static String FONT = get().getString("gui.font", "fonts/roboto.ttf");

    public final static String[] FONT_SIZES = toArray(get().getString("gui.font-sizes", "24"));

    // Dialog

    public final static Color OVERLAY_COLOR = toColor(get().getString("gui.dialog.overlay-color", "0,0,0,192"));

    // Window

    public final static String WINDOW_HEADER_SPRITE = get().getString("gui.window.header.sprite", "gui/default_window_header.png");

    public final static int WINDOW_HEADER_BORDER = get().getInt("gui.window.header.border", 2);

    public final static int WINDOW_HEADER_WIDTH = get().getInt("gui.window.header.width", 32);

    public final static int WINDOW_HEADER_HEIGHT = get().getInt("gui.window.header.height", 32);

    public final static String WINDOW_BODY_SPRITE = get().getString("gui.window.body.sprite", "gui/default_window_body.png");

    public final static int WINDOW_BODY_BORDER = get().getInt("gui.window.body.border", 2);

    public final static int WINDOW_BODY_SIZE = get().getInt("gui.window.body.size", 32);

    // Textbox

    public final static String TEXTBOX_SPRITE = get().getString("gui.textbox.sprite", "gui/default_textbox.png");

    public final static Color TEXTBOX_TEXT_COLOR = toColor(get().getString("gui.textbox.text-color", "255,255,255,255"));

    public final static int TEXTBOX_BORDER = get().getInt("gui.textbox.border", 2);

    public final static int TEXTBOX_SPRITE_SIZE = get().getInt("gui.textbox.sprite.size", 32);

    public final static int TEXTBOX_PADDING = get().getInt("gui.textbox.padding", 10);

    // Button

    public final static String BUTTON_SPRITE = get().getString("gui.button.sprite", "gui/default_button.png");

    public final static Color BUTTON_TEXT_COLOR = toColor(get().getString("gui.button.text-color", "128,128,128,255"));

    public final static Color BUTTON_TEXT_HOVER_COLOR = toColor(get().getString("gui.button.text-hover-color", "0,0,0,255"));

    public final static Color BUTTON_DISABLED_FILTER = toColor(get().getString("gui.button.disable-filter", "204,204,204,255"));

    public final static int BUTTON_BORDER = get().getInt("gui.button.border", 2);

    public final static int BUTTON_SPRITE_SIZE = get().getInt("gui.button.sprite.size", 16);

    public final static int BUTTON_PADDING = get().getInt("gui.button.padding", 10);

    private GUIConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    private static String[] toArray(String array) {
        return array.split(",");
    }

    private static Color toColor(String value) {
        String[] channels = value.split(",");
        if (channels.length != 4) {
            return null;
        }

        return new Color(parseInt(channels[0]), parseInt(channels[1]), parseInt(channels[2]), parseInt(channels[3]));
    }
}
