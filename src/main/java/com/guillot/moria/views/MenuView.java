package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.LIGHT_GREY;
import static com.guillot.moria.ressources.Colors.YELLOW_PALE;
import static com.guillot.moria.ressources.Images.LOGO;
import static com.guillot.moria.ressources.Images.LOGO_TEXT;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static com.guillot.moria.save.GameSaveManager.isSaveFilePresent;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.LinkButton;
import com.guillot.engine.gui.View;
import com.guillot.moria.component.VaultDialog;

public class MenuView extends View {

    private LinkButton newGameButton;

    private LinkButton continueGameButton;

    private LinkButton viewVaultButton;

    private LinkButton quitButton;

    private VaultDialog vaultDialog;

    @Override
    public void start() throws Exception {
        newGameButton = new LinkButton("New game", 64, HEIGHT - 244, 256);
        newGameButton.setFont(GUI.get().getFont(1));
        newGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init();
                GUI.get().switchView(new GameView(game));
            }
        });

        continueGameButton = new LinkButton("Continue game", 64, HEIGHT - 204, 256);
        continueGameButton.setFont(GUI.get().getFont(1));
        continueGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init(GAME_SAVE_PATH);
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
        g.pushTransform();
        g.translate(WIDTH / 2, 96);

        LOGO.getImage().draw(-LOGO.getImage().getWidth(), 0, 2f, YELLOW_PALE.getColor());
        LOGO_TEXT.getImage().draw(-LOGO_TEXT.getImage().getWidth() / 2, 120, LIGHT_GREY.getColor());

        g.popTransform();

        super.paint(g);
    }
}
