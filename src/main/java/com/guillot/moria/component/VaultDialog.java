package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.configs.VaultConfig.VAULT_LIMIT;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.engine.gui.Window;
import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.save.VaultSaveManager;

public class VaultDialog extends Window {

    private Vault vault;

    private InventoryGridComponent inventoryGrid;

    private Window itemWindow;

    private TextBox itemTextBox;

    public VaultDialog(View parent) throws Exception {
        super(parent, 256, 128, WIDTH - 512, HEIGHT - 448, "Your vault");
        setShowCloseButton(true);

        vault = VaultSaveManager.loadSaveFile(VAULT_SAVE_PATH);

        inventoryGrid = new InventoryGridComponent(null, vault.getItems(), VAULT_LIMIT, 8);
        inventoryGrid.setX(32);
        inventoryGrid.setY(64);

        itemWindow = new Window(parent, -x, HEIGHT - y, WIDTH, 0);
        itemWindow.setShowOverlay(false);
        itemWindow.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setX(16 - x);
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setDrawBox(false);
        itemTextBox.setVisible(false);

        add(inventoryGrid, itemWindow, itemTextBox);
    }

    @Override
    public void onShow() throws Exception {
        inventoryGrid.setHoveredItem(null);
        inventoryGrid.setSelectedItem(null);
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        if (!inventoryGrid.mouseOn() && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            inventoryGrid.setSelectedItem(null);
        }

        super.update(offsetX, offsetY);

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (inventoryGrid.getHoveredItem() != null) {
            itemTextBox.setText(inventoryGrid.getHoveredItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - y - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - y - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else if (inventoryGrid.getSelectedItem() != null) {
            itemTextBox.setText(inventoryGrid.getSelectedItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - y - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - y - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
            itemWindow.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
