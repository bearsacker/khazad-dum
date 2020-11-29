package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.configs.VaultConfig.VAULT_LIMIT;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_ESCAPE;

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

        inventoryGrid = new InventoryGridComponent(null, vault.getItems(), VAULT_LIMIT);
        inventoryGrid.setX(288);
        inventoryGrid.setY(128 + 64);

        itemWindow = new Window(parent, 0, HEIGHT, WIDTH, 0);
        itemWindow.setShowOverlay(false);
        itemWindow.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setX(16);
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setDrawBox(false);
        itemTextBox.setVisible(false);

        add();
    }

    @Override
    public void onShow() throws Exception {
        inventoryGrid.setHoveredItem(null);
        inventoryGrid.setSelectedItem(null);
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        inventoryGrid.update();
        itemWindow.update();
        itemTextBox.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

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

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        inventoryGrid.paint(g);
        // TODO Fix position with view
        if (itemWindow.isVisible()) {
            itemWindow.paint(g);
        }
        if (itemTextBox.isVisible()) {
            itemTextBox.paint(g);
        }
    }
}
