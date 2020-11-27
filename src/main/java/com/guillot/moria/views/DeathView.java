package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;

import java.util.Collections;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.View;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.component.InventoryGridComponent;
import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.save.GameSaveManager;
import com.guillot.moria.save.VaultSaveManager;

public class DeathView extends View {

    private AbstractCharacter player;

    private Vault vault;

    private InventoryGridComponent inventoryGrid;

    private Button validateButton;

    public DeathView(GameState game) throws Exception {
        GameSaveManager.deleteSaveFile(GAME_SAVE_PATH);
        vault = VaultSaveManager.loadSaveFile(VAULT_SAVE_PATH);

        this.player = game.getPlayer();

        inventoryGrid = new InventoryGridComponent(player);
        inventoryGrid.setX(128);
        inventoryGrid.setY(64);

        validateButton = new Button("New game", 64, HEIGHT - 204, 256, 48);
        validateButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                if (vault.addItem(inventoryGrid.getSelectedItem())) {
                    VaultSaveManager.writeSaveFile(VAULT_SAVE_PATH, vault);
                }
                GUI.get().switchView(new MenuView());
            }
        });

        add(inventoryGrid, validateButton);
    }

    @Override
    public void start() throws Exception {
        Collections.sort(player.getInventory());
    }

}
