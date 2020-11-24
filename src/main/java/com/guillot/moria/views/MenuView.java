package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.moria.views.SaveManager.SAVE_PATH;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.View;

public class MenuView extends View {

    private Button newGameButton;

    private Button continueGameButton;

    private Button quitButton;

    @Override
    public void start() throws Exception {
        newGameButton = new Button("New game", 64, HEIGHT - 240, 288, 48);
        newGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init();
                GUI.get().switchView(new GameView(game));
            }
        });

        continueGameButton = new Button("Continue game", 64, HEIGHT - 176, 288, 48);
        continueGameButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameState game = new GameState();
                game.init(SAVE_PATH);
                GUI.get().switchView(new GameView(game));
            }
        });

        quitButton = new Button("Quit", 64, HEIGHT - 112, 288, 48);
        quitButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().close();
            }
        });

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
