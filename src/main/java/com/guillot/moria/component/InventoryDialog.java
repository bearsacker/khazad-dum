package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.DIALOG_OVERLAY_COLOR;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import java.util.Collections;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemBlock;
import com.guillot.moria.item.Usable;
import com.guillot.moria.views.GameView;

public class InventoryDialog extends SubView {

    private GameView parent;

    private AbstractCharacter player;

    private Button buttonAction;

    private Button buttonDrop;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    private Window itemWindow;

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

        buttonAction = new Button("Use", WIDTH - 336, 72, 224, 48);
        buttonAction.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useOrEquipeItem(selectedItem);
            }
        });
        buttonAction.setVisible(false);

        buttonDrop = new Button("Drop", WIDTH - 336, 136, 224, 48);
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

        itemWindow = new Window(parent, 0, HEIGHT, WIDTH, 0);
        itemWindow.setShowOverlay(false);
        itemWindow.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setX(16);
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setDrawBox(false);
        itemTextBox.setVisible(false);

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        itemBlockNeck = new ItemBlockComponent(this, player, ItemBlock.NECK);
        itemBlockNeck.setX(WIDTH - 360);
        itemBlockNeck.setY(68);

        itemBlockHead = new ItemBlockComponent(this, player, ItemBlock.HEAD);
        itemBlockHead.setX(WIDTH - 280);
        itemBlockHead.setY(68);

        itemBlockBody = new ItemBlockComponent(this, player, ItemBlock.BODY);
        itemBlockBody.setX(WIDTH - 280);
        itemBlockBody.setY(148);

        itemBlockLeftHand = new ItemBlockComponent(this, player, ItemBlock.LEFT_HAND);
        itemBlockLeftHand.setX(WIDTH - 200);
        itemBlockLeftHand.setY(148);

        itemBlockRightHand = new ItemBlockComponent(this, player, ItemBlock.RIGHT_HAND);
        itemBlockRightHand.setX(WIDTH - 360);
        itemBlockRightHand.setY(148);

        itemBlockFinger = new ItemBlockComponent(this, player, ItemBlock.FINGER);
        itemBlockFinger.setX(WIDTH - 200);
        itemBlockFinger.setY(228);

        add(itemWindow, buttonAction, buttonDrop, inventoryGrid, itemBlockHead, itemBlockNeck, itemBlockBody,
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

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (hoveredItem != null) {
            itemTextBox.setText(hoveredItem.toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else if (selectedItem != null) {
            itemTextBox.setText(selectedItem.toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
            itemWindow.setVisible(false);
        }

        if (selectedItem != null) {
            buttonAction.setY(itemTextBox.getY() + 40);
            buttonDrop.setY(itemTextBox.getY() + 104);

            buttonAction.setVisible(selectedItem instanceof Usable || selectedItem instanceof Equipable);
            buttonAction.setText(selectedItem instanceof Usable ? "Use" : (player.isEquipedByItem(selectedItem) ? "Unequip" : "Equip"));
            if (selectedItem instanceof Equipable) {
                buttonAction.setEnabled(((Equipable) selectedItem).isEquipable(player));
            } else {
                buttonAction.setEnabled(true);
            }
            buttonDrop.setVisible(selectedItem != null);
        } else {
            buttonAction.setVisible(false);
            buttonDrop.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(DIALOG_OVERLAY_COLOR);
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
