package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.DIALOG_OVERLAY_COLOR;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_DELETE;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import java.util.Collections;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemBlock;
import com.guillot.moria.item.Usable;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class InventoryDialog extends Window {

    private GameView parent;

    private GameState game;

    private AbstractCharacter player;

    private Button buttonAction;

    private Button buttonDrop;

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

    public InventoryDialog(GameView parent, GameState game) throws Exception {
        super(parent, 0, 0, WIDTH, HEIGHT, "Inventory");
        setShowCloseButton(true);

        this.parent = parent;
        this.game = game;
        this.player = game.getPlayer();

        buttonAction = new Button("Use", WIDTH - 336, 72, 224, 48);
        buttonAction.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useOrEquipeItem(inventoryGrid.getSelectedItem());
            }
        });
        buttonAction.setVisible(false);

        buttonDrop = new Button("Drop", WIDTH - 336, 136, 224, 48);
        buttonDrop.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                dropItem(inventoryGrid.getSelectedItem());
            }
        });
        buttonDrop.setVisible(false);

        inventoryGrid = new InventoryGridComponent(player);
        inventoryGrid.setX(128);
        inventoryGrid.setY(128);

        itemWindow = new Window(parent, 0, HEIGHT, WIDTH, 0);
        itemWindow.setImageHeader(new Image("gui/window_header_alternate.png"));
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
        itemBlockNeck.setY(132);

        itemBlockHead = new ItemBlockComponent(this, player, ItemBlock.HEAD);
        itemBlockHead.setX(WIDTH - 280);
        itemBlockHead.setY(132);

        itemBlockBody = new ItemBlockComponent(this, player, ItemBlock.BODY);
        itemBlockBody.setX(WIDTH - 280);
        itemBlockBody.setY(212);

        itemBlockLeftHand = new ItemBlockComponent(this, player, ItemBlock.LEFT_HAND);
        itemBlockLeftHand.setX(WIDTH - 200);
        itemBlockLeftHand.setY(212);

        itemBlockRightHand = new ItemBlockComponent(this, player, ItemBlock.RIGHT_HAND);
        itemBlockRightHand.setX(WIDTH - 360);
        itemBlockRightHand.setY(212);

        itemBlockFinger = new ItemBlockComponent(this, player, ItemBlock.FINGER);
        itemBlockFinger.setX(WIDTH - 200);
        itemBlockFinger.setY(292);

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

        if (GUI.get().getInput().isMousePressed(MOUSE_RIGHT_BUTTON) && inventoryGrid.getHoveredItem() != null) {
            useOrEquipeItem(getHoveredItem());
        }

        if (GUI.get().isKeyPressed(KEY_DELETE) && inventoryGrid.getSelectedItem() != null) {
            dropItem(inventoryGrid.getSelectedItem());
        }

        if (inventoryGrid.getHoveredItem() != null) {
            itemTextBox.setText(inventoryGrid.getHoveredItem().toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight());
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 40);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else if (inventoryGrid.getSelectedItem() != null) {
            itemTextBox.setText(inventoryGrid.getSelectedItem().toString());
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

        if (inventoryGrid.getSelectedItem() != null) {
            buttonAction.setY(itemTextBox.getY() + 40);
            buttonDrop.setY(itemTextBox.getY() + 104);

            buttonAction
                    .setVisible(inventoryGrid.getSelectedItem() instanceof Usable || inventoryGrid.getSelectedItem() instanceof Equipable);
            buttonAction.setText(inventoryGrid.getSelectedItem() instanceof Usable ? "Use"
                    : (player.isEquipedByItem(inventoryGrid.getSelectedItem()) ? "Unequip" : "Equip"));
            if (inventoryGrid.getSelectedItem() instanceof Equipable) {
                buttonAction.setEnabled(((Equipable) inventoryGrid.getSelectedItem()).isEquipable(player));
            } else {
                buttonAction.setEnabled(true);
            }
            buttonDrop.setVisible(inventoryGrid.getSelectedItem() != null);
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
                    game.addMessage("GREEN_PALE@@You@@WHITE@@ use @@" + item.getFormattedName());
                    inventoryGrid.setSelectedItem(null);
                    inventoryGrid.setHoveredItem(null);
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

            game.addMessage("GREEN_PALE@@You@@WHITE@@ drop @@" + item.getFormattedName());
            inventoryGrid.setSelectedItem(null);
            inventoryGrid.setHoveredItem(null);
        }
    }

    public void showCursorTextBox(int x, int y, String text) {
        cursorTextBox.setText(text);
        cursorTextBox.setX(x);
        cursorTextBox.setY(y);
        cursorTextBox.setVisible(true);
    }

    public AbstractItem getSelectedItem() {
        return inventoryGrid.getSelectedItem();
    }

    public void setSelectedItem(AbstractItem selectedItem) {
        inventoryGrid.setSelectedItem(selectedItem);
    }

    public AbstractItem getHoveredItem() {
        return inventoryGrid.getHoveredItem();
    }

    public void setHoveredItem(AbstractItem hoveredItem) {
        inventoryGrid.setHoveredItem(hoveredItem);
    }
}
