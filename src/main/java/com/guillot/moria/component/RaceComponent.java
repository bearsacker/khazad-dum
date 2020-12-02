package com.guillot.moria.component;

import static com.guillot.moria.ressources.Colors.BLUE_PALE;
import static com.guillot.moria.ressources.Colors.GREEN_PALE;
import static com.guillot.moria.ressources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.ressources.Colors.RED_PALE;
import static com.guillot.moria.ressources.Colors.WHITE;
import static com.guillot.moria.ressources.Colors.YELLOW;
import static com.guillot.moria.ressources.Colors.YELLOW_PALE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.ProgressBar;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Dwarf;
import com.guillot.moria.character.Elf;
import com.guillot.moria.character.Hobbit;
import com.guillot.moria.character.Human;
import com.guillot.moria.character.Race;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.views.CharacterEditView;

public class RaceComponent extends SubView {

    private CharacterEditView parent;

    private Race race;

    private AbstractCharacter character;

    private Text raceNameText;

    private Text strengthText;

    private Text agilityText;

    private Text spiritText;

    private Text destinyText;

    private ProgressBar strengthBar;

    private ProgressBar agilityBar;

    private ProgressBar spiritBar;

    private ProgressBar destinyBar;

    private SmallInventoryComponent inventory;

    private TextBox cursorTextBox;

    private Button choseButton;

    public RaceComponent(CharacterEditView parent, Race race, int x, int y) throws Exception {
        super(null);

        this.parent = parent;
        this.race = race;
        this.x = x;
        this.y = y;
        width = 256;
        height = 320;

        choseButton = new Button("", 32, 0, 64, 64);
        choseButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                parent.setSelectedRace(race);
            }
        });

        switch (race) {
        case DWARF:
            character = new Dwarf(null);
            choseButton.setIcon(Images.AXE.getImage());
            break;
        case ELF:
            character = new Elf(null);
            choseButton.setIcon(Images.LONG_BOW.getImage());
            break;
        case HOBBIT:
            character = new Hobbit(null);
            choseButton.setIcon(Images.CLOAK.getImage());
            break;
        case HUMAN:
            character = new Human(null);
            choseButton.setIcon(Images.LONG_SWORD.getImage());
            break;
        }

        raceNameText = new Text(character.getRaceName(), 112, 20, GUI.get().getFont(1), YELLOW.getColor());

        strengthText = new Text("Strength", 16, 80, GUI.get().getFont(), WHITE.getColor());
        agilityText = new Text("Agility", 16, 112, GUI.get().getFont(), WHITE.getColor());
        spiritText = new Text("Spirit", 16, 144, GUI.get().getFont(), WHITE.getColor());
        destinyText = new Text("Destiny", 16, 176, GUI.get().getFont(), WHITE.getColor());

        strengthBar = new ProgressBar(120, 80, 96, 16, character.getStrength() / 40f);
        strengthBar.setValueColor(RED_PALE.getColor());
        agilityBar = new ProgressBar(120, 112, 96, 16, character.getAgility() / 30f);
        agilityBar.setValueColor(GREEN_PALE.getColor());
        spiritBar = new ProgressBar(120, 144, 96, 16, character.getSpirit() / 20f);
        spiritBar.setValueColor(YELLOW_PALE.getColor());
        destinyBar = new ProgressBar(120, 176, 96, 16, character.getDestiny() / 30f);
        destinyBar.setValueColor(BLUE_PALE.getColor());

        inventory = new SmallInventoryComponent(36, 240);
        inventory.setItems(character);

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        add(raceNameText, choseButton, inventory, strengthText, agilityText, spiritText, destinyText, strengthBar, agilityBar, spiritBar,
                destinyBar, cursorTextBox);
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        cursorTextBox.setVisible(false);

        if (strengthText.mouseOn() || strengthBar.mouseOn()) {
            showTextBox("Physical damage\nInventory limit");
        } else if (agilityText.mouseOn() || agilityBar.mouseOn()) {
            showTextBox("Chance to hit\nChance to dodge/block\nMovement");
        } else if (spiritText.mouseOn() || spiritBar.mouseOn()) {
            showTextBox("Life\nLight radius");
        } else if (destinyText.mouseOn() || destinyBar.mouseOn()) {
            showTextBox("Chance to magic find\nChance to pick a lock\nChance of critical hit");
        }

        choseButton.setEnabled(parent.getSelectedRace() != race);
        raceNameText.setColor(parent.getSelectedRace() == race ? ITEM_LEGENDARY.getColor() : YELLOW_PALE.getColor());

        super.update();
    }

    @Override
    public void paint(Graphics g) {
        g.pushTransform();
        g.translate(x, y);

        super.paint(g);

        g.popTransform();
    }

    private void showTextBox(String text) {
        cursorTextBox.setText(text);
        cursorTextBox.setX(GUI.get().getMouseX() + 16 - x);
        cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight() - 16 - y);
        cursorTextBox.setVisible(true);
    }

}
