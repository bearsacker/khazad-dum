package com.guillot.moria.component;


import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;

public class InventoryGridComponent extends Component {

    private final static int INVENTORY_BLOCKS_WIDTH = 8;

    private final static int BLOCK_SIZE = 80;

    private final static int BORDER_BLOCK_SIZE = 8;

    private final static Color BLOCK_COLOR = new Color(1f, 1f, 1f, .2f);

    private final static Color SELECTED_COLOR = new Color(1f, 1f, 1f, .5f);

    private final static Color EQUIPED_COLOR = new Color(1f, 1f, 0f, .5f);

    private final static Color NOT_EQUIPABLE_COLOR = new Color(1f, 0f, 0f, .5f);

    private final static Color LEGENDARY_COLOR = new Color(Color.yellow);

    private final static Color MAGIC_COLOR = new Color(Color.cyan);

    private AbstractCharacter player;

    private ArrayList<AbstractItem> items;

    private int limit;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    public InventoryGridComponent(AbstractCharacter player) {
        this(player, player.getInventory(), player.getInventoryLimit());
    }

    public InventoryGridComponent(AbstractCharacter player, ArrayList<AbstractItem> items, int limit) {
        this.player = player;
        this.items = items;
        this.limit = limit;

        width = INVENTORY_BLOCKS_WIDTH * BLOCK_SIZE;
        height = (limit / INVENTORY_BLOCKS_WIDTH + 1) * BLOCK_SIZE;
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (mouseOn) {
            int x = (GUI.get().getMouseX() - this.x) / BLOCK_SIZE;
            int y = (GUI.get().getMouseY() - this.y) / BLOCK_SIZE;
            int index = y * INVENTORY_BLOCKS_WIDTH + x;

            if (index >= 0 && index < items.size()) {
                hoveredItem = items.get(index);
            } else {
                hoveredItem = null;
            }

            if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                if (hoveredItem != null) {
                    selectedItem = hoveredItem;
                }
            }

        } else {
            hoveredItem = null;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.pushTransform();
        g.translate(this.x, this.y);

        for (int i = 0; i < limit; i++) {
            int x = (i % INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;
            int y = (i / INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;

            g.setColor(BLOCK_COLOR);
            g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                    BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
        }

        for (int i = 0; i < items.size(); i++) {
            AbstractItem item = items.get(i);
            int x = (i % INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;
            int y = (i / INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;

            if (player != null && player.isEquipedByItem(item)) {
                g.setColor(EQUIPED_COLOR);
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            } else if (selectedItem == item || hoveredItem == item) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            if (player != null && item instanceof Equipable && !((Equipable) item).isEquipable(player)) {
                g.setColor(NOT_EQUIPABLE_COLOR);
                g.drawRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            g.drawImage(item.getImage(), x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, x + BLOCK_SIZE - BORDER_BLOCK_SIZE,
                    y + BLOCK_SIZE - BORDER_BLOCK_SIZE, 0, 0, 16, 16);

            g.pushTransform();
            g.translate(x + BORDER_BLOCK_SIZE - 2, y + BORDER_BLOCK_SIZE - 2);

            Polygon shape = new Polygon(new float[] {0, 0, 16f, 0, 0, 16f});

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

            g.popTransform();
        }

        g.popTransform();
    }

    public AbstractItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(AbstractItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public AbstractItem getHoveredItem() {
        return hoveredItem;
    }

    public void setHoveredItem(AbstractItem hoveredItem) {
        this.hoveredItem = hoveredItem;
    }

}
