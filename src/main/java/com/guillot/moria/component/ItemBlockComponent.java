package com.guillot.moria.component;


import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.ItemBlock;

public class ItemBlockComponent extends Component {

    private final static Color LEGENDARY_COLOR = new Color(Color.yellow);

    private final static Color MAGIC_COLOR = new Color(Color.cyan);

    private final static Color BLOCK_COLOR = new Color(1f, 1f, 1f, .2f);

    private final static Color SELECTED_COLOR = new Color(1f, 1f, 1f, .5f);

    private final static int BLOCK_SIZE = 64;

    private InventoryDialog inventory;

    private AbstractCharacter player;

    private ItemBlock block;

    private AbstractItem item;

    public ItemBlockComponent(InventoryDialog inventory, AbstractCharacter player, ItemBlock block) {
        width = BLOCK_SIZE;
        height = BLOCK_SIZE;

        this.inventory = inventory;
        this.player = player;
        this.block = block;
    }

    @Override
    public void update() throws Exception {
        super.update();

        switch (block) {
        case BODY:
            item = (AbstractItem) player.getBody();
            break;
        case FINGER:
            item = (AbstractItem) player.getFinger();
            break;
        case HEAD:
            item = (AbstractItem) player.getHead();
            break;
        case LEFT_HAND:
            item = (AbstractItem) player.getLeftHand();
            break;
        case NECK:
            item = (AbstractItem) player.getNeck();
            break;
        case RIGHT_HAND:
            item = (AbstractItem) player.getRightHand();
            break;
        default:
            item = null;
            break;
        }

        if (mouseOn()) {
            inventory.setHoveredItem(item);

            if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                inventory.setSelectedItem(item);
            }

            if (GUI.get().getInput().isMousePressed(MOUSE_RIGHT_BUTTON) && item != null) {
                player.unequipItem(item);
            }

            inventory.showCursorTextBox(x - 16, y + BLOCK_SIZE + 16, block.toString());
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(BLOCK_COLOR);
        g.fillRect(x + 2, y + 2, BLOCK_SIZE, BLOCK_SIZE);

        if (item != null) {
            if (mouseOn() || inventory.getSelectedItem() == item) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(x + 2, y + 2, BLOCK_SIZE, BLOCK_SIZE);
            }

            g.drawImage(item.getImage(), x + 2, y + 2, x + BLOCK_SIZE + 2, y + BLOCK_SIZE + 2, 0, 0, 16, 16);

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
    }

}
