package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_C;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.TextBox;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.views.GameView;

public class CharacterDialog extends SubView {

    private final static Color OVERLAY_COLOR = new Color(0f, 0f, 0f, .75f);

    private AbstractCharacter player;

    private TextBox characterTextBox;

    public CharacterDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent);

        this.player = player;

        characterTextBox = new TextBox();
        characterTextBox.setAutoWidth(false);
        characterTextBox.setWidth(WIDTH - 128);
        characterTextBox.setX(64);
        characterTextBox.setY(64);
        characterTextBox.setVisible(true);

        add(characterTextBox);
    }

    @Override
    public void onShow() throws Exception {
        characterTextBox.setText(player.toString());
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE) || GUI.get().isKeyPressed(KEY_C)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        super.paint(g);
    }

}
