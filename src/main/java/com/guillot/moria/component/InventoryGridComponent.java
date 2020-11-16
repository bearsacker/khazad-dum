package com.guillot.moria.component;


import static org.newdawn.slick.Input.KEY_DELETE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

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

    private InventoryDialog inventory;

    private AbstractCharacter player;

    public InventoryGridComponent(InventoryDialog inventory, AbstractCharacter player) {
        this.inventory = inventory;
        this.player = player;

        width = INVENTORY_BLOCKS_WIDTH * BLOCK_SIZE;
        height = (player.getInventoryLimit() / INVENTORY_BLOCKS_WIDTH + 1) * BLOCK_SIZE;
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (mouseOn) {
            int x = (GUI.get().getMouseX() - this.x) / BLOCK_SIZE;
            int y = (GUI.get().getMouseY() - this.y) / BLOCK_SIZE;
            int index = y * INVENTORY_BLOCKS_WIDTH + x;

            if (index >= 0 && index < player.getInventory().size()) {
                inventory.setHoveredItem(player.getInventory().get(index));
            } else {
                inventory.setHoveredItem(null);
            }

            if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                if (inventory.getHoveredItem() != null) {
                    inventory.setSelectedItem(inventory.getHoveredItem());
                }
            }

            if (GUI.get().getInput().isMousePressed(MOUSE_RIGHT_BUTTON)) {
                if (inventory.getHoveredItem() != null) {
                    inventory.setSelectedItem(inventory.getHoveredItem());
                    inventory.useOrEquipeItem(inventory.getHoveredItem());
                }
            }
        } else {
            inventory.setHoveredItem(null);
        }

        if (GUI.get().isKeyPressed(KEY_DELETE) && inventory.getSelectedItem() != null) {
            inventory.dropItem(inventory.getSelectedItem());
        }
    }

    @Override
    public void paint(Graphics g) {
        g.pushTransform();
        g.translate(this.x, this.y);

        for (int i = 0; i < player.getInventoryLimit(); i++) {
            int x = (i % INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;
            int y = (i / INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;

            g.setColor(BLOCK_COLOR);
            g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                    BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
        }

        for (int i = 0; i < player.getInventory().size(); i++) {
            AbstractItem item = player.getInventory().get(i);
            int x = (i % INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;
            int y = (i / INVENTORY_BLOCKS_WIDTH) * BLOCK_SIZE;

            if (player.isEquipedByItem(item)) {
                g.setColor(EQUIPED_COLOR);
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            } else if (inventory.getSelectedItem() == item || inventory.getHoveredItem() == item) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            if (item instanceof Equipable && !((Equipable) item).isEquipable(player)) {
                g.setColor(NOT_EQUIPABLE_COLOR);
                g.fillRect(x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, BLOCK_SIZE - BORDER_BLOCK_SIZE * 2,
                        BLOCK_SIZE - BORDER_BLOCK_SIZE * 2);
            }

            g.drawImage(item.getImage(), x + BORDER_BLOCK_SIZE, y + BORDER_BLOCK_SIZE, x + BLOCK_SIZE - BORDER_BLOCK_SIZE,
                    y + BLOCK_SIZE - BORDER_BLOCK_SIZE, 0, 0, 16, 16);

            Polygon shape = new Polygon(new float[] {x, y, x + 16f, y, x, y + 16f});

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

        g.popTransform();
    }

}
