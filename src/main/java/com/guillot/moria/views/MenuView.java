package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.resources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.resources.Images.LOGO;
import static com.guillot.moria.resources.Images.LOGO_TEXT;
import static com.guillot.moria.resources.Images.MENU;
import static com.guillot.moria.resources.Images.MENU_OVERLAY;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static com.guillot.moria.save.GameSaveManager.isSaveFilePresent;
import static com.guillot.moria.save.VaultSaveManager.VAULT_SAVE_PATH;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.FadeTransition;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.LinkButton;
import com.guillot.engine.gui.View;
import com.guillot.engine.particles.Generator;
import com.guillot.engine.particles.Lava;
import com.guillot.engine.particles.Particles;
import com.guillot.moria.component.VaultDialog;
import com.guillot.moria.resources.Images;
import com.guillot.moria.save.VaultSaveManager;

public class MenuView extends View {

    private LinkButton newGameButton;

    private LinkButton continueGameButton;

    private LinkButton viewVaultButton;

    private LinkButton quitButton;

    private VaultDialog vaultDialog;

    private Generator lava;

    @Override
    public void start() throws Exception {
        if (!GUI.get().hasPostProcessingShader()) {
            GUI.get().setPostProcessingShader(new PostProcessingShader());
        }

        GUI.get().setCursor(Images.POINTER.getImage());

        newGameButton = new LinkButton("New game", WIDTH / 2, HEIGHT - 204, 256);
        newGameButton.setX(newGameButton.getX() - newGameButton.getText().length() * 7);
        newGameButton.setFont(GUI.get().getFont(1));
        newGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().switchView(new FadeTransition(new CharacterEditView(), 1500));
            }
        });

        continueGameButton = new LinkButton("Continue game", WIDTH / 2, HEIGHT - 164, 256);
        continueGameButton.setX(continueGameButton.getX() - continueGameButton.getText().length() * 7);
        continueGameButton.setFont(GUI.get().getFont(1));
        continueGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.initFromSave();
                GUI.get().switchView(new FadeTransition(new GameView(game), 1500));
            }
        });

        viewVaultButton = new LinkButton("Vault", WIDTH / 2, HEIGHT - 124, 256);
        viewVaultButton.setX(viewVaultButton.getX() - viewVaultButton.getText().length() * 7);
        viewVaultButton.setFont(GUI.get().getFont(1));
        viewVaultButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                vaultDialog.setVisible(true);
            }
        });

        quitButton = new LinkButton("Quit", WIDTH / 2, HEIGHT - 64, 256);
        quitButton.setX(quitButton.getX() - quitButton.getText().length() * 7);
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

        lava = new Generator(0, 20);
        lava.setPattern(new Lava(lava, 4f, 48, WIDTH));
        lava.setX(0);
        lava.setY(HEIGHT - 164);
        lava.setRunning(true);
        lava.setAngle(-90);
        Particles.get().add(lava);

        add(newGameButton, continueGameButton, quitButton, viewVaultButton, vaultDialog);
    }

    @Override
    public void stop(boolean closingWindow) throws Exception {
        super.stop(closingWindow);

        Particles.get().getGenerators().remove(0);
    }

    @Override
    public void update() throws Exception {
        super.update();

        Particles.get().update();
    }

    @Override
    public void paint(Graphics g) throws Exception {
        MENU.getImage().draw(0, 0, 10);

        Particles.get().draw(g, false);

        MENU_OVERLAY.getImage().draw(0, 0, 10);

        g.pushTransform();
        g.translate(WIDTH / 2, 96);

        LOGO.getImage().draw(-LOGO.getImage().getWidth() / 2, 0, ITEM_LEGENDARY.getColor());
        LOGO_TEXT.getImage().draw(-LOGO_TEXT.getImage().getWidth() / 2, 120);

        g.popTransform();

        super.paint(g);
    }
}
