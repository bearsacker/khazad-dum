package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.Collections;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.component.InventoryGridComponent;
import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.save.GameSaveManager;
import com.guillot.moria.save.VaultSaveManager;

public class DeathView extends View {

    private AbstractCharacter player;

    private Vault vault;

    private Text deathText;

    private InventoryGridComponent inventoryGrid;

    private Button validateButton;

    private Window itemWindow;

    private TextBox itemTextBox;

    public DeathView(GameState game) throws Exception {
        GameSaveManager.deleteSaveFile(GAME_SAVE_PATH);
        vault = VaultSaveManager.loadSaveFile(VAULT_SAVE_PATH);

        this.player = game.getPlayer();

        inventoryGrid = new InventoryGridComponent(null, player.getInventory(), player.getInventoryLimit(), 11);
        inventoryGrid.setX(160);
        inventoryGrid.setY(160);

        validateButton = new Button("Hide no object", WIDTH - 304, HEIGHT - 96, 48);
        validateButton.setX(WIDTH - validateButton.getWidth() - 48);
        validateButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                if (vault.addItem(inventoryGrid.getSelectedItem())) {
                    VaultSaveManager.writeSaveFile(VAULT_SAVE_PATH, vault);
                }
                GUI.get().switchView(new MenuView());
            }
        });

        String text = "WHITE@@Unfortunately, you just died in the mines ...\n";
        text += "YELLOW_PALE@@But you can hide an item in your vault before leaving this world.";

        deathText = new Text(text, 0, 64, GUI.get().getFont(1));
        deathText.setX(WIDTH / 2 - deathText.getWidth() / 2);

        itemWindow = new Window(null, 0, HEIGHT, WIDTH, 0);
        itemWindow.setShowOverlay(false);
        itemWindow.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setX(16);
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setDrawBox(false);
        itemTextBox.setVisible(false);

        add(inventoryGrid, itemWindow, itemTextBox, validateButton, deathText);
    }

    @Override
    public void start() throws Exception {
        Collections.sort(player.getInventory());
    }

    @Override
    public void update() throws Exception {
        validateButton.setText(inventoryGrid.getSelectedItem() != null
                ? "DARK_GREY@@Hide @@" + inventoryGrid.getSelectedItem().getFormattedName().replace("LIGHT_", "DARK_")
                : "Hide no object");
        validateButton.setX(WIDTH - validateButton.getWidth() - 48);

        if (!inventoryGrid.mouseOn() && !validateButton.mouseOn() && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            inventoryGrid.setSelectedItem(null);
        }

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

}
