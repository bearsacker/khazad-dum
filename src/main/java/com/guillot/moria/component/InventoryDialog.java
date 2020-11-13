package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_DELETE;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.KEY_I;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.TextBox;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.Usable;
import com.guillot.moria.views.GameView;

public class InventoryDialog extends SubView {

    private final static Color OVERLAY_COLOR = new Color(0f, 0f, 0f, .75f);

    private final static Color BLOCK_COLOR = new Color(1f, 1f, 1f, .2f);

    private final static Color SELECTED_COLOR = new Color(1f, 1f, 1f, .5f);

    private final static Color EQUIPED_COLOR = new Color(1f, 1f, 0f, .5f);

    private final static Color LEGENDARY_COLOR = new Color(Color.yellow);

    private final static Color MAGIC_COLOR = new Color(Color.cyan);

    private final static int INVENTORY_WIDTH = 10;

    private GameView parent;

    private AbstractCharacter player;

    private Button buttonAction;

    private Button buttonDrop;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    private TextBox itemTextBox;

    public InventoryDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent);

        this.parent = parent;
        this.player = player;

        buttonAction = new Button("Use", WIDTH - 256 - 4, 64, 192, 32);
        buttonAction.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                useOrEquipeItem(selectedItem);
            }
        });
        buttonAction.setVisible(false);

        buttonDrop = new Button("Drop", WIDTH - 256 - 4, 108, 192, 32);
        buttonDrop.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                dropItem(selectedItem);
            }
        });
        buttonDrop.setVisible(false);

        itemTextBox = new TextBox();
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setVisible(false);

        add(buttonAction, buttonDrop, itemTextBox);
    }

    @Override
    public void onShow() throws Exception {
        Collections.sort(player.getInventory());
    }

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE) || GUI.get().isKeyPressed(KEY_I)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (GUI.get().getMouseX() >= 64 && GUI.get().getMouseX() < INVENTORY_WIDTH * 40 + 64 && GUI.get().getMouseY() >= 64
                && GUI.get().getMouseY() < player.getInventory().size() / INVENTORY_WIDTH * 40 + 64 + 40) {
            int x = (GUI.get().getMouseX() - 64) / 40;
            int y = (GUI.get().getMouseY() - 64) / 40;
            int index = y * INVENTORY_WIDTH + x;

            if (index >= 0 && index < player.getInventory().size()) {
                hoveredItem = player.getInventory().get(index);
            } else {
                hoveredItem = null;
            }
        } else {
            hoveredItem = null;
        }

        if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            if (hoveredItem != null) {
                selectedItem = hoveredItem;
            }
        }

        if (GUI.get().getInput().isMousePressed(MOUSE_RIGHT_BUTTON)) {
            useOrEquipeItem(hoveredItem);
        }

        if (GUI.get().isKeyPressed(KEY_DELETE) && selectedItem != null) {
            dropItem(selectedItem);
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

        for (int i = 0; i < player.getInventoryLimit(); i++) {
            int x = (i % INVENTORY_WIDTH) * 40 + 64;
            int y = (i / INVENTORY_WIDTH) * 40 + 64;

            g.setColor(BLOCK_COLOR);
            g.fillRect(x + 2, y + 2, 36, 36);
        }

        for (int i = 0; i < player.getInventory().size(); i++) {
            AbstractItem item = player.getInventory().get(i);
            int x = (i % INVENTORY_WIDTH) * 40 + 64;
            int y = (i / INVENTORY_WIDTH) * 40 + 64;

            if (player.isEquipedByItem(item)) {
                g.setColor(EQUIPED_COLOR);
                g.fillRect(x + 2, y + 2, 36, 36);
            } else if (selectedItem == item || hoveredItem == item) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(x + 2, y + 2, 36, 36);
            }

            g.drawImage(item.getImage(), x + 3, y + 4, x + 35, y + 36, 0, 0, 16, 16);

            Polygon shape = new Polygon(new float[] {x, y, x + 8f, y, x, y + 8f});

            switch (item.getRarity()) {
            case LEGENDARY:
                g.setColor(LEGENDARY_COLOR);
                g.fill(shape);
                break;
            case MAGIC:
                g.setColor(MAGIC_COLOR);
                g.fill(shape);
                break;
            default:
                break;
            }
        }

        super.paint(g);
    }

    private void useOrEquipeItem(AbstractItem item) {
        if (item != null) {
            if (item instanceof Usable) {
                if (((Usable) item).use(player)) {
                    player.getInventory().remove(item);
                    parent.getConsole().addMessage(player.getName() + " uses " + item.getName());
                    item = null;
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

    private void dropItem(AbstractItem item) {
        if (player.dropItem(parent.getDungeon(), item)) {
            player.unequipItem(item);

            parent.getConsole().addMessage(player.getName() + " drops " + item.getName());
            item = null;
        }
    }
}
