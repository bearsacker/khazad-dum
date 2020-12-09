package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Colors.WHITE;
import static java.lang.Math.max;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.Collections;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.dungeon.entity.Merchant;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class TradingDialog extends Window {

    private AbstractCharacter player;

    private Merchant merchant;

    private Window itemWindow;

    private TextBox itemTextBox;

    private InventoryGridComponent playerInventoryGrid;

    private InventoryGridComponent merchantInventoryGrid;

    private Text playerCoinsText;

    private Text merchantCoinsText;

    private Text itemCoinsText;

    private Button buyButton;

    private Button sellButton;

    public TradingDialog(GameView parent, GameState game) throws Exception {
        super(parent, 0, 0, WIDTH, HEIGHT, "Trade");
        setShowCloseButton(true);

        this.player = game.getPlayer();

        playerInventoryGrid = new InventoryGridComponent(null, player.getInventory(), player.getInventoryLimit(), 5);
        playerInventoryGrid.setX(96);
        playerInventoryGrid.setY(160);

        merchantInventoryGrid = new InventoryGridComponent(Merchant.INVENTORY_LIMIT, 5);
        merchantInventoryGrid.setX(WIDTH / 2 + 96);
        merchantInventoryGrid.setY(160);

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

        buyButton = new Button("Buy", WIDTH / 2 - 64, 272, 48);
        buyButton.setVisible(false);
        buyButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                buy();
            }
        });

        sellButton = new Button("Sell", WIDTH / 2 - 64, 272, 48);
        sellButton.setVisible(false);
        sellButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                sell();
            }
        });

        playerCoinsText =
                new Text("", playerInventoryGrid.getX() + 92, playerInventoryGrid.getY() - 72, GUI.get().getFont(3), WHITE.getColor());
        merchantCoinsText =
                new Text("", merchantInventoryGrid.getX() + 92, merchantInventoryGrid.getY() - 72, GUI.get().getFont(3), WHITE.getColor());
        itemCoinsText = new Text("", WIDTH - 96, HEIGHT - 80, GUI.get().getFont(2), WHITE.getColor());

        add(playerInventoryGrid, merchantInventoryGrid, itemWindow, itemTextBox, buyButton, sellButton, itemCoinsText, playerCoinsText,
                merchantCoinsText);
    }

    @Override
    public void onShow() throws Exception {
        setTitle(merchant.getName());

        Collections.sort(player.getInventory());
        merchantInventoryGrid.setItems(merchant.getInventory());
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        playerCoinsText.setText(Integer.toString(player.getCoins()));
        merchantCoinsText.setText(Integer.toString(merchant.getCoins()));

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        super.update(offsetX, offsetY);

        if (GUI.get().isMousePressed(MOUSE_LEFT_BUTTON)) {
            if (!playerInventoryGrid.mouseOn() && !sellButton.mouseOn() && !buyButton.mouseOn()) {
                playerInventoryGrid.setSelectedItem(null);
            }

            if (!merchantInventoryGrid.mouseOn() && !sellButton.mouseOn() && !buyButton.mouseOn()) {
                merchantInventoryGrid.setSelectedItem(null);
            }
        }

        sellButton.setVisible(playerInventoryGrid.getSelectedItem() != null);
        if (playerInventoryGrid.getSelectedItem() != null) {
            sellButton.setEnabled(
                    !merchant.isInventoryFull() && merchant.getCoins() >= playerInventoryGrid.getSelectedItem().getCoinsValue());
            sellButton.setText("Sell (" + playerInventoryGrid.getSelectedItem().getCoinsValue() + ") >");
            sellButton.setX(WIDTH / 2 - sellButton.getWidth() / 2);
        }

        buyButton.setVisible(merchantInventoryGrid.getSelectedItem() != null);
        if (merchantInventoryGrid.getSelectedItem() != null) {
            buyButton.setEnabled(!player.isInventoryFull()
                    && player.getCoins() >= merchantInventoryGrid.getSelectedItem().getCoinsValue(merchant.getMargin()));
            buyButton.setText("< Buy (" + merchantInventoryGrid.getSelectedItem().getCoinsValue(merchant.getMargin()) + ")");
            buyButton.setX(WIDTH / 2 - buyButton.getWidth() / 2);
        }

        AbstractItem displayingItem = nonNullFrom(playerInventoryGrid.getHoveredItem(), merchantInventoryGrid.getHoveredItem(),
                playerInventoryGrid.getSelectedItem(), merchantInventoryGrid.getSelectedItem());
        if (displayingItem != null) {
            itemTextBox.setText(displayingItem.toString());
            itemTextBox.setHeight(max(180, itemTextBox.getHeight()));
            itemTextBox.setY(HEIGHT - itemTextBox.getHeight() - 16);
            itemTextBox.setVisible(true);

            itemWindow.setHeight(itemTextBox.getHeight() + 64);
            itemWindow.setY(HEIGHT - itemWindow.getHeight());
            itemWindow.setVisible(true);

            float margin =
                    (merchantInventoryGrid.getHoveredItem() == displayingItem || merchantInventoryGrid.getHoveredItem() == displayingItem)
                            ? merchant.getMargin()
                            : 1f;

            itemCoinsText.setText(Integer.toString(displayingItem.getCoinsValue(margin)));
            itemCoinsText.setVisible(true);
        } else {
            itemTextBox.setVisible(false);
            itemWindow.setVisible(false);
            itemCoinsText.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(Images.GOLD_PURSE.getImage(), playerCoinsText.getX() - 80, playerCoinsText.getY() - 16, playerCoinsText.getX() - 16,
                playerCoinsText.getY() + 48, 0, 0, 32, 32, filter);

        g.drawImage(Images.GOLD_1.getImage(), playerCoinsText.getX() - 64, playerCoinsText.getY(), playerCoinsText.getX(),
                playerCoinsText.getY() + 64, 0, 0, 32, 32, filter);

        g.drawImage(Images.GOLD_PURSE.getImage(), merchantCoinsText.getX() - 80, merchantCoinsText.getY() - 16,
                merchantCoinsText.getX() - 16,
                merchantCoinsText.getY() + 48, 0, 0, 32, 32, filter);

        g.drawImage(Images.GOLD_1.getImage(), merchantCoinsText.getX() - 64, merchantCoinsText.getY(), merchantCoinsText.getX(),
                merchantCoinsText.getY() + 64, 0, 0, 32, 32, filter);

        if (itemCoinsText.isVisible()) {
            g.drawImage(Images.GOLD_2.getImage(), itemCoinsText.getX() - 64, itemCoinsText.getY() - 16, itemCoinsText.getX(),
                    itemCoinsText.getY() + 48, 0, 0, 32, 32, filter);
        }
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    private AbstractItem nonNullFrom(AbstractItem... items) {
        for (AbstractItem item : items) {
            if (item != null) {
                return item;
            }
        }

        return null;
    }

    private void buy() {
        AbstractItem item = merchantInventoryGrid.getSelectedItem();
        player.pickUpItem(item);
        player.setCoins(player.getCoins() - item.getCoinsValue(merchant.getMargin()));

        merchant.getInventory().remove(item);
        merchant.setCoins(merchant.getCoins() + item.getCoinsValue(merchant.getMargin()));
        merchantInventoryGrid.setSelectedItem(null);
    }

    private void sell() {
        AbstractItem item = playerInventoryGrid.getSelectedItem();
        player.dropItem(item);
        player.setCoins(player.getCoins() + item.getCoinsValue());

        merchant.getInventory().add(item);
        merchant.setCoins(merchant.getCoins() - item.getCoinsValue());
        playerInventoryGrid.setSelectedItem(null);
    }

}
