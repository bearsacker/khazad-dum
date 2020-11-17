package com.guillot.engine.gui;

import static com.guillot.engine.configs.GUIConfig.FONT;
import static com.guillot.engine.configs.GUIConfig.FONT_SIZES;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import com.guillot.engine.utils.FileLoader;


public class GUI {

    private static GUI instance = new GUI();

    private GameContainer container;

    private Input input;

    private int mouseX;

    private int mouseY;

    private boolean[] keysPressed;

    private boolean[] mouseButtonsDown;

    private boolean[] mouseButtonsReleased;

    private View currentView;

    private ArrayList<TrueTypeFont> fonts;

    public static GUI get() {
        return instance;
    }

    private GUI() {
        fonts = new ArrayList<>();
        for (String size : FONT_SIZES) {
            try {
                Font awtFont = FileLoader.fontFromResource(FONT);
                awtFont = awtFont.deriveFont(Float.parseFloat(size));
                fonts.add(new TrueTypeFont(awtFont, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        keysPressed = new boolean[256];
        mouseButtonsDown = new boolean[Mouse.getButtonCount()];
        mouseButtonsReleased = new boolean[Mouse.getButtonCount()];
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
            e.printStackTrace();
        }

        return null;
    }

    public void switchView(View v) {
        try {
            if (currentView != null) {
                currentView.stop();
            }

            currentView = v;
            currentView.start();
        } catch (Exception e) {
            e.printStackTrace();
            switchView(new ViewException(e));
        }
    }

    public void paint(Graphics g) {
        try {
            if (currentView != null) {
                currentView.paintComponents(g);
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

        for (int i = 0; i < mouseButtonsReleased.length; i++) {
            mouseButtonsReleased[i] = false;

            if (input.isMouseButtonDown(i)) {
                mouseButtonsDown[i] = true;
            } else {
                if (mouseButtonsDown[i]) {
                    mouseButtonsReleased[i] = true;
                    mouseButtonsDown[i] = false;
                }
            }
        }

        try {
            if (currentView != null) {
                currentView.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            switchView(new ViewException(e));
        }

        Controller.get().update();
    }

    public void close() {
        try {
            this.currentView.stop();
        } catch (Exception e) {
            e.printStackTrace();
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

    public boolean isMouseButtonReleased(int button) {
        return mouseButtonsReleased[button];
    }

    public GameContainer getContainer() {
        return container;
    }

    public Input getInput() {
        return input;
    }

    public static void drawTiledImage(Image image, Color filter, int width, int height, int spriteSize, int borderSize) {
        for (int i = borderSize; i < width - borderSize; i += (spriteSize - borderSize * 2)) {
            int sizeW = spriteSize - borderSize * 2;
            if (i + sizeW > width - borderSize) {
                sizeW = width - borderSize - i;
            }

            for (int j = borderSize; j < height - borderSize; j += (spriteSize - borderSize * 2)) {
                int sizeH = spriteSize - borderSize * 2;
                if (j + sizeH > height - borderSize) {
                    sizeH = height - borderSize - j;
                }

                image.draw(i, j, i + sizeW, j + sizeH, borderSize, borderSize, borderSize + sizeW, borderSize + sizeH, filter);
            }

            image.draw(i, 0, i + sizeW, borderSize, borderSize, 0, borderSize + sizeW, borderSize, filter);
            image.draw(i, height - borderSize, i + sizeW, height, borderSize, spriteSize - borderSize, borderSize + sizeW, spriteSize,
                    filter);
        }

        for (int j = borderSize; j < height - borderSize; j += (spriteSize - borderSize * 2)) {
            int size = spriteSize - borderSize * 2;
            if (j + size > height - borderSize) {
                size = height - borderSize - j;
            }

            image.draw(0, j, borderSize, j + size, 0, borderSize, borderSize, borderSize + size, filter);
            image.draw(width - borderSize, j, width, j + size, spriteSize - borderSize, borderSize, spriteSize, borderSize + size, filter);
        }

        image.draw(0, 0, borderSize, borderSize, 0, 0, borderSize, borderSize, filter);
        image.draw(0, 0 + height - borderSize, borderSize, height, 0, spriteSize - borderSize, borderSize, spriteSize, filter);
        image.draw(width - borderSize, 0, width, borderSize, spriteSize - borderSize, 0, spriteSize, borderSize, filter);
        image.draw(width - borderSize, height - borderSize, width, height, spriteSize - borderSize, spriteSize - borderSize, spriteSize,
                spriteSize, filter);
    }
}
