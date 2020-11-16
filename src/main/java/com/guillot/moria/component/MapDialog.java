package com.guillot.moria.component;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.ressources.Images.PARCHMENT;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.KEY_M;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.utils.Point;
import com.guillot.moria.views.GameView;

public class MapDialog extends SubView {

    private final static Color OVERLAY_COLOR = new Color(0f, 0f, 0f, .75f);

    private final static Color TRANSPARENT_COLOR = new Color(0f, 0f, 0f, 0f);

    private final static Color OPEN_DOOR_COLOR = new Color(Color.yellow);

    private final static Color LOCKED_DOOR_COLOR = new Color(Color.red);

    private final static Color STAIRS_COLOR = new Color(Color.cyan);

    private final static Color BLOCKED_COLOR = new Color(Color.gray);

    private final static int PIXEL_SIZE = 4;

    private Dungeon dungeon;

    private float[][] alphas;

    public MapDialog(GameView parent, Dungeon dungeon) throws Exception {
        super(parent);

        this.dungeon = dungeon;
        alphas = new float[dungeon.getHeight() / PIXEL_SIZE + 1][dungeon.getWidth() / PIXEL_SIZE + 1];
        for (int i = 0; i < dungeon.getHeight() / PIXEL_SIZE; i++) {
            for (int j = 0; j < dungeon.getWidth() / PIXEL_SIZE; j++) {
                alphas[i][j] = Math.min(1f, (float) (Math.random() + .9f)) - .4f;
            }
        }
    }

    @Override
    public void onShow() throws Exception {}

    @Override
    public void onHide() throws Exception {}

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_ESCAPE) || GUI.get().isKeyPressed(KEY_M)) {
            setVisible(false);
            GUI.get().clearKeysPressed();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int width = PARCHMENT.getImage().getWidth() * HEIGHT / PARCHMENT.getImage().getHeight();

        g.pushTransform();
        g.translate(x + (WIDTH - width) / 2, y);

        g.drawImage(PARCHMENT.getImage(), 0, 0, width, HEIGHT, 0, 0,
                PARCHMENT.getImage().getWidth(), PARCHMENT.getImage().getHeight());

        g.popTransform();

        int mapWidth = dungeon.getWidth() * PIXEL_SIZE;
        int mapHeight = dungeon.getHeight() * PIXEL_SIZE;

        g.pushTransform();
        g.translate(x + (WIDTH - mapWidth) / 2, y + (HEIGHT - mapHeight) / 2);

        for (int i = 0; i < dungeon.getHeight(); i++) {
            for (int j = 0; j < dungeon.getWidth(); j++) {
                Tile tile = dungeon.getDiscoveredTiles()[i][j];

                float alpha = alphas[i / PIXEL_SIZE][j / PIXEL_SIZE];
                g.setColor(new Color(0f, 0f, 0f, alpha));

                if (tile != null) {
                    switch (tile) {
                    case RUBBLE:
                        g.setColor(BLOCKED_COLOR);
                        break;
                    case UP_STAIR:
                    case DOWN_STAIR:
                        g.setColor(STAIRS_COLOR);
                        break;
                    case LIGHT_FLOOR:
                    case DARK_FLOOR:
                    case CORRIDOR_FLOOR:
                        g.setColor(TRANSPARENT_COLOR);
                        break;
                    default:
                        g.setColor(new Color(0f, 0f, 0f, alpha));
                        break;
                    }

                    Door door = dungeon.getDoorAt(new Point(j, i));
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

        g.popTransform();

        super.paint(g);
    }

}
