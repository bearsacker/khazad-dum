package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.YELLOW_PALE;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_ENTER;
import static org.newdawn.slick.Input.KEY_ESCAPE;
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
import com.guillot.moria.character.Race;
import com.guillot.moria.component.InventoryGridComponent;
import com.guillot.moria.component.RaceComponent;
import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.save.VaultSaveManager;

public class CharacterEditView extends View {

    private Window background;

    private Text nameText;

    private TextField nameField;

    private Vault vault;

    private InventoryGridComponent vaultGrid;

    private Button validateButton;

    private Window itemWindow;

    private TextBox itemTextBox;

    private Text vaultText;

    private RaceComponent humanComponent;

    private RaceComponent elfComponent;

    private RaceComponent dwarfComponent;

    private RaceComponent hobbitComponent;

    private Race selectedRace;

    public CharacterEditView() throws Exception {
        vault = VaultSaveManager.loadSaveFile(VAULT_SAVE_PATH);

        background = new Window(null, 0, 0, WIDTH, HEIGHT);
        background.setShowHeader(false);
        background.setVisible(true);

        nameText = new Text("Name", 80, 77, GUI.get().getFont(1), YELLOW_PALE.getColor());
        nameField = new TextField(176, 64, 256, 48);

        vaultGrid = new InventoryGridComponent(null, vault.getItems(), vault.getLimit(), vault.getLimit());
        vaultGrid.setX(WIDTH - vaultGrid.getWidth() - 64);
        vaultGrid.setY(HEIGHT - 144);

        validateButton = new Button("Begin adventure", 0, 64, 48);
        validateButton.setX(WIDTH - validateButton.getWidth() - 64);
        validateButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                validate();
            }
        });

        humanComponent = new RaceComponent(this, Race.HUMAN, 144, 192);
        humanComponent.setVisible(true);
        elfComponent = new RaceComponent(this, Race.ELF, 144 + humanComponent.getWidth(), 192);
        elfComponent.setVisible(true);
        dwarfComponent = new RaceComponent(this, Race.DWARF, 144 + 2 * humanComponent.getWidth(), 192);
        dwarfComponent.setVisible(true);
        hobbitComponent = new RaceComponent(this, Race.HOBBIT, 144 + 3 * humanComponent.getWidth(), 192);
        hobbitComponent.setVisible(true);

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
        vaultGrid.setVisible(!vault.isEmpty());

        add(background, nameText, nameField, vaultText, hobbitComponent, dwarfComponent,
                elfComponent, humanComponent, validateButton, itemWindow, itemTextBox, vaultGrid);
    }

    @Override
    public void start() throws Exception {}

    @Override
    public void update() throws Exception {
        if (!vaultGrid.mouseOn() && !nameField.mouseOn() && !validateButton.mouseOn() && !dwarfComponent.mouseOn()
                && !elfComponent.mouseOn() && !hobbitComponent.mouseOn() && !humanComponent.mouseOn()
                && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            vaultGrid.setSelectedItem(null);
        }

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            GUI.get().switchView(new MenuView());
        }

        if (GUI.get().isKeyPressed(KEY_ENTER) && validateButton.isEnabled()) {
            validate();
        }

        validateButton.setText(vaultGrid.getSelectedItem() != null
                ? "DARKER_GRAY@@Begin adventure with @@" + vaultGrid.getSelectedItem().getFormattedName().replace("LIGHT_", "DARK_")
                : "Begin adventure");
        validateButton.setX(WIDTH - validateButton.getWidth() - 64);
        validateButton.setEnabled(!nameField.isEmpty() && selectedRace != null);

        super.update();

        if (vaultGrid.getHoveredItem() != null) {
            itemTextBox.setText(vaultGrid.getHoveredItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else if (vaultGrid.getSelectedItem() != null) {
            itemTextBox.setText(vaultGrid.getSelectedItem().toString());
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

    private void validate() throws Exception {
        AbstractCharacter player = null;
        switch (selectedRace) {
        case DWARF:
            player = dwarfComponent.getCharacter();
            player.setName(nameField.getValue());
            break;
        case ELF:
            player = elfComponent.getCharacter();
            player.setName(nameField.getValue());
            break;
        case HOBBIT:
            player = hobbitComponent.getCharacter();
            player.setName(nameField.getValue());
            break;
        case HUMAN:
            player = humanComponent.getCharacter();
            player.setName(nameField.getValue());
            break;
        }

        AbstractItem vaultItem = vaultGrid.getSelectedItem();
        if (vaultItem != null && player.pickUpItem(vaultItem)) {
            vault.getItems().remove(vaultItem);
            VaultSaveManager.writeSaveFile(VAULT_SAVE_PATH, vault);
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
