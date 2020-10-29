package com.guillot.moria.views;

import static com.guillot.moria.Images.HUMAN_WARRIOR;
import static com.guillot.moria.dungeon.Tile.OPEN_DOOR;
import static com.guillot.moria.dungeon.Tile.PILLAR;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.View;
import com.guillot.moria.Player;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class GameView extends View {

    public final static int MAX_VIEW_DISTANCE = 15;

    public final static int SIZE = 64;

    private Dungeon dungeon;

    private Player player;

    private DepthBufferedImage image;

    @Override
    public void start() throws Exception {

        RNG.get().setSeed(1603923549811L);

        dungeon = new Dungeon(300);
        dungeon.generate();

        player = new Player();
        player.setPosition(dungeon.getPlayerSpawn());

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (isFocused()) {
            if (GUI.get().isKeyPressed(Input.KEY_LEFT)) {
                player.getPosition().decrementX();
            }

            if (GUI.get().isKeyPressed(Input.KEY_RIGHT)) {
                player.getPosition().incrementX();
            }

            if (GUI.get().isKeyPressed(Input.KEY_UP)) {
                player.getPosition().incrementY();
            }

            if (GUI.get().isKeyPressed(Input.KEY_DOWN)) {
                player.getPosition().decrementY();
            }
        }
    }

    private void discoverUp(Tile[][] grid, int tx, int ty, boolean stop) {
        int y = player.getPosition().y + ty - MAX_VIEW_DISTANCE;
        int x = player.getPosition().x + tx - MAX_VIEW_DISTANCE;

        if (tx < 0 || ty < 0 || tx >= MAX_VIEW_DISTANCE * 2 + 1 || ty >= MAX_VIEW_DISTANCE * 2 + 1) {
            return;
        }

        if (x >= 0 && y >= 0 && y < dungeon.getHeight() && x < dungeon.getWidth()) {
            grid[ty][tx] = dungeon.getFloor()[y][x];

            if (stop) {
                return;
            }

            if (dungeon.getFloor()[y][x].isFloor || dungeon.getFloor()[y][x] == PILLAR) {
                boolean willStop =
                        y + 1 < dungeon.getHeight() && (dungeon.getFloor()[y + 1][x].isFloor || dungeon.getFloor()[y + 1][x] == PILLAR);
                discoverUp(grid, tx - 1, ty + 1, !willStop);
                discoverUp(grid, tx + 1, ty + 1, !willStop);

                discoverUp(grid, tx, ty + 1, false);
            }
        }
    }

    private void discoverDown(Tile[][] grid, int tx, int ty, boolean stop) {
        int y = player.getPosition().y + ty - MAX_VIEW_DISTANCE;
        int x = player.getPosition().x + tx - MAX_VIEW_DISTANCE;

        if (tx < 0 || ty < 0 || tx >= MAX_VIEW_DISTANCE * 2 + 1 || ty >= MAX_VIEW_DISTANCE * 2 + 1) {
            return;
        }

        if (x >= 0 && y >= 0 && y < dungeon.getHeight() && x < dungeon.getWidth()) {
            grid[ty][tx] = dungeon.getFloor()[y][x];

            if (stop) {
                return;
            }

            if (dungeon.getFloor()[y][x].isFloor || dungeon.getFloor()[y][x] == PILLAR) {
                boolean willStop = y - 1 >= 0 && (dungeon.getFloor()[y - 1][x].isFloor || dungeon.getFloor()[y - 1][x] == PILLAR);
                discoverDown(grid, tx - 1, ty - 1, !willStop);
                discoverDown(grid, tx + 1, ty - 1, !willStop);

                discoverDown(grid, tx, ty - 1, false);
            }
        }
    }

    private void discoverLeft(Tile[][] grid, int tx, int ty, boolean stop) {
        int y = player.getPosition().y + ty - MAX_VIEW_DISTANCE;
        int x = player.getPosition().x + tx - MAX_VIEW_DISTANCE;

        if (tx < 0 || ty < 0 || tx >= MAX_VIEW_DISTANCE * 2 + 1 || ty >= MAX_VIEW_DISTANCE * 2 + 1) {
            return;
        }

        if (x >= 0 && y >= 0 && y < dungeon.getHeight() && x < dungeon.getWidth()) {
            grid[ty][tx] = dungeon.getFloor()[y][x];

            if (stop) {
                return;
            }

            if (dungeon.getFloor()[y][x].isFloor || dungeon.getFloor()[y][x] == PILLAR) {
                boolean willStop = x - 1 >= 0 && (dungeon.getFloor()[y][x - 1].isFloor || dungeon.getFloor()[y][x - 1] == PILLAR);
                discoverLeft(grid, tx - 1, ty - 1, !willStop);
                discoverLeft(grid, tx - 1, ty + 1, !willStop);

                discoverLeft(grid, tx - 1, ty, false);
            }
        }
    }

    private void discoverRight(Tile[][] grid, int tx, int ty, boolean stop) {
        int y = player.getPosition().y + ty - MAX_VIEW_DISTANCE;
        int x = player.getPosition().x + tx - MAX_VIEW_DISTANCE;

        if (tx < 0 || ty < 0 || tx >= MAX_VIEW_DISTANCE * 2 + 1 || ty >= MAX_VIEW_DISTANCE * 2 + 1) {
            return;
        }

        if (x >= 0 && y >= 0 && y < dungeon.getHeight() && x < dungeon.getWidth()) {
            grid[ty][tx] = dungeon.getFloor()[y][x];

            if (stop) {
                return;
            }

            if (dungeon.getFloor()[y][x].isFloor || dungeon.getFloor()[y][x] == PILLAR) {
                boolean willStop =
                        x + 1 < dungeon.getWidth() && (dungeon.getFloor()[y][x + 1].isFloor || dungeon.getFloor()[y][x + 1] == PILLAR);
                discoverRight(grid, tx + 1, ty - 1, !willStop);
                discoverRight(grid, tx + 1, ty + 1, !willStop);

                discoverRight(grid, tx + 1, ty, false);
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        image.clear();

        Tile[][] grid = new Tile[MAX_VIEW_DISTANCE * 2 + 1][MAX_VIEW_DISTANCE * 2 + 1];
        discoverUp(grid, MAX_VIEW_DISTANCE, MAX_VIEW_DISTANCE, false);
        discoverDown(grid, MAX_VIEW_DISTANCE, MAX_VIEW_DISTANCE, false);
        discoverLeft(grid, MAX_VIEW_DISTANCE, MAX_VIEW_DISTANCE, false);
        discoverRight(grid, MAX_VIEW_DISTANCE, MAX_VIEW_DISTANCE, false);

        for (int y = MAX_VIEW_DISTANCE * 2; y >= 0; y--) {
            for (int x = 0; x < MAX_VIEW_DISTANCE * 2 + 1; x++) {
                int i = player.getPosition().y + y - MAX_VIEW_DISTANCE;
                int j = player.getPosition().x + x - MAX_VIEW_DISTANCE;

                if (grid[y][x] != null) {
                    boolean alternate = false;

                    if (grid[y][x].isWall) {
                        if (y + 1 < MAX_VIEW_DISTANCE * 2 + 1 && grid[y + 1][x] != null
                                && (grid[y + 1][x].isFloor || grid[y + 1][x].isDoor)) {
                            alternate = true;
                        } else if (x - 1 >= 0 && grid[y][x - 1] != null && (grid[y][x - 1].isFloor || grid[y][x - 1].isDoor)) {
                            alternate = true;
                        } else if (y + 1 < MAX_VIEW_DISTANCE * 2 + 1 && x - 1 >= 0
                                && grid[y + 1][x - 1] != null && (grid[y + 1][x - 1].isFloor || grid[y + 1][x - 1].isDoor)) {
                            alternate = true;
                        }
                    } else if (grid[y][x] == OPEN_DOOR) {
                        alternate = true;
                    }

                    drawTile(grid[y][x], i, j, alternate);
                }

                if (x == MAX_VIEW_DISTANCE && y == MAX_VIEW_DISTANCE) {
                    image.drawImage(HUMAN_WARRIOR.getImage(), image.getCenterOfRotationX() - 32, image.getCenterOfRotationY() - 48);
                }
            }
        }

        g.drawImage(image.getImage(), 0, 0);

        super.paintComponents(g);
    }

    private void drawTile(Tile tile, int px, int py, boolean alternate) {
        int x = (int) ((px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + image.getCenterOfRotationX() - 32);
        int y = (int) ((py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + image.getCenterOfRotationY() - 48);

        if (tile.image != null) {
            image.drawImage(new Point(px, py), tile.image, x, y, alternate);
        }
    }

    public static void main(String[] args) throws SlickException {
        new Game("Moria", new GameView());
    }

}
