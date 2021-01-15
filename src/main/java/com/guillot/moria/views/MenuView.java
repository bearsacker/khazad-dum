package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.resources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.resources.Images.LOGO;
import static com.guillot.moria.resources.Images.LOGO_TEXT;
import static com.guillot.moria.resources.Images.MENU;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static com.guillot.moria.save.GameSaveManager.isSaveFilePresent;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.LinkButton;
import com.guillot.engine.gui.View;
import com.guillot.moria.component.VaultDialog;
import com.guillot.moria.resources.Images;
import com.guillot.moria.save.VaultSaveManager;

public class MenuView extends View {

    private LinkButton newGameButton;

    private LinkButton continueGameButton;

    private LinkButton viewVaultButton;

    private LinkButton quitButton;

    private VaultDialog vaultDialog;

    @Override
    public void start() throws Exception {
        if (!GUI.get().hasPostProcessingShader()) {
            GUI.get().setPostProcessingShader(new PostProcessingShader());
        }

        GUI.get().setCursor(Images.POINTER.getImage());

        newGameButton = new LinkButton("New game", 64, HEIGHT - 244, 256);
        newGameButton.setFont(GUI.get().getFont(1));
        newGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().switchView(new CharacterEditView());
            }
        });

        continueGameButton = new LinkButton("Continue game", 64, HEIGHT - 204, 256);
        continueGameButton.setFont(GUI.get().getFont(1));
        continueGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.initFromSave();
                GUI.get().switchView(new GameView(game));
            }
        });

        viewVaultButton = new LinkButton("Vault", 64, HEIGHT - 164, 256);
        viewVaultButton.setFont(GUI.get().getFont(1));
        viewVaultButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                vaultDialog.setVisible(true);
            }
        });

        quitButton = new LinkButton("Quit", 64, HEIGHT - 88, 256);
        quitButton.setFont(GUI.get().getFont(1));
        quitButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().close();
            }
        });

        viewVaultButton.setEnabled(VaultSaveManager.isSaveFilePresent(VAULT_SAVE_PATH));
        continueGameButton.setEnabled(isSaveFilePresent(GAME_SAVE_PATH));

        vaultDialog = new VaultDialog(this);

        add(newGameButton, continueGameButton, quitButton, viewVaultButton, vaultDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Override
    public void paint(Graphics g) throws Exception {
        MENU.getImage().draw(0, 0, 10);

        g.pushTransform();
        g.translate(WIDTH / 2, 96);

        LOGO.getImage().draw(-LOGO.getImage().getWidth() / 2, 0, ITEM_LEGENDARY.getColor());
        LOGO_TEXT.getImage().draw(-LOGO_TEXT.getImage().getWidth() / 2, 120);

        g.popTransform();

        super.paint(g);
    }
}
