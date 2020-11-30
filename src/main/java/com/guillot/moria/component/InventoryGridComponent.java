package com.guillot.moria.component;


import static com.guillot.moria.ressources.Colors.ITEM_BLOCK;
import static com.guillot.moria.ressources.Colors.ITEM_EQUIPED;
import static com.guillot.moria.ressources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.ressources.Colors.ITEM_MAGIC;
import static com.guillot.moria.ressources.Colors.ITEM_NOT_EQUIPABLE;
import static com.guillot.moria.ressources.Colors.ITEM_SELECTED;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;

public class InventoryGridComponent extends Component {

    private final static int INVENTORY_BLOCKS_WIDTH = 8;

    private final static int BLOCK_SIZE = 88;

    private final static int BORDER_BLOCK_SIZE = 8;

    private AbstractCharacter player;

    private ArrayList<AbstractItem> items;

    private int blocksWidth;

    private int limit;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    public InventoryGridComponent(AbstractCharacter player) {
        this(player, player.getInventory(), player.getInventoryLimit(), INVENTORY_BLOCKS_WIDTH);
    }

    public InventoryGridComponent(AbstractCharacter player, ArrayList<AbstractItem> items, int limit, int blocksWidth) {
        this.player = player;
        this.items = items;
        this.limit = limit;
        this.blocksWidth = blocksWidth;

        width = blocksWidth * BLOCK_SIZE;
        height = (limit / blocksWidth + 1) * BLOCK_SIZE;
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (mouseOn) {
            int x = (GUI.get().getMouseX() - this.x) / BLOCK_SIZE;
            int y = (GUI.get().getMouseY() - this.y) / BLOCK_SIZE;
            int index = y * blocksWidth + x;

            if (index >= 0 && index < items.size()) {
                hoveredItem = items.get(index);
            } else {
                hoveredItem = null;
            }

            if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                selectedItem = hoveredItem;
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
            int x = (i % blocksWidth) * BLOCK_SIZE;
            int y = (i / blocksWidth) * BLOCK_SIZE;

            g.setColor(ITEM_BLOCK.getColor());
            g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                    BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
        }

        for (int i = 0; i < items.size(); i++) {
            AbstractItem item = items.get(i);
            int x = (i % blocksWidth) * BLOCK_SIZE;
            int y = (i / blocksWidth) * BLOCK_SIZE;

            if (player != null && player.isEquipedByItem(item)) {
                g.setColor(ITEM_EQUIPED.getColor());
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            } else if (selectedItem == item || hoveredItem == item) {
                g.setColor(ITEM_SELECTED.getColor());
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            if (player != null && item instanceof Equipable && !((Equipable) item).isEquipable(player)) {
                g.setColor(ITEM_NOT_EQUIPABLE.getColor());
                g.drawRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            g.drawImage(item.getImage(), x + BORDER_BLOCK_SIZE + 4, y + BORDER_BLOCK_SIZE + 4, x + BLOCK_SIZE - 4 - BORDER_BLOCK_SIZE,
                    y + BLOCK_SIZE - 4 - BORDER_BLOCK_SIZE, 0, 0, 32, 32);

            g.pushTransform();
            g.translate(x + BORDER_BLOCK_SIZE - 2, y + BORDER_BLOCK_SIZE - 2);

            Polygon shape = new Polygon(new float[] {0, 0, 16f, 0, 0, 16f});

            switch (item.getRarity()) {
            case LEGENDARY:
                g.setColor(ITEM_LEGENDARY.getColor());
                g.fill(shape);
                break;
            case MAGIC:
                g.setColor(ITEM_MAGIC.getColor());
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
