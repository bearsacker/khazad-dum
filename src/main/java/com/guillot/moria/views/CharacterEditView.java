package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.YELLOW_PALE;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.TextField;
import com.guillot.engine.gui.View;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Dwarf;
import com.guillot.moria.character.Elf;
import com.guillot.moria.character.Hobbit;
import com.guillot.moria.character.Human;
import com.guillot.moria.character.Race;
import com.guillot.moria.component.InventoryGridComponent;
import com.guillot.moria.component.RaceComponent;
import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.save.VaultSaveManager;

public class CharacterEditView extends View {

    private Window background;

    private Text nameText;

    private TextField nameField;

    private Vault vault;

    private InventoryGridComponent inventoryGrid;

    private Button validateButton;

    private Window itemWindow;

    private TextBox itemTextBox;

    private Text vaultText;

    private RaceComponent choseHumanButton;

    private RaceComponent choseElfButton;

    private RaceComponent choseDwarfButton;

    private RaceComponent choseHobbitButton;

    private Race selectedRace;

    public CharacterEditView() throws Exception {
        vault = VaultSaveManager.loadSaveFile(VAULT_SAVE_PATH);

        background = new Window(null, 0, 0, WIDTH, HEIGHT);
        background.setShowHeader(false);
        background.setVisible(true);

        nameText = new Text("Name", 80, 77, GUI.get().getFont(1), YELLOW_PALE.getColor());
        nameField = new TextField(176, 64, 256, 48);

        inventoryGrid = new InventoryGridComponent(null, vault.getItems(), vault.getLimit(), vault.getLimit());
        inventoryGrid.setX(WIDTH - inventoryGrid.getWidth() - 64);
        inventoryGrid.setY(HEIGHT - 144);

        validateButton = new Button("Begin adventure", 0, 64, 48);
        validateButton.setX(WIDTH - validateButton.getWidth() - 64);
        validateButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                validate();
            }
        });

        choseHumanButton = new RaceComponent(this, Race.HUMAN, 128, 192);
        choseHumanButton.setVisible(true);
        choseElfButton = new RaceComponent(this, Race.ELF, 128 + choseHumanButton.getWidth(), 192);
        choseElfButton.setVisible(true);
        choseDwarfButton = new RaceComponent(this, Race.DWARF, 128 + 2 * choseHumanButton.getWidth(), 192);
        choseDwarfButton.setVisible(true);
        choseHobbitButton = new RaceComponent(this, Race.HOBBIT, 128 + 3 * choseHumanButton.getWidth(), 192);
        choseHobbitButton.setVisible(true);

        String text = "WHITE@@This is your vault. You can take an item from it,\n";
        text += "RED_PALE@@but you can also lose it forever...";

        vaultText = new Text(text, 64, HEIGHT - 120, GUI.get().getFont(1));

        itemWindow = new Window(null, 0, HEIGHT, WIDTH, 0);
        itemWindow.setImageHeader(Images.WINDOW_HEADER_ALTERNATE.getImage());
        itemWindow.setShowOverlay(false);
        itemWindow.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setX(16);
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setDrawBox(false);
        itemTextBox.setVisible(false);

        vaultText.setVisible(!vault.isEmpty());
        inventoryGrid.setVisible(!vault.isEmpty());

        add(background, nameText, nameField, vaultText, itemWindow, itemTextBox, inventoryGrid, choseHobbitButton, choseDwarfButton,
                choseElfButton, choseHumanButton, validateButton);
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void update() throws Exception {
        if (!inventoryGrid.mouseOn() && !nameField.mouseOn() && !validateButton.mouseOn() && !choseDwarfButton.mouseOn()
                && !choseElfButton.mouseOn() && !choseHobbitButton.mouseOn() && !choseHumanButton.mouseOn()
                && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            inventoryGrid.setSelectedItem(null);
        }

        validateButton.setText(inventoryGrid.getSelectedItem() != null
                ? "DARK_GREY@@Begin adventure with @@" + inventoryGrid.getSelectedItem().getFormattedName().replace("LIGHT_", "DARK_")
                : "Begin adventure");
        validateButton.setX(WIDTH - validateButton.getWidth() - 64);
        validateButton.setEnabled(!nameField.isEmpty() && selectedRace != null);

        super.update();

        if (inventoryGrid.getHoveredItem() != null) {
            itemTextBox.setText(inventoryGrid.getHoveredItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else if (inventoryGrid.getSelectedItem() != null) {
            itemTextBox.setText(inventoryGrid.getSelectedItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
            itemWindow.setVisible(false);
        }
    }

    private void validate() {
        AbstractCharacter player = null;
        switch (selectedRace) {
        case DWARF:
            player = new Dwarf(nameField.getValue());
            break;
        case ELF:
            player = new Elf(nameField.getValue());
            break;
        case HOBBIT:
            player = new Hobbit(nameField.getValue());
            break;
        case HUMAN:
            player = new Human(nameField.getValue());
            break;
        }

        if (inventoryGrid.getSelectedItem() != null) {
            player.pickUpItem(inventoryGrid.getSelectedItem());
        }

        GameState game = new GameState();
        game.init(player);

        GUI.get().switchView(new GameView(game));
    }

    public Race getSelectedRace() {
        return selectedRace;
    }

    public void setSelectedRace(Race race) {
        this.selectedRace = race;
    }

}
