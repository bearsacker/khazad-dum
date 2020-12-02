package com.guillot.moria.component;


import static com.guillot.moria.ressources.Colors.ITEM_BLOCK;
import static com.guillot.moria.ressources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.ressources.Colors.ITEM_MAGIC;
import static com.guillot.moria.ressources.Colors.ITEM_SELECTED;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.engine.gui.GUI;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.ItemBlock;

public class ItemBlockComponent extends Component {

    private final static int BLOCK_SIZE = 72;

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
    public void update(int offsetX, int offsetY) throws Exception {
        super.update(offsetX, offsetY);

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
        g.pushTransform();
        g.translate(x, y);

        g.setColor(ITEM_BLOCK.getColor());
        g.fillRect(2, 2, BLOCK_SIZE, BLOCK_SIZE);

        if (item != null) {
            if (mouseOn() || inventory.getSelectedItem() == item) {
                g.setColor(ITEM_SELECTED.getColor());
                g.fillRect(2, 2, BLOCK_SIZE, BLOCK_SIZE);
            }

            g.drawImage(item.getImage(), 6, 6, BLOCK_SIZE - 2, BLOCK_SIZE - 2, 0, 0, 32, 32);

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
        }

        g.popTransform();
    }

}
