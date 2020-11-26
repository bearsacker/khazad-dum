package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class CharacterDialog extends Window {

    private AbstractCharacter player;

    private TextBox characterTextBox;

    private Text attributesPointsText;

    private Button increaseStrengthButton;

    private Button increaseAgilityButton;

    private Button increaseSpiritButton;

    private Button increaseDestinyButton;

    public CharacterDialog(GameView parent, GameState game) throws Exception {
        super(parent, 128, 96, WIDTH - 256, 0);

        this.player = game.getPlayer();

        characterTextBox = new TextBox();
        characterTextBox.setDrawBox(false);
        characterTextBox.setWidth(width);
        characterTextBox.setX(16);
        characterTextBox.setY(48);
        characterTextBox.setText(player.toString());
        height = characterTextBox.getHeight() + 64;

        increaseStrengthButton = new Button("+", 576, 224, 32, 32);
        increaseStrengthButton.setEnabled(false);
        increaseStrengthButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseStrength();
                characterTextBox.setText(player.toString());
            }
        });

        increaseAgilityButton = new Button("+", 576, 288, 32, 32);
        increaseAgilityButton.setEnabled(false);
        increaseAgilityButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseAgility();
                characterTextBox.setText(player.toString());
            }
        });

        increaseSpiritButton = new Button("+", 576, 352, 32, 32);
        increaseSpiritButton.setEnabled(false);
        increaseSpiritButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseSpirit();
                characterTextBox.setText(player.toString());
            }
        });

        increaseDestinyButton = new Button("+", 576, 416, 32, 32);
        increaseDestinyButton.setEnabled(false);
        increaseDestinyButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseDestiny();
                characterTextBox.setText(player.toString());
            }
        });

        attributesPointsText = new Text("Available attributes points : " + player.getAttributesPoints(), 640, 80, Color.white);

        add(characterTextBox, attributesPointsText);
    }

    @Override
    public void onShow() throws Exception {
        characterTextBox.setText(player.toString());
        attributesPointsText.setText("Available attributes points : " + player.getAttributesPoints());
    }

    @Override
    public void update() throws Exception {
        super.update();

        height = characterTextBox.getHeight() + 64;

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        increaseStrengthButton.setEnabled(player.getAttributesPoints() > 0);
        increaseAgilityButton.setEnabled(player.getAttributesPoints() > 0);
        increaseSpiritButton.setEnabled(player.getAttributesPoints() > 0);
        increaseDestinyButton.setEnabled(player.getAttributesPoints() > 0);

        increaseStrengthButton.update();
        increaseAgilityButton.update();
        increaseSpiritButton.update();
        increaseDestinyButton.update();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        increaseStrengthButton.paint(g);
        increaseAgilityButton.paint(g);
        increaseSpiritButton.paint(g);
        increaseDestinyButton.paint(g);
    }

}
