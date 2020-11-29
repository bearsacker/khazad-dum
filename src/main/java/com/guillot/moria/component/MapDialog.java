package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Images.MAP_CURSOR;
import static com.guillot.moria.ressources.Images.PARCHMENT;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Window;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.Entity;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class MapDialog extends Window {

    private final static Color TRANSPARENT_COLOR = new Color(0f, 0f, 0f, 0f);

    private final static Color OPEN_DOOR_COLOR = new Color(Color.yellow);

    private final static Color LOCKED_DOOR_COLOR = new Color(Color.red);

    private final static Color STAIRS_COLOR = new Color(Color.cyan);

    private final static Color BLOCKED_COLOR = new Color(Color.gray);

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
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = PARCHMENT.getImage().getWidth() * (HEIGHT - 112) / PARCHMENT.getImage().getHeight();

        g.pushTransform();
        g.translate(32, 64);

        g.drawImage(PARCHMENT.getImage(), 0, 0, width, HEIGHT - 112, 0, 0,
                PARCHMENT.getImage().getWidth(), PARCHMENT.getImage().getHeight());

        g.popTransform();

        int mapWidth = game.getDungeon().getWidth() * PIXEL_SIZE;
        int mapHeight = game.getDungeon().getHeight() * PIXEL_SIZE;

        g.pushTransform();
        g.translate(x + (WIDTH - mapWidth) / 2, y + (HEIGHT - mapHeight) / 2);

        for (int i = 0; i < game.getDungeon().getHeight(); i++) {
            for (int j = 0; j < game.getDungeon().getWidth(); j++) {
                Tile tile = game.getDungeon().getDiscoveredTiles()[i][j];

                float alpha = .5f;
                g.setColor(TRANSPARENT_COLOR);

                if (tile != null) {
                    switch (tile) {
                    case UP_STAIR:
                    case DOWN_STAIR:
                        g.setColor(STAIRS_COLOR);
                        break;
                    case GRANITE_WALL:
                    case MAGMA_WALL:
                    case QUARTZ_WALL:
                    case TMP1_WALL:
                    case TMP2_WALL:
                        g.setColor(new Color(0f, 0f, 0f, alpha));
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
                            g.setColor(LOCKED_DOOR_COLOR);
                            break;
                        case OPEN:
                            g.setColor(OPEN_DOOR_COLOR);
                            break;
                        case STUCK:
                            g.setColor(BLOCKED_COLOR);
                            break;
                        default:
                            break;
                        }
                    }

                    Entity entity = game.getDungeon().getEntityAt(new Point(j, i));
                    if (entity != null) {
                        switch (entity) {
                        case PILLAR:
                        case RUBBLE:
                            g.setColor(BLOCKED_COLOR);
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
