package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.resources.Colors.WHITE;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.resources.Colors;
import com.guillot.moria.resources.Images;
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

    private TextBox cursorTextBox;

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
        height = characterTextBox.getHeight() + 160;

        increaseStrengthButton = new Button("", 48, 152, 32, 32);
        increaseStrengthButton.setIcon(Images.LEVEL_UP.getImage());
        increaseStrengthButton.setEnabled(false);
        increaseStrengthButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseStrength();
                loadCharacterInfo();
            }
        });

        increaseAgilityButton = new Button("", 48, 224, 32, 32);
        increaseAgilityButton.setIcon(Images.LEVEL_UP.getImage());
        increaseAgilityButton.setEnabled(false);
        increaseAgilityButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseAgility();
                loadCharacterInfo();
            }
        });

        increaseSpiritButton = new Button("", 48, 304, 32, 32);
        increaseSpiritButton.setIcon(Images.LEVEL_UP.getImage());
        increaseSpiritButton.setEnabled(false);
        increaseSpiritButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseSpirit();
                loadCharacterInfo();
            }
        });

        increaseDestinyButton = new Button("", 48, 376, 32, 32);
        increaseDestinyButton.setIcon(Images.LEVEL_UP.getImage());
        increaseDestinyButton.setEnabled(false);
        increaseDestinyButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                player.increaseDestiny();
                loadCharacterInfo();
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

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        add(characterTextBox, nameText, damagesText, physicalDamagesText, fireDamagesText, frostDamagesText, lightningDamagesText,
                armorText, cursorTextBox, increaseAgilityButton, increaseDestinyButton, increaseSpiritButton, increaseStrengthButton);
    }

    @Override
    public void onShow() throws Exception {
        loadCharacterInfo();
    }

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        super.update(offsetX, offsetY);

        height = characterTextBox.getHeight() + 160;

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        increaseStrengthButton.setEnabled(player.getAttributesPoints() > 0);
        increaseAgilityButton.setEnabled(player.getAttributesPoints() > 0);
        increaseSpiritButton.setEnabled(player.getAttributesPoints() > 0);
        increaseDestinyButton.setEnabled(player.getAttributesPoints() > 0);

        cursorTextBox.setVisible(false);
        if (increaseAgilityButton.mouseOn() || increaseDestinyButton.mouseOn() || increaseSpiritButton.mouseOn()
                || increaseStrengthButton.mouseOn()) {
            showTextBox("LIGHT_GRAY@@Available points: @@ITEM_LEGENDARY@@" + player.getAttributesPoints());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.pushTransform();
        g.translate(x, y);

        g.drawImage(Images.LONG_SWORD.getImage(), damagesText.getX() - 80, damagesText.getY() - 8, damagesText.getX() - 16,
                damagesText.getY() + 56, 0, 0, 32, 32, filter);
        g.drawImage(Images.LARGE_SHIELD.getImage(), armorText.getX() - 80, armorText.getY() - 8, armorText.getX() - 16,
                armorText.getY() + 56, 0, 0, 32, 32, filter);

        g.drawImage(Images.PHYSICAL_DAMAGES.getImage(), physicalDamagesText.getX() - 48, physicalDamagesText.getY() - 8,
                physicalDamagesText.getX() - 16, physicalDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.FIRE_DAMAGES.getImage(), fireDamagesText.getX() - 48, fireDamagesText.getY() - 8,
                fireDamagesText.getX() - 16, fireDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.FROST_DAMAGES.getImage(), frostDamagesText.getX() - 48, frostDamagesText.getY() - 8,
                frostDamagesText.getX() - 16, frostDamagesText.getY() + 24, 0, 0, 32, 32, filter);
        g.drawImage(Images.LIGHTNING_DAMAGES.getImage(), lightningDamagesText.getX() - 48, lightningDamagesText.getY() - 8,
                lightningDamagesText.getX() - 16, lightningDamagesText.getY() + 24, 0, 0, 32, 32, filter);

        if (player.getAttributesPoints() > 0) {
            g.setColor(Colors.ITEM_LEGENDARY.getColor());
            g.setLineWidth(2f);

            g.drawRect(increaseStrengthButton.getX(), increaseStrengthButton.getY() - 1, increaseStrengthButton.getWidth(),
                    increaseStrengthButton.getHeight());
            g.drawRect(increaseAgilityButton.getX(), increaseAgilityButton.getY() - 1, increaseAgilityButton.getWidth(),
                    increaseAgilityButton.getHeight());
            g.drawRect(increaseSpiritButton.getX(), increaseSpiritButton.getY() - 1, increaseSpiritButton.getWidth(),
                    increaseSpiritButton.getHeight());
            g.drawRect(increaseDestinyButton.getX(), increaseDestinyButton.getY() - 1, increaseDestinyButton.getWidth(),
                    increaseDestinyButton.getHeight());
        }

        g.popTransform();
    }

    private void loadCharacterInfo() {
        nameText.setText("WHITE@@" + player.getName() + " @@YELLOW_PALE@@(Level " + player.getLevel() + ")");
        characterTextBox.setText(player.toString());
        damagesText.setText(Integer.toString(player.computeDamages()));
        physicalDamagesText.setText(Integer.toString(player.computePhysicalDamages()));
        fireDamagesText.setText(Integer.toString(player.getFireDamage()));
        frostDamagesText.setText(Integer.toString(player.getFrostDamage()));
        lightningDamagesText.setText(Integer.toString(player.getLightningDamage()));
        armorText.setText(Integer.toString(player.getArmor()));
    }

    private void showTextBox(String text) {
        cursorTextBox.setText(text);
        cursorTextBox.setX(GUI.get().getMouseX() + 16 - x);
        cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight() - 16 - y);
        cursorTextBox.setVisible(true);
    }

}
