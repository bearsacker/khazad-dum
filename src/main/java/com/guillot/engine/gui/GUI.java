package com.guillot.engine.gui;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.DEFAULT_TEXT_COLOR;
import static com.guillot.engine.configs.GUIConfig.FONT;
import static com.guillot.engine.configs.GUIConfig.FONT_SIZES;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import com.guillot.engine.opengl.FrameBuffer;
import com.guillot.engine.opengl.OpenGL;
import com.guillot.engine.utils.FileLoader;


public class GUI {

    private final static Logger logger = LogManager.getLogger(GUI.class);

    private static GUI instance = new GUI();

    private GameContainer container;

    private Input input;

    private int mouseX;

    private int mouseY;

    private boolean[] keysPressed;

    private MouseButtonState[] mouseButtons;

    private View currentView;

    private ArrayList<TrueTypeFont> fonts;

    private Bindable postProcessingShader;

    private FrameBuffer frameBuffer;

    private FrameBuffer layerFrameBuffer;

    private HashMap<String, Color> colors;

    private Image cursor;

    public static GUI get() {
        return instance;
    }

    private GUI() {
        frameBuffer = new FrameBuffer(WIDTH, HEIGHT);
        layerFrameBuffer = new FrameBuffer(WIDTH, HEIGHT);

        colors = new HashMap<>();
        fonts = new ArrayList<>();
        for (String size : FONT_SIZES) {
            try {
                Font awtFont = FileLoader.fontFromResource(FONT);
                awtFont = awtFont.deriveFont(Float.parseFloat(size));
                fonts.add(new TrueTypeFont(awtFont, false));
            } catch (Exception e) {
                logger.error("Error while loading font", e);
            }
        }

        keysPressed = new boolean[256];
        mouseButtons = new MouseButtonState[Mouse.getButtonCount()];
        Arrays.fill(mouseButtons, MouseButtonState.NONE);
    }

    public TrueTypeFont getFont() {
        return fonts.get(0);
    }

    public TrueTypeFont getFont(int index) {
        if (index < fonts.size()) {
            return fonts.get(index);
        }

        return fonts.get(0);
    }

    public TrueTypeFont loadFont(String font, float size) {
        return loadFont(font, true, size);
    }

    public TrueTypeFont loadFont(String font, boolean antiAlias, float size) {
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream(font);
            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(size);
            inputStream.close();

            return (new TrueTypeFont(awtFont2, antiAlias));
        } catch (Exception e) {
            logger.error("Error while loading font", e);
        }

