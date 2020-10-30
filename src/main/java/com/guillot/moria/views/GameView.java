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

    public final static int MAX_VIEW_DISTANCE = 10;

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

    private void compute(Tile[][] grid, Point position, int length) {
        if (length >= MAX_VIEW_DISTANCE) {
            return;
        }

        if (position.x >= 0 && position.y >= 0 && position.x < dungeon.getWidth() && position.y < dungeon.getHeight()) {
            Tile dungeonTile = dungeon.getFloor()[position.y][position.x];

            int gridY = position.y - player.getPosition().y + MAX_VIEW_DISTANCE;
            int gridX = position.x - player.getPosition().x + MAX_VIEW_DISTANCE;
            if (gridX < 0 || gridY < 0 || gridX >= MAX_VIEW_DISTANCE * 2 + 1 || gridY >= MAX_VIEW_DISTANCE * 2 + 1) {
                return;
            }

            grid[gridY][gridX] = dungeonTile;

            if (dungeonTile.isFloor || dungeonTile == PILLAR) {
                compute(grid, new Point(position.x - 1, position.y), length + 1);
                compute(grid, new Point(position.x + 1, position.y), length + 1);
                compute(grid, new Point(position.x, position.y - 1), length + 1);
                compute(grid, new Point(position.x, position.y + 1), length + 1);
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        image.clear();

        Tile[][] grid = new Tile[MAX_VIEW_DISTANCE * 2 + 1][MAX_VIEW_DISTANCE * 2 + 1];


        compute(grid, player.getPosition(), 0);

        for (int y = MAX_VIEW_DISTANCE * 2; y >= 0; y--) {
            for (int x = 0; x < MAX_VIEW_DISTANCE * 2 + 1; x++) {
                int i = player.getPosition().y + y - MAX_VIEW_DISTANCE;
                int j = player.getPosition().x + x - MAX_VIEW_DISTANCE;

                if (grid[y][x] != null) {
                    Tile tile = grid[y][x];

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
                    } else if (tile == OPEN_DOOR) {
                        alternate = true;
                    }

                    drawTile(tile, i, j, alternate);
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
