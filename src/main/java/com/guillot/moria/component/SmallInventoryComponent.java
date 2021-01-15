package com.guillot.moria.component;


import static com.guillot.moria.resources.Colors.ITEM_BLOCK;
import static com.guillot.moria.resources.Colors.ITEM_LEGENDARY;
import static com.guillot.moria.resources.Colors.ITEM_MAGIC;

import java.util.HashSet;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;

public class SmallInventoryComponent extends Component {

    private final static int BLOCK_SIZE = 36;

    private HashSet<Equipable> items;

    public SmallInventoryComponent(int x, int y) {
        width = 6 * BLOCK_SIZE;
        height = BLOCK_SIZE;
        this.x = x;
        this.y = y;
        items = new HashSet<>();
    }

    @Override
    public void paint(Graphics g) {
        g.pushTransform();
        g.translate(x, y);

        for (Equipable item : items) {
            paintItem(g, (AbstractItem) item);
            g.translate(BLOCK_SIZE + 8, 0);
        }

        g.popTransform();
    }

    private void paintItem(Graphics g, AbstractItem item) {
        g.setColor(ITEM_BLOCK.getColor());
        g.fillRect(2, 2, BLOCK_SIZE, BLOCK_SIZE);

        if (item != null) {
            g.drawImage(item.getImage(), 4, 4, BLOCK_SIZE, BLOCK_SIZE, 0, 0, 32, 32);

            Polygon shape = new Polygon(new float[] {0, 0, 8f, 0, 0, 8f});

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
    }

    public void setItems(AbstractCharacter character) {
        items.clear();

        items.add(character.getLeftHand());
        if (character.getLeftHand() != character.getRightHand()) {
            items.add(character.getRightHand());
        }
        items.add(character.getBody());
        items.add(character.getHead());
        items.add(character.getNeck());
        items.add(character.getFinger());
        items.remove(null);
    }

}