        return null;
    }

    public void switchView(View v) {
        try {
            if (currentView != null) {
                currentView.stop(false);
            }

            currentView = v;
            currentView.start();
        } catch (Exception e) {
            switchView(new ViewException(e));
        }
    }

    public void paint(Graphics g) {
        try {
            if (currentView != null) {
                frameBuffer.bind();
                currentView.paint(g);
                frameBuffer.unbind();

                if (postProcessingShader != null) {
                    layerFrameBuffer.bind();
                    currentView.paintIntoLayer(g);
                    layerFrameBuffer.unbind();

                    postProcessingShader.bind(layerFrameBuffer);
                    OpenGL.drawRectangle(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), frameBuffer.getTextureId());
                    postProcessingShader.unbind();
                } else {
                    OpenGL.drawRectangle(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), frameBuffer.getTextureId());
                }
            }
        } catch (Exception e) {
            switchView(new ViewException(e));
        }
    }

    public void update(GameContainer container) {
        if (this.container != container) {
            this.container = container;
        }

        if (input == null) {
            input = this.container.getInput();
        }

        mouseX = input.getMouseX();
        mouseY = input.getMouseY();

        for (int i = 0; i < keysPressed.length; i++) {
            keysPressed[i] = input.isKeyPressed(i);
        }

        for (int i = 0; i < mouseButtons.length; i++) {
            if (input.isMouseButtonDown(i)) {
                switch (mouseButtons[i]) {
                case RELEASED:
                case NONE:
                    mouseButtons[i] = MouseButtonState.PRESSED;
                    break;
                case DOWN:
                case PRESSED:
                    mouseButtons[i] = MouseButtonState.DOWN;
                    break;
                }
            } else {
                switch (mouseButtons[i]) {
                case RELEASED:
                case NONE:
                    mouseButtons[i] = MouseButtonState.NONE;
                    break;
                case DOWN:
                case PRESSED:
                    mouseButtons[i] = MouseButtonState.RELEASED;
                    break;
                }
            }
        }

        try {
            if (currentView != null) {
                currentView.update();
            }
        } catch (Exception e) {
            switchView(new ViewException(e));
        }
    }

    public void close() {
        try {
            currentView.stop(true);
            System.exit(0);
        } catch (Exception e) {
            switchView(new ViewException(e));
        }
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getLastKeyPressed() {
        for (int i = 0; i < keysPressed.length; i++) {
            if (keysPressed[i]) {
                return i;
            }
        }

        return -1;
    }

    public boolean isKeyPressed(int code) {
        return keysPressed[code];
    }

    public void clearKeysPressed() {
        keysPressed = new boolean[256];
    }

    public boolean isMousePressed(int button) {
        return mouseButtons[button] == MouseButtonState.PRESSED;
    }

    public boolean isMouseDown(int button) {
        return mouseButtons[button] == MouseButtonState.DOWN;
    }


    public boolean isMouseReleased(int button) {
        return mouseButtons[button] == MouseButtonState.RELEASED;
    }

    public GameContainer getContainer() {
        return container;
    }

    public Input getInput() {
        return input;
    }

    public void setPostProcessingShader(Bindable postProcessingShader) {
        this.postProcessingShader = postProcessingShader;
    }

    public boolean hasPostProcessingShader() {
        return postProcessingShader != null;
    }

    public void registerColor(String name, Color color) {
        colors.put(name, color);
    }

    public Color getColor(String name) {
        if (colors.containsKey(name)) {
            return new Color(colors.get(name));
        }

        return null;
    }

    public Image getCursor() {
        return cursor;
    }

    public void setCursor(Image cursor) {
        this.cursor = cursor;
    }

    public static void drawTiledImage(Image image, Color filter, int width, int height, int spriteWidth, int spriteHeight, int borderSize) {
        for (int i = borderSize; i < width - borderSize; i += (spriteWidth - borderSize * 2)) {
            int sizeW = spriteWidth - borderSize * 2;
            if (i + sizeW > width - borderSize) {
                sizeW = width - borderSize - i;
            }

            for (int j = borderSize; j < height - borderSize; j += (spriteHeight - borderSize * 2)) {
                int sizeH = spriteHeight - borderSize * 2;
                if (j + sizeH > height - borderSize) {
                    sizeH = height - borderSize - j;
                }


                image.draw(i, j, i + sizeW, j + sizeH, borderSize, borderSize, borderSize + sizeW, borderSize + sizeH, filter);
            }

            image.draw(i, 0, i + sizeW, borderSize, borderSize, 0, borderSize + sizeW, borderSize, filter);
            image.draw(i, height - borderSize, i + sizeW, height, borderSize, spriteHeight - borderSize, borderSize + sizeW, spriteHeight,
                    filter);
        }

        for (int j = borderSize; j < height - borderSize; j += (spriteHeight - borderSize * 2)) {
            int size = spriteHeight - borderSize * 2;
            if (j + size > height - borderSize) {
                size = height - borderSize - j;
            }

            image.draw(0, j, borderSize, j + size, 0, borderSize, borderSize, borderSize + size, filter);
            image.draw(width - borderSize, j, width, j + size, spriteWidth - borderSize, borderSize, spriteWidth, borderSize + size,
                    filter);
        }

        image.draw(0, 0, borderSize, borderSize, 0, 0, borderSize, borderSize, filter);
        image.draw(width - borderSize, 0, width, borderSize, spriteWidth - borderSize, 0, spriteWidth, borderSize, filter);
        image.draw(0, height - borderSize, borderSize, height, 0, spriteHeight - borderSize, borderSize, spriteHeight, filter);
        image.draw(width - borderSize, height - borderSize, width, height, spriteWidth - borderSize, spriteHeight - borderSize, spriteWidth,
                spriteHeight, filter);
    }

    public static void drawColoredString(Graphics g, TrueTypeFont font, int x, int y, String text, Color defaultColor) {
        drawColoredString(g, font, x, y, text, defaultColor, 1f);
    }

    public static void drawColoredString(Graphics g, TrueTypeFont font, int x, int y, String text, Color defaultColor,
            float alpha) {
        String[] values = text.split("@@");

        if (values.length == 1 || values.length % 2 != 0) {
            Color color = new Color(defaultColor);
            color.a = alpha;
            font.drawString(x, y, text, color);
        } else {
            int currentOffset = x;

            for (int i = 0; i < values.length; i += 2) {
                Color color = GUI.get().getColor(values[i]);
                if (color == null) {
                    color = DEFAULT_TEXT_COLOR;
                }

                color.a = alpha;
                font.drawString(currentOffset, y, values[i + 1], color);
                currentOffset += font.getWidth((values[i + 1]));
            }
        }
    }

    public static int getColoredStringWidth(TrueTypeFont font, String text) {
        String[] values = text.split("@@");

        if (values.length == 1) {
            return font.getWidth(text);
        } else {
            int width = 0;

            for (int i = 0; i < values.length; i += 2) {
                width += font.getWidth((values[i + 1]));
            }

            return width;
        }
    }
}
