package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.WHITE;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_DELETE;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import java.util.Collections;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemBlock;
import com.guillot.moria.item.Usable;
import com.guillot.moria.ressources.Images;
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

    private Text coinsText;

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
        inventoryGrid.setX(96);
        inventoryGrid.setY(128);

        itemWindow = new Window(parent, 0, HEIGHT, WIDTH, 0);
        itemWindow.setImageHeader(Images.WINDOW_HEADER_ALTERNATE.getImage());
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
        itemBlockHead.setX(WIDTH - 272);
        itemBlockHead.setY(132);

        itemBlockBody = new ItemBlockComponent(this, player, ItemBlock.BODY);
        itemBlockBody.setX(WIDTH - 272);
        itemBlockBody.setY(220);

        itemBlockLeftHand = new ItemBlockComponent(this, player, ItemBlock.LEFT_HAND);
        itemBlockLeftHand.setX(WIDTH - 184);
        itemBlockLeftHand.setY(220);

        itemBlockRightHand = new ItemBlockComponent(this, player, ItemBlock.RIGHT_HAND);
        itemBlockRightHand.setX(WIDTH - 360);
        itemBlockRightHand.setY(220);

        itemBlockFinger = new ItemBlockComponent(this, player, ItemBlock.FINGER);
        itemBlockFinger.setX(WIDTH - 184);
        itemBlockFinger.setY(308);

        coinsText = new Text("", inventoryGrid.getX() + 96, inventoryGrid.getY() + inventoryGrid.getHeight() + 64, GUI.get().getFont(3),
                WHITE.getColor());

        add(itemWindow, buttonAction, buttonDrop, inventoryGrid, itemBlockHead, itemBlockNeck, itemBlockBody,
                itemBlockLeftHand, itemBlockRightHand, itemBlockFinger, coinsText, itemTextBox, cursorTextBox);
    }

    @Override
    public void onShow() throws Exception {
        Collections.sort(player.getInventory());
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        cursorTextBox.setVisible(false);

        if (!inventoryGrid.mouseOn() && !buttonAction.mouseOn() && !buttonDrop.mouseOn()
                && GUI.get().isMousePressed(MOUSE_LEFT_BUTTON)) {
            inventoryGrid.setSelectedItem(null);
        }

        coinsText.setText(Integer.toString(player.getCoins()));

        super.update(offsetX, offsetY);

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (GUI.get().isMousePressed(MOUSE_RIGHT_BUTTON) && inventoryGrid.getHoveredItem() != null) {
            useOrEquipeItem(getHoveredItem());
        }

        if (GUI.get().isKeyPressed(KEY_DELETE) && inventoryGrid.getSelectedItem() != null) {
            dropItem(inventoryGrid.getSelectedItem());
        }

        AbstractItem displayingItem = nonNullFrom(inventoryGrid.getHoveredItem(), inventoryGrid.getHoveredItem());
        if (displayingItem != null) {
            itemTextBox.setText(displayingItem.toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight());
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 40);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
            itemWindow.setVisible(false);
        }

        if (inventoryGrid.getSelectedItem() != null) {
            buttonAction.setY(itemTextBox.getY() + 32);
            buttonDrop.setY(itemTextBox.getY() + 96);

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
        super.paint(g);

        g.drawImage(Images.GOLD_COST.getImage(), coinsText.getX() - 80, coinsText.getY() - 16, coinsText.getX() - 16,
                coinsText.getY() + 56, 0, 0, 32, 32, filter);
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

    private AbstractItem nonNullFrom(AbstractItem... items) {
        for (AbstractItem item : items) {
            if (item != null) {
                return item;
            }
        }

        return null;
    }
}
