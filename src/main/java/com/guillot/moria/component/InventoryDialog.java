package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.KEY_I;

import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.TextBox;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemBlock;
import com.guillot.moria.item.Usable;
import com.guillot.moria.views.GameView;

public class InventoryDialog extends SubView {

    private final static Color OVERLAY_COLOR = new Color(0f, 0f, 0f, .75f);

    private GameView parent;

    private AbstractCharacter player;

    private Button buttonAction;

    private Button buttonDrop;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    private TextBox itemTextBox;

    private TextBox cursorTextBox;

    private InventoryGridComponent inventoryGrid;

    private ItemBlockComponent itemBlockHead;

    private ItemBlockComponent itemBlockNeck;

    private ItemBlockComponent itemBlockBody;

    private ItemBlockComponent itemBlockLeftHand;

    private ItemBlockComponent itemBlockRightHand;

    private ItemBlockComponent itemBlockFinger;

    public InventoryDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent);

        this.parent = parent;
        this.player = player;

        buttonAction = new Button("Use", WIDTH - 336, 72, 224, 32);
        buttonAction.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useOrEquipeItem(selectedItem);
            }
        });
        buttonAction.setVisible(false);

        buttonDrop = new Button("Drop", WIDTH - 336, 120, 224, 32);
        buttonDrop.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                dropItem(selectedItem);
            }
        });
        buttonDrop.setVisible(false);

        inventoryGrid = new InventoryGridComponent(this, player);
        inventoryGrid.setX(128);
        inventoryGrid.setY(64);

        itemTextBox = new TextBox();
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setVisible(false);

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        itemBlockNeck = new ItemBlockComponent(this, player, ItemBlock.NECK);
        itemBlockNeck.setX(WIDTH - 336);
        itemBlockNeck.setY(HEIGHT / 2 - 80);

        itemBlockHead = new ItemBlockComponent(this, player, ItemBlock.HEAD);
        itemBlockHead.setX(WIDTH - 256);
        itemBlockHead.setY(HEIGHT / 2 - 80);

        itemBlockBody = new ItemBlockComponent(this, player, ItemBlock.BODY);
        itemBlockBody.setX(WIDTH - 256);
        itemBlockBody.setY(HEIGHT / 2);

        itemBlockLeftHand = new ItemBlockComponent(this, player, ItemBlock.LEFT_HAND);
        itemBlockLeftHand.setX(WIDTH - 176);
        itemBlockLeftHand.setY(HEIGHT / 2);

        itemBlockRightHand = new ItemBlockComponent(this, player, ItemBlock.RIGHT_HAND);
        itemBlockRightHand.setX(WIDTH - 336);
        itemBlockRightHand.setY(HEIGHT / 2);

        itemBlockFinger = new ItemBlockComponent(this, player, ItemBlock.FINGER);
        itemBlockFinger.setX(WIDTH - 176);
        itemBlockFinger.setY(HEIGHT / 2 + 80);

        add(buttonAction, buttonDrop, inventoryGrid, itemBlockHead, itemBlockNeck, itemBlockBody,
                itemBlockLeftHand, itemBlockRightHand, itemBlockFinger, itemTextBox, cursorTextBox);
    }

    @Override
    public void onShow() throws Exception {
        Collections.sort(player.getInventory());
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        cursorTextBox.setVisible(false);

        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE) || GUI.get().isKeyPressed(KEY_I)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (hoveredItem != null) {
            itemTextBox.setText(hoveredItem.toString());
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight());
            itemTextBox.setVisible(true);
        } else if (selectedItem != null) {
            itemTextBox.setText(selectedItem.toString());
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight());
            itemTextBox.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
        }

        buttonAction.setVisible(selectedItem instanceof Usable || selectedItem instanceof Equipable);
        buttonAction.setText(selectedItem instanceof Usable ? "Use" : (player.isEquipedByItem(selectedItem) ? "Unequip" : "Equip"));
        buttonDrop.setVisible(selectedItem != null);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        super.paint(g);
    }

    public void useOrEquipeItem(AbstractItem item) {
        if (item != null) {
            if (item instanceof Usable) {
                if (((Usable) item).use(player)) {
                    player.getInventory().remove(item);
                    parent.getConsole().addMessage(player.getName() + " uses " + item.getName());
                    selectedItem = null;
                    hoveredItem = null;
                }
            } else if (item instanceof Equipable) {
                if (player.isEquipedByItem(item)) {
                    player.unequipItem(item);
                } else {
                    player.equipItem((Equipable) item);
                }
            }
        }
    }

    public void dropItem(AbstractItem item) {
        if (player.dropItem(parent.getDungeon(), item)) {
            player.unequipItem(item);

            parent.getConsole().addMessage(player.getName() + " drops " + item.getName());
            selectedItem = null;
            hoveredItem = null;
        }
    }

    public void setHoveredItem(AbstractItem hoveredItem) {
        this.hoveredItem = hoveredItem;
    }

    public AbstractItem getHoveredItem() {
        return hoveredItem;
    }

    public void setSelectedItem(AbstractItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public AbstractItem getSelectedItem() {
        return selectedItem;
    }

    public void showCursorTextBox(int x, int y, String text) {
        cursorTextBox.setText(text);
        cursorTextBox.setX(x);
        cursorTextBox.setY(y);
        cursorTextBox.setVisible(true);
    }
}
