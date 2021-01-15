package com.guillot.moria.component;

import static com.guillot.engine.gui.GUI.drawColoredString;
import static com.guillot.moria.resources.Colors.WHITE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.utils.LinkedNonBlockingQueue;

public class Console extends Component {

    public final static int LINE_HEIGHT = 24;

    private LinkedNonBlockingQueue<String> messages;

    public Console(int width, LinkedNonBlockingQueue<String> messages) throws Exception {
        this.messages = messages;
        this.width = width;
        this.height = messages.getMaxSize() * LINE_HEIGHT;
    }

    @Override
    public void paint(Graphics g) {
        float alpha = 1f;
        int line = 1;
        for (int i = messages.size() - 1; i >= 0; i--) {
            drawColoredString(g, GUI.get().getFont(), x + 8, y + height - (line * LINE_HEIGHT), messages.get(i), WHITE.getColor(), alpha);

            line++;
            alpha -= 1f / messages.getMaxSize();
        }
    }

}
