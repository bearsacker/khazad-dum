package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.WHITE;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Colors;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class CharacterDialog extends Window {

    private AbstractCharacter player;

    private TextBox characterTextBox;

    private Text nameText;

    private Button increaseStrengthButton;

    private Button increaseAgilityButton;

    private Button increaseSpiritButton;

    private Button increaseDestinyButton;

    private Text damagesText;

    private Text physicalDamagesText;

    private Text fireDamagesText;

    private Text frostDamagesText;

    private Text lightningDamagesText;

    private Text armorText;

    public CharacterDialog(GameView parent, GameState game) throws Exception {
        super(parent, 128, 96, WIDTH - 256, 0);
        setShowCloseButton(true);

        this.player = game.getPlayer();

        characterTextBox = new TextBox();
        characterTextBox.setDrawBox(false);
        characterTextBox.setWidth(width);
        characterTextBox.setX(64);
        characterTextBox.setY(128);
        characterTextBox.setText(player.toString());
        height = characterTextBox.getHeight() + 128;

        increaseStrengthButton = new Button("", 176, 248, 32, 32);
        increaseStrengthButton.setIcon(Images.LEVEL_UP.getImage());
        increaseStrengthButton.setEnabled(false);
        increaseStrengthButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseStrength();
                characterTextBox.setText(player.toString());
            }
        });

        increaseAgilityButton = new Button("", 176, 320, 32, 32);
        increaseAgilityButton.setIcon(Images.LEVEL_UP.getImage());
        increaseAgilityButton.setEnabled(false);
        increaseAgilityButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseAgility();
                characterTextBox.setText(player.toString());
            }
        });

        increaseSpiritButton = new Button("", 176, 400, 32, 32);
        increaseSpiritButton.setIcon(Images.LEVEL_UP.getImage());
        increaseSpiritButton.setEnabled(false);
        increaseSpiritButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseSpirit();
                characterTextBox.setText(player.toString());
            }
        });

        increaseDestinyButton = new Button("", 176, 472, 32, 32);
        increaseDestinyButton.setIcon(Images.LEVEL_UP.getImage());
        increaseDestinyButton.setEnabled(false);
        increaseDestinyButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseDestiny();
                characterTextBox.setText(player.toString());
            }
        });

        nameText = new Text("WHITE@@" + player.getName() + " @@YELLOW_PALE@@(Level " + player.getLevel() + ")", 160, 80);
        nameText.setFont(GUI.get().getFont(2));

        damagesText = new Text("", WIDTH / 2 + 32, 160, GUI.get().getFont(3), WHITE.getColor());
        physicalDamagesText = new Text("", WIDTH / 2 + 38, 240, GUI.get().getFont(1), WHITE.getColor());
        fireDamagesText = new Text("", WIDTH / 2 + 38, 272, GUI.get().getFont(1), WHITE.getColor());
        frostDamagesText = new Text("", WIDTH / 2 + 38, 304, GUI.get().getFont(1), WHITE.getColor());
        lightningDamagesText = new Text("", WIDTH / 2 + 38, 336, GUI.get().getFont(1), WHITE.getColor());

        armorText = new Text("", WIDTH / 2 + 224, 160, GUI.get().getFont(3), WHITE.getColor());

        add(characterTextBox, nameText, damagesText, physicalDamagesText, fireDamagesText, frostDamagesText, lightningDamagesText,
                armorText);
    }

    @Override
    public void onShow() throws Exception {
        characterTextBox.setText(player.toString());
        damagesText.setText(Integer.toString(player.computeDamages()));
        physicalDamagesText.setText(Integer.toString(player.computePhysicalDamages()));
        fireDamagesText.setText(Integer.toString(player.getFireDamage()));
        frostDamagesText.setText(Integer.toString(player.getFrostDamage()));
        lightningDamagesText.setText(Integer.toString(player.getLightningDamage()));
        armorText.setText(Integer.toString(player.getArmor()));
    }

    @Override
    public void update() throws Exception {
        super.update();

        height = characterTextBox.getHeight() + 128;

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

        g.drawImage(Images.LONG_SWORD.getImage(), x + damagesText.getX() - 80, y + damagesText.getY() - 8, x + damagesText.getX() - 16,
                y + damagesText.getY() + 56, 0, 0, 32, 32, filter);
        g.drawImage(Images.LARGE_SHIELD.getImage(), x + armorText.getX() - 80, y + armorText.getY() - 8, x + armorText.getX() - 16,
                y + armorText.getY() + 56, 0, 0, 32, 32, filter);

        g.drawImage(Images.PHYSICAL_DAMAGES.getImage(), x + physicalDamagesText.getX() - 48, y + physicalDamagesText.getY() - 8,
                x + physicalDamagesText.getX() - 16, y + physicalDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.FIRE_DAMAGES.getImage(), x + fireDamagesText.getX() - 48, y + fireDamagesText.getY() - 8,
                x + fireDamagesText.getX() - 16, y + fireDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.FROST_DAMAGES.getImage(), x + frostDamagesText.getX() - 48, y + frostDamagesText.getY() - 8,
                x + frostDamagesText.getX() - 16, y + frostDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.LIGHTNING_DAMAGES.getImage(), x + lightningDamagesText.getX() - 48, y + lightningDamagesText.getY() - 8,
                x + lightningDamagesText.getX() - 16, y + lightningDamagesText.getY() + 24, 0, 0, 32, 32, filter);

        if (player.getAttributesPoints() > 0) {
            g.setColor(Colors.ITEM_LEGENDARY.getColor());
            g.setLineWidth(2f);

            g.drawRect(increaseStrengthButton.getX() + 1, increaseStrengthButton.getY(), increaseStrengthButton.getWidth() - 2,
                    increaseStrengthButton.getHeight() - 2);
            g.drawRect(increaseAgilityButton.getX() + 1, increaseAgilityButton.getY(), increaseAgilityButton.getWidth() - 2,
                    increaseAgilityButton.getHeight() - 2);
            g.drawRect(increaseSpiritButton.getX() + 1, increaseSpiritButton.getY(), increaseSpiritButton.getWidth() - 2,
                    increaseSpiritButton.getHeight() - 2);
            g.drawRect(increaseDestinyButton.getX() + 1, increaseDestinyButton.getY(), increaseDestinyButton.getWidth() - 2,
                    increaseDestinyButton.getHeight() - 2);
        }
    }

}
