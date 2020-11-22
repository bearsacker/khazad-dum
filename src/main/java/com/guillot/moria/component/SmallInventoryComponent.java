package com.guillot.moria.component;


import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.guillot.engine.gui.Component;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;

public class SmallInventoryComponent extends Component {

    private final static int BLOCK_SIZE = 32;

    private final static Color BLOCK_COLOR = new Color(1f, 1f, 1f, .2f);

    private final static Color LEGENDARY_COLOR = new Color(Color.yellow);

    private final static Color MAGIC_COLOR = new Color(Color.cyan);

    private HashSet<Equipable> items;

    public SmallInventoryComponent(int x, int y) {
        width = 6 * BLOCK_SIZE;
        height = BLOCK_SIZE;
        this.x = x;
        this.y = y;
        items = new HashSet<>();
    }

    @Override
    public void update() throws Exception {
        super.update();
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
        g.setColor(BLOCK_COLOR);
        g.fillRect(2, 2, BLOCK_SIZE, BLOCK_SIZE);

        if (item != null) {
            g.drawImage(item.getImage(), 2, 2, BLOCK_SIZE + 2, BLOCK_SIZE + 2, 0, 0, 16, 16);

            Polygon shape = new Polygon(new float[] {0, 0, 8f, 0, 0, 8f});

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
