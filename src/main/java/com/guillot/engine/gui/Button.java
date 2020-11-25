package com.guillot.engine.gui;

import static com.guillot.engine.configs.GUIConfig.BUTTON_BORDER;
import static com.guillot.engine.configs.GUIConfig.BUTTON_ICON_SIZE;
import static com.guillot.engine.configs.GUIConfig.BUTTON_PADDING;
import static com.guillot.engine.configs.GUIConfig.BUTTON_SPRITE;
import static com.guillot.engine.configs.GUIConfig.BUTTON_SPRITE_SIZE;
import static com.guillot.engine.configs.GUIConfig.BUTTON_TEXT_COLOR;
import static com.guillot.engine.configs.GUIConfig.BUTTON_TEXT_HOVER_COLOR;
import static com.guillot.engine.configs.GUIConfig.COMPONENT_DISABLED_FILTER;
import static com.guillot.engine.configs.GUIConfig.COMPONENT_FILTER_COLOR;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;


public class Button extends Component {

    private Image image;

    private Image icon;

    private Position iconAlignement;

    private String text;

    private Event event;

    private Color color;

    private Color colorHover;

    private TrueTypeFont font;

    private boolean enabled;

    private boolean sizeAuto;

    public Button(String text, int x, int y, int width, int height) throws Exception {
        super();

        this.image = new Image(BUTTON_SPRITE);
        this.image.setFilter(Image.FILTER_NEAREST);
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = new Color(BUTTON_TEXT_COLOR);
        this.colorHover = new Color(BUTTON_TEXT_HOVER_COLOR);
        this.enabled = true;
        this.font = GUI.get().getFont();
        this.width = width;
        this.height = height;
        this.sizeAuto = false;
    }

    public Button(String text, int x, int y) throws Exception {
        this(text, x, y, GUI.get().getFont().getWidth(text), GUI.get().getFont().getHeight());

        sizeAuto = true;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        sizeAuto = false;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        sizeAuto = false;
    }

    public boolean isSizeAuto() {
        return sizeAuto;
    }

    public void setSizeAuto(boolean sizeAuto) {
        this.sizeAuto = sizeAuto;
        if (this.sizeAuto) {
            width = font.getWidth(text) + BUTTON_PADDING * 2;
            height = font.getHeight();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = new Color(color);
    }

    public Color getColorHover() {
        return colorHover;
    }

    public void setColorHover(Color colorHover) {
        this.colorHover = new Color(colorHover);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        color.a = opacity;
        colorHover.a = opacity;
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
        if (sizeAuto) {
            width = this.font.getWidth(text) + BUTTON_PADDING * 2;
            height = this.font.getHeight();
        }
    }

    public void clearIcon() {
        icon = null;
    }

    public void setIcon(Image icon, Position alignement) {
        this.icon = icon;
        this.iconAlignement = alignement;
    }

    public void setIcon(Image icon) {
        setIcon(icon, Position.LEFT);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        float opacity = filter.a;
        filter = enabled ? new Color(COMPONENT_FILTER_COLOR) : new Color(COMPONENT_DISABLED_FILTER);
        filter.a = opacity;
    }

    @Override
    public void paint(Graphics g) {
        g.pushTransform();
        g.translate(x, y);

        int frame = 0;

        if (mouseOn && enabled && GUI.get().getInput().isMouseButtonDown(MOUSE_LEFT_BUTTON)) {
            frame = 1;
        }

        Image image = this.image.getSubImage(0, frame * BUTTON_SPRITE_SIZE, BUTTON_SPRITE_SIZE, BUTTON_SPRITE_SIZE);
        GUI.drawTiledImage(image, filter, width, height, BUTTON_SPRITE_SIZE, BUTTON_SPRITE_SIZE, BUTTON_BORDER);

        int lineHeight = font.getLineHeight();
        int lineWidth = font.getWidth(text);
        font.drawString((width / 2) - (lineWidth / 2), (height / 2) - (lineHeight / 2), text, (mouseOn && enabled ? colorHover : color));

        if (icon != null) {
            int iconX = 8;
            int iconY = (height - BUTTON_ICON_SIZE) / 2;

            switch (iconAlignement) {
            case CENTER:
                iconX = width / 2 - icon.getWidth() / 2;
                break;
            case RIGHT:
                iconX = width - 8 - icon.getWidth();
                break;
            case LEFT:
            case BOTTOM:
            case TOP:
                break;
            }

            g.drawImage(icon, iconX, iconY, iconX + BUTTON_ICON_SIZE, iconY + BUTTON_ICON_SIZE, 0, 0, icon.getWidth(), icon.getHeight(),
                    enabled ? filter : COMPONENT_DISABLED_FILTER);
        }

        g.popTransform();
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (enabled && mouseOn && GUI.get().isMouseButtonReleased(MOUSE_LEFT_BUTTON) && event != null) {
            event.perform();
        }
    }

    public void setEvent(Event e) {
        event = e;
    }
}
