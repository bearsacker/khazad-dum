package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.moria.save.SaveManager.SAVE_PATH;
import static com.guillot.moria.save.SaveManager.isSaveFilePresent;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.LinkButton;
import com.guillot.engine.gui.View;

public class MenuView extends View {

    private LinkButton newGameButton;

    private LinkButton continueGameButton;

    private LinkButton quitButton;

    @Override
    public void start() throws Exception {
        newGameButton = new LinkButton("New game", 64, HEIGHT - 204, 256);
        newGameButton.setFont(GUI.get().getFont(1));
        newGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init();
                GUI.get().switchView(new GameView(game));
            }
        });

        continueGameButton = new LinkButton("Continue game", 64, HEIGHT - 164, 256);
        continueGameButton.setFont(GUI.get().getFont(1));
        continueGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init(SAVE_PATH);
                GUI.get().switchView(new GameView(game));
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

        continueGameButton.setEnabled(isSaveFilePresent(SAVE_PATH));

        add(newGameButton, continueGameButton, quitButton);
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Override
    public void paint(Graphics g) throws Exception {
        super.paint(g);
    }
}
