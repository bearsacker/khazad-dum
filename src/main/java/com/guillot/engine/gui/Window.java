package com.guillot.engine.gui;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.OVERLAY_COLOR;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_BORDER;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_SIZE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_SPRITE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_BORDER;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_HEIGHT;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_SPRITE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_WIDTH;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Window extends SubView {

    private Image imageHeader;

    private Image imageBody;

    protected boolean showOverlay;

    protected boolean showHeader;

    public Window(View parent, int x, int y, int width, int height) throws Exception {
        super(parent);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        imageHeader = new Image(WINDOW_HEADER_SPRITE);
        imageBody = new Image(WINDOW_BODY_SPRITE);
        showOverlay = true;
        showHeader = true;
    }

    @Override
    public void paint(Graphics g) {
        if (showOverlay) {
            g.setColor(OVERLAY_COLOR);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        g.pushTransform();
        g.translate(x, y);

        GUI.drawTiledImage(imageBody, filter, width, height, WINDOW_BODY_SIZE, WINDOW_BODY_BORDER);
        if (showHeader) {
            GUI.drawTiledImage(imageHeader, filter, width, WINDOW_HEADER_HEIGHT, WINDOW_HEADER_WIDTH, WINDOW_HEADER_BORDER);
        }

        g.popTransform();

        super.paint(g);
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    public boolean isShowOverlay() {
        return showOverlay;
    }

    public void setShowOverlay(boolean showOverlay) {
        this.showOverlay = showOverlay;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

}
