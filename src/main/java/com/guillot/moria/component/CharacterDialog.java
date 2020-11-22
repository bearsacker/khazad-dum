package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.views.GameView;

public class CharacterDialog extends Window {

    private AbstractCharacter player;

    private TextBox characterTextBox;

    public CharacterDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent, 128, 96, WIDTH - 256, 0);

        this.player = player;

        characterTextBox = new TextBox();
        characterTextBox.setDrawBox(false);
        characterTextBox.setWidth(width);
        characterTextBox.setX(16);
        characterTextBox.setY(48);
        characterTextBox.setText(player.toString());
        height = characterTextBox.getHeight() + 64;

        add(characterTextBox);
    }

    @Override
    public void onShow() throws Exception {
        characterTextBox.setText(player.toString());
    }

    @Override
    public void update() throws Exception {
        super.update();

        height = characterTextBox.getHeight() + 64;

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

}
