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

    private Button useKeyButton;

    private Button pickingLockButton;

    public DoorDialog(GameView parent, GameState game) throws Exception {
        super(parent, 256, 224, WIDTH - 512, HEIGHT - 448, "The door is locked!");
        setShowCloseButton(true);

        this.game = game;
        this.player = game.getPlayer();

        useKeyButton = new Button("Use a key", width / 2 - 144, 96, 288, 48);
        useKeyButton.setIcon(Images.KEY.getImage());
        useKeyButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useKey();
            }
        });

        pickingLockButton = new Button("Pick the lock", width / 2 - 144, 160, 288, 48);
        pickingLockButton.setIcon(Images.LOCK_PICKING.getImage());
        pickingLockButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                pickingLock();
            }
        });

        add(useKeyButton, pickingLockButton);
    }

    @Override
    public void onShow() throws Exception {
        useKeyButton.setEnabled(false);
        pickingLockButton.setEnabled(false);
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

        useKeyButton.setEnabled(player.hasItemType(ItemType.KEY));
        pickingLockButton.setText("Pick the lock (" + player.getChanceLockPicking() + "%)");
        pickingLockButton.setEnabled(!door.isPicked());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    private void useKey() {
        AbstractItem key = player.getFirstItemOfType(ItemType.KEY);
        player.getInventory().remove(key);

        door.setState(DoorState.OPEN);
        player.setPosition(door.getDirectionPosition(player.getPosition()));
        game.addMessage("GREEN_PALE@@You@@WHITE@@ use a key to unlock the door!");
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
