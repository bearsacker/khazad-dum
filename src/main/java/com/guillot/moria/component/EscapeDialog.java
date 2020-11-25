package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.save.SaveManager.SAVE_PATH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Window;
import com.guillot.moria.save.SaveManager;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;
import com.guillot.moria.views.MenuView;

public class EscapeDialog extends Window {

    private Button saveAndBackToMenuButton;

    private Button saveAndQuitButton;

    public EscapeDialog(GameView parent, GameState game) throws Exception {
        super(parent, 256, 224, WIDTH - 512, 240);

        showHeader = false;

        saveAndBackToMenuButton = new Button("Save and Back to menu", WIDTH / 2 - 144, y + 64, 288, 48);
        saveAndBackToMenuButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                SaveManager.writeSaveFile(SAVE_PATH, game);
                GUI.get().switchView(new MenuView());
            }
        });

        saveAndQuitButton = new Button("Save and Quit", WIDTH / 2 - 144, y + 128, 288, 48);
        saveAndQuitButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                SaveManager.writeSaveFile(SAVE_PATH, game);
                GUI.get().close();
            }
        });
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        saveAndBackToMenuButton.update();
        saveAndQuitButton.update();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        saveAndBackToMenuButton.paint(g);
        saveAndQuitButton.paint(g);
    }

}
