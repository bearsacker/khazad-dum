package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Window;
import com.guillot.moria.save.GameSaveManager;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;
import com.guillot.moria.views.MenuView;

public class MenuDialog extends Window {

    private Button backToMenuButton;

    private Button quitButton;

    public MenuDialog(GameView parent, GameState game) throws Exception {
        super(parent, 256, 256, WIDTH - 512, 240, "Menu");
        setShowCloseButton(true);

        backToMenuButton = new Button("Back to menu", width / 2 - 144, 80, 288, 48);
        backToMenuButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameSaveManager.writeSaveFile(GAME_SAVE_PATH, game);
                GUI.get().switchView(new MenuView());
            }
        });

        quitButton = new Button("Quit", width / 2 - 144, 144, 288, 48);
        quitButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GameSaveManager.writeSaveFile(GAME_SAVE_PATH, game);
                GUI.get().close();
            }
        });

        add(backToMenuButton, quitButton);
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        super.update(offsetX, offsetY);

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

}
