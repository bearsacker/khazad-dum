package com.guillot.engine.gui;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.DIALOG_OVERLAY_COLOR;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_BORDER;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_SIZE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_BODY_SPRITE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_BORDER;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_HEIGHT;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_SPRITE;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_TEXT_COLOR;
import static com.guillot.engine.configs.GUIConfig.WINDOW_HEADER_WIDTH;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Window extends SubView {

    private Image imageHeader;

    private Image imageBody;

    protected String title;

    protected boolean showOverlay;

    protected boolean showHeader;

    public Window(View parent, int x, int y, int width, int height) throws Exception {
        this(parent, x, y, width, height, null);
    }

    public Window(View parent, int x, int y, int width, int height, String title) throws Exception {
        super(parent);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        imageHeader = new Image(WINDOW_HEADER_SPRITE);
        imageHeader.setFilter(Image.FILTER_NEAREST);
        imageBody = new Image(WINDOW_BODY_SPRITE);
        imageBody.setFilter(Image.FILTER_NEAREST);
        showOverlay = true;
        showHeader = true;
    }

    @Override
    public void paint(Graphics g) {
        if (showOverlay) {
            g.setColor(DIALOG_OVERLAY_COLOR);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        g.pushTransform();
        g.translate(x, y);

        GUI.drawTiledImage(imageBody, filter, width, height, WINDOW_BODY_SIZE, WINDOW_BODY_SIZE, WINDOW_BODY_BORDER);
        if (showHeader) {
            int frame = title != null ? 1 : 0;
            Image imageHeader = this.imageHeader.getSubImage(0, frame * WINDOW_HEADER_HEIGHT, WINDOW_HEADER_WIDTH, WINDOW_HEADER_HEIGHT);
            GUI.drawTiledImage(imageHeader, filter, width, WINDOW_HEADER_HEIGHT, WINDOW_HEADER_WIDTH, WINDOW_HEADER_HEIGHT,
                    WINDOW_HEADER_BORDER);

            if (title != null) {
                GUI.get().getFont().drawString(width / 2 - GUI.get().getFont().getWidth(title) / 2,
                        WINDOW_HEADER_HEIGHT / 2 - GUI.get().getFont().getHeight() / 2, title, WINDOW_HEADER_TEXT_COLOR);
            }
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
