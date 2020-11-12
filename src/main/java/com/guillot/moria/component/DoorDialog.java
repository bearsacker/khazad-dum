package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.moria.Images;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameView;

public class DoorDialog extends SubView {

    private final static Color OVERLAY_COLOR = new Color(0f, 0f, 0f, .75f);

    private GameView parent;

    private Door door;

    private AbstractCharacter player;

    private Button buttonUseKey;

    private Button buttonPickingLock;

    public DoorDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent);

        this.parent = parent;
        this.player = player;

        buttonUseKey = new Button("Use a key", WIDTH / 2 - 144, 384, 288, 32);
        buttonUseKey.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useKey();
            }
        });

        buttonPickingLock = new Button("Pick the lock", WIDTH / 2 - 144, 440, 288, 32);
        buttonPickingLock.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                pickingLock();
            }
        });

        add(buttonUseKey, buttonPickingLock);
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

        buttonUseKey.setEnabled(player.hasItemType(ItemType.KEY));

        buttonPickingLock.setText("Pick the lock (" + player.getChanceLockPicking() + "%)");
        buttonPickingLock.setEnabled(!door.isPicked());
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        String title = "The door is locked!";
        int titleWidth = GUI.get().getFont().getWidth(title);

        g.setColor(Color.white);
        GUI.get().getFont().drawString(WIDTH / 2 - titleWidth / 2, 128, title);

        int x = WIDTH / 2 - 92;
        int y = 92;
        g.drawImage(Images.DOOR.getSubImage(0, 0), x, y, x + 384, y + 384, 0, 0, 64, 96);

        parent.getConsole().paint(g);

        super.paint(g);

        g.drawImage(Images.ITEMS.getSubImage(11, 3), buttonUseKey.getX() + 8, buttonUseKey.getY() + 4, buttonUseKey.getX() + 32,
                buttonUseKey.getY() + 28, 0, 0, 16, 16);
        g.drawImage(Images.ITEMS.getSubImage(12, 11), buttonPickingLock.getX() + 8, buttonPickingLock.getY() + 4,
                buttonPickingLock.getX() + 32, buttonPickingLock.getY() + 28, 0, 0, 16, 16);
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    private void useKey() {
        AbstractItem key = player.getFirstItemOfType(ItemType.KEY);
        player.getInventory().remove(key);

        door.setState(DoorState.OPEN);
        player.setPosition(door.getDirectionPosition(player.getPosition()));
        parent.getConsole().addMessage(player.getName() + " uses a key to unlock the door!");
        setVisible(false);
    }

    private void pickingLock() {
        int dice = RNG.get().randomNumber(100);
        if (dice <= player.getChanceLockPicking()) {
            door.setState(DoorState.OPEN);
            player.setPosition(door.getDirectionPosition(player.getPosition()));
            parent.getConsole().addMessage("The door has been unlocked!");
            setVisible(false);
        } else if (dice >= 90) {
            door.setPicked(true);
            door.setState(DoorState.STUCK);
            parent.getConsole().addMessage("The lockpick failed... The door is now stuck");
            setVisible(false);
        } else {
            door.setPicked(true);
            parent.getConsole().addMessage("The lockpick failed");
        }
    }
}
