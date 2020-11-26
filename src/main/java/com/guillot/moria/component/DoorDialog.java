package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class DoorDialog extends Window {

    private Door door;

    private GameState game;

    private AbstractCharacter player;

    private Button buttonUseKey;

    private Button buttonPickingLock;

    public DoorDialog(GameView parent, GameState game) throws Exception {
        super(parent, 256, 224, WIDTH - 512, HEIGHT - 448, "The door is locked!");

        this.game = game;
        this.player = game.getPlayer();

        buttonUseKey = new Button("Use a key", WIDTH / 2 - 144, y + 96, 288, 48);
        buttonUseKey.setIcon(Images.KEY.getImage());
        buttonUseKey.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useKey();
            }
        });

        buttonPickingLock = new Button("Pick the lock", WIDTH / 2 - 144, y + 160, 288, 48);
        buttonPickingLock.setIcon(Images.DICE.getImage());
        buttonPickingLock.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                pickingLock();
            }
        });
    }

    @Override
    public void onShow() throws Exception {
        buttonUseKey.setEnabled(false);
        buttonPickingLock.setEnabled(false);
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        buttonUseKey.update();
        buttonUseKey.setEnabled(player.hasItemType(ItemType.KEY));

        buttonPickingLock.update();
        buttonPickingLock.setText("Pick the lock (" + player.getChanceLockPicking() + "%)");
        buttonPickingLock.setEnabled(!door.isPicked());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        buttonUseKey.paint(g);
        buttonPickingLock.paint(g);
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    private void useKey() {
        AbstractItem key = player.getFirstItemOfType(ItemType.KEY);
        player.getInventory().remove(key);

        door.setState(DoorState.OPEN);
        player.setPosition(door.getDirectionPosition(player.getPosition()));
        game.addMessage(player.getName() + " uses a key to unlock the door!");
        setVisible(false);
    }

    private void pickingLock() {
        int dice = RNG.get().randomNumber(100);
        if (dice <= player.getChanceLockPicking()) {
            door.setState(DoorState.OPEN);
            player.setPosition(door.getDirectionPosition(player.getPosition()));
            game.addMessage("The door has been unlocked!");
            setVisible(false);
        } else if (dice >= 90) {
            door.setPicked(true);
            door.setState(DoorState.STUCK);
            game.addMessage("The lockpick failed... The door is now stuck");
            setVisible(false);
        } else {
            door.setPicked(true);
            game.addMessage("The lockpick failed");
        }
    }
}
