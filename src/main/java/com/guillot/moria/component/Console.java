package com.guillot.moria.component;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;

public class Console extends Component {

    public final static int LINE_HEIGHT = 24;

    private LinkedList<String> messages;

    private int limit;

    public Console(int width, int limit) throws Exception {
        this.messages = new LinkedList<>();
        this.limit = limit;
        this.width = width;
        this.height = limit * LINE_HEIGHT;
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Override
    public void paint(Graphics g) {
        int line = 1;
        for (int i = messages.size() - 1; i >= 0; i--) {
            GUI.get().getFont().drawString(x + 8, y + height - (line * LINE_HEIGHT), messages.get(i), Color.white);

            line++;

            if (limit > 0 && line > limit) {
                break;
            }
        }
    }

    public void addMessage(String message) {
        messages.add(message);
    }

}
