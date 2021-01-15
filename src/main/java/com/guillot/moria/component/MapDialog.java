package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.resources.Colors.MAP_LOCKED_DOOR;
import static com.guillot.moria.resources.Colors.MAP_OPEN_DOOR;
import static com.guillot.moria.resources.Colors.TRANSPARENT;
import static com.guillot.moria.resources.Images.MAP_CURSOR;
import static com.guillot.moria.resources.Images.PARCHMENT;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Window;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.dungeon.entity.AbstractEntity;
import com.guillot.moria.dungeon.entity.Door;
import com.guillot.moria.resources.Colors;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class MapDialog extends Window {

    private final static int PIXEL_SIZE = 4;

    private GameState game;

    public MapDialog(GameView parent, GameState game) throws Exception {
        super(parent, 0, 0, WIDTH, HEIGHT, "Maps");
        setShowCloseButton(true);

        this.game = game;
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        super.update(offsetX, offsetY);

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = PARCHMENT.getImage().getWidth() * (HEIGHT - 80) / PARCHMENT.getImage().getHeight();

        g.pushTransform();
        g.translate(16, 48);

        g.drawImage(PARCHMENT.getImage(), 0, 0, width, HEIGHT - 80, 0, 0,
                PARCHMENT.getImage().getWidth(), PARCHMENT.getImage().getHeight());

        g.popTransform();

        int mapHeight = game.getDungeon().getHeight() * PIXEL_SIZE;

        g.pushTransform();
        g.translate(48, y + (HEIGHT - mapHeight) / 2);

        for (int i = 0; i < game.getDungeon().getHeight(); i++) {
            for (int j = 0; j < game.getDungeon().getWidth(); j++) {
                Tile tile = game.getDungeon().getDiscoveredTiles()[i][j];

                float alpha = .2f;
                g.setColor(TRANSPARENT.getColor());

                if (tile != null) {
                    switch (tile) {
                    case GRANITE_WALL:
                    case MAGMA_WALL:
                    case QUARTZ_WALL:
                    case TMP1_WALL:
                    case TMP2_WALL:
                        g.setColor(new Color(0f, 0f, 0f, 1f - alpha));
                        break;
                    case ROOM_FLOOR:
                    case CORRIDOR_FLOOR:
                        g.setColor(new Color(1f, 1f, 1f, 1f - alpha));
                        break;
                    default:
                        break;
                    }

                    Door door = game.getDungeon().getDoorAt(new Point(j, i));
                    if (door != null) {
                        switch (door.getState()) {
                        case LOCKED:
                            g.setColor(MAP_LOCKED_DOOR.getColor());
                            break;
                        case OPEN:
                            g.setColor(MAP_OPEN_DOOR.getColor());
                            break;
                        case STUCK:
                            g.setColor(new Color(0f, 0f, 0f, 1f - alpha));
                            break;
                        default:
                            break;
                        }
                    }

                    AbstractEntity entity = game.getDungeon().getEntityAt(new Point(j, i));
                    if (entity != null) {
                        switch (entity.getType()) {
                        case CHEST:
                        case MERCHANT:
                            g.setColor(Colors.ITEM_LEGENDARY.getColor());
                            break;
                        case DOOR:
                            break;
                        case UPSTAIRS:
                        case DOWNSTAIRS:
                            g.setColor(Colors.ITEM_MAGIC.getColor());
                        default:
                            g.setColor(Colors.GRAY.getColor());
                            break;
                        }
                    }
                }

                g.fillRect(j * PIXEL_SIZE, mapHeight - i * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }
        }

        Point playerPosition = game.getPlayer().getPosition();
        g.drawImage(MAP_CURSOR.getImage(), playerPosition.x * PIXEL_SIZE - 4, mapHeight - playerPosition.y * PIXEL_SIZE - 4);

        g.popTransform();
    }

}
