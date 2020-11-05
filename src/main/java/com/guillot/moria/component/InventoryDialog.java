package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

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

    private final static Color HOVERED_COLOR = new Color(1f, 1f, 1f, .1f);

    private final static Color SELECTED_COLOR = new Color(1f, 1f, 1f, .5f);

    private final static Color LEGENDARY_COLOR = new Color(Color.yellow);

    private final static Color MAGIC_COLOR = new Color(Color.cyan);

    private final static int INVENTORY_WIDTH = 11;

    private AbstractCharacter player;

    private Button buttonUse;

    private Button buttonEquip;

    private AbstractItem selectedItem;

    private AbstractItem hoveredItem;

    private TextBox itemTextBox;

    public InventoryDialog(GameView parent, AbstractCharacter player) throws Exception {
        super(parent);

        this.player = player;

        buttonUse = new Button("Use", WIDTH - 256 - 36, 36, 256, 32);
        buttonUse.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                if (((Usable) selectedItem).use(player)) {
                    player.getInventory().remove(selectedItem);
                    parent.getConsole().addMessage(player.getName() + " use " + selectedItem.getName());
                    selectedItem = null;
                }
            }
        });
        buttonUse.setEnabled(false);

        buttonEquip = new Button("Equip", WIDTH - 256 - 36, 80, 256, 32);
        buttonEquip.setEvent(new Event() {

            @Override
            public void perform() throws Exception {

            }
        });
        buttonEquip.setEnabled(false);

        itemTextBox = new TextBox();
        itemTextBox.setAutoWidth(false);
        itemTextBox.setWidth(WIDTH);
        itemTextBox.setVisible(false);

        add(buttonUse, buttonEquip, itemTextBox);
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

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }

        if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            if (GUI.get().getMouseX() >= 32 && GUI.get().getMouseX() < INVENTORY_WIDTH * 40 + 32 && GUI.get().getMouseY() >= 32
                    && GUI.get().getMouseY() < player.getInventory().size() / INVENTORY_WIDTH * 40 + 32 + 40) {
                int x = (GUI.get().getMouseX() - 32) / 40;
                int y = (GUI.get().getMouseY() - 32) / 40;
                int index = y * INVENTORY_WIDTH + x;

                if (index >= 0 && index < player.getInventory().size()) {
                    selectedItem = player.getInventory().get(index);
                } else {
                    selectedItem = null;
                }
            }
        }

        if (GUI.get().getMouseX() >= 32 && GUI.get().getMouseX() < INVENTORY_WIDTH * 40 + 32 && GUI.get().getMouseY() >= 32
                && GUI.get().getMouseY() < player.getInventory().size() / INVENTORY_WIDTH * 40 + 32 + 40) {
            int x = (GUI.get().getMouseX() - 32) / 40;
            int y = (GUI.get().getMouseY() - 32) / 40;
            int index = y * INVENTORY_WIDTH + x;

            if (index >= 0 && index < player.getInventory().size()) {
                hoveredItem = player.getInventory().get(index);
            } else {
                hoveredItem = null;
            }
        } else {
            hoveredItem = null;
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

        buttonUse.setEnabled(selectedItem != null && selectedItem instanceof Usable);
        buttonEquip.setEnabled(selectedItem != null && selectedItem instanceof Equipable);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < player.getInventory().size(); i++) {
            AbstractItem item = player.getInventory().get(i);
            int x = (i % INVENTORY_WIDTH) * 40 + 32;
            int y = (i / INVENTORY_WIDTH) * 40 + 32;

            if (selectedItem == item) {
                g.setColor(SELECTED_COLOR);
                g.fillRect(x + 2, y + 2, 36, 36);
            } else if (hoveredItem == item) {
                g.setColor(HOVERED_COLOR);
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

}
