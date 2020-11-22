package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.engine.configs.GUIConfig.DIALOG_OVERLAY_COLOR;
import static com.guillot.moria.ressources.Images.MAP_CURSOR;
import static com.guillot.moria.ressources.Images.PARCHMENT;
import static org.newdawn.slick.Input.KEY_ESCAPE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;

public class MapDialog extends SubView {

    private final static Color TRANSPARENT_COLOR = new Color(0f, 0f, 0f, 0f);

    private final static Color OPEN_DOOR_COLOR = new Color(Color.yellow);

    private final static Color LOCKED_DOOR_COLOR = new Color(Color.red);

    private final static Color STAIRS_COLOR = new Color(Color.cyan);

    private final static Color BLOCKED_COLOR = new Color(Color.gray);

    private final static int PIXEL_SIZE = 4;

    private GameState game;

    public MapDialog(GameView parent, GameState game) throws Exception {
        super(parent);

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
        g.setColor(DIALOG_OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int width = PARCHMENT.getImage().getWidth() * HEIGHT / PARCHMENT.getImage().getHeight();

        g.pushTransform();
        g.translate(x + (WIDTH - width) / 2, y);

        g.drawImage(PARCHMENT.getImage(), 0, 0, width, HEIGHT, 0, 0,
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
                    case RUBBLE:
                        g.setColor(BLOCKED_COLOR);
                        break;
                    case UP_STAIR:
                    case DOWN_STAIR:
                        g.setColor(STAIRS_COLOR);
                        break;
                    case GRANITE_WALL:
                    case MAGMA_WALL:
                    case PILLAR:
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
                }

                g.fillRect(j * PIXEL_SIZE, mapHeight - i * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }
        }

        Point playerPosition = game.getPlayer().getPosition();
        g.drawImage(MAP_CURSOR.getImage(), playerPosition.x * PIXEL_SIZE - 4, mapHeight - playerPosition.y * PIXEL_SIZE - 4);

        g.popTransform();

        String text = "Level " + game.getDungeon().getLevel();
        int widthText = GUI.get().getFont(2).getWidth(text);

        GUI.get().getFont(1).drawString(WIDTH / 2 - widthText / 2, HEIGHT - 80, text, Color.black);

        super.paint(g);
    }

}
