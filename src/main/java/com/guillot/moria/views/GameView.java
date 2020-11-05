package com.guillot.moria.views;

import static com.guillot.moria.Images.CURSOR;
import static com.guillot.moria.dungeon.Tile.DOWN_STAIR;
import static com.guillot.moria.dungeon.Tile.OPEN_DOOR;
import static com.guillot.moria.dungeon.Tile.PILLAR;
import static com.guillot.moria.dungeon.Tile.UP_STAIR;
import static org.newdawn.slick.Input.KEY_I;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.moria.ai.AStar;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Human;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class GameView extends View {

    public final static int MAX_VIEW_DISTANCE = 10;

    public final static int SIZE = 64;

    private Dungeon dungeon;

    private AbstractCharacter player;

    private DepthBufferedImage image;

    private Point cursor;

    private AStar astar;

    private Path path;

    private int currentStep;

    private long lastStep;

    // Components

    private InventoryDialog inventoryDialog;

    private TextBox cursorTextBox;

    private Console console;

    @Override
    public void start() throws Exception {
        RNG.get().setSeed(1603923549811L);

        player = new Human("Jean Castex");
        System.out.println(player);

        dungeon = new Dungeon(player, 300);
        dungeon.generate();

        dungeon.getItems().forEach(x -> {
            System.out.println(x);
            player.getInventory().add(x);
        });

        astar = new AStar(dungeon, 100);

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);

        inventoryDialog = new InventoryDialog(this, player);

        console = new Console(EngineConfig.WIDTH, 5);
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        add(console, cursorTextBox, inventoryDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        cursorTextBox.setVisible(false);

        long time = System.currentTimeMillis();
        if (time - lastStep > 50) {
            if (path != null) {
                player.setPosition(path.getStep(currentStep).inverseXY());
                currentStep++;

                if (currentStep >= path.getLength()) {
                    path = null;
                }
            } else {
                AbstractItem item = dungeon.getItemAt(player.getPosition());
                if (item != null && player.pickUpItem(item)) {
                    dungeon.removeItem(item);
                    console.addMessage(player.getName() + " picked up " + item.getName());
                }
            }

            lastStep = time;
        }

        if (isFocused()) {
            cursor = image.getDepth(GUI.get().getMouseX(), GUI.get().getMouseY());

            if (cursor != null) {
                if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                    path = astar.findPath(player.getPosition().inverseXY(), cursor, false);
                    currentStep = 0;
                }

                if (player.getPosition().is(cursor.inverseXY())) {
                    cursorTextBox.setText(player.getName() + " - Level " + player.getLevel());
                    cursorTextBox.setX(GUI.get().getMouseX());
                    cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight());
                    cursorTextBox.setVisible(true);
                } else {
                    AbstractItem item = dungeon.getItemAt(cursor.inverseXY());
                    if (item != null) {
                        cursorTextBox.setText(item.getName());
                        cursorTextBox.setX(GUI.get().getMouseX());
                        cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight());
                        cursorTextBox.setVisible(true);
                    }
                }
            }

            if (GUI.get().isKeyPressed(KEY_I)) {
                inventoryDialog.setVisible(true);
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

            if (dungeonTile.isFloor || dungeonTile == PILLAR || dungeonTile == DOWN_STAIR || dungeonTile == UP_STAIR) {
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

                    if (path != null) {
                        for (int k = currentStep; k < path.getLength(); k++) {
                            Point step = path.getStep(k);

                            if (step.y - player.getPosition().x + MAX_VIEW_DISTANCE == x
                                    && step.x - player.getPosition().y + MAX_VIEW_DISTANCE == y) {
                                drawPathTile(step.x, step.y);
                            }
                        }
                    }

                    if (cursor != null) {
                        if (cursor.y - player.getPosition().x + MAX_VIEW_DISTANCE == x
                                && cursor.x - player.getPosition().y + MAX_VIEW_DISTANCE == y) {
                            drawCursor(cursor.x, cursor.y, grid[y][x]);
                        }
                    }

                    AbstractItem item = dungeon.getItemAt(new Point(j, i));
                    if (item != null) {
                        item.draw(image, player.getPosition());
                    }

                    if (x == MAX_VIEW_DISTANCE && y == MAX_VIEW_DISTANCE) {
                        player.draw(image);
                    }
                }
            }
        }

        g.drawImage(image.getImage(), 0, 0);

        super.paintComponents(g);
    }

    private void drawTile(Tile tile, int px, int py, boolean alternate) {
        int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        if (tile.image != null) {
            if (alternate) {
                image.drawImage(new Point(px, py), tile.image.getSubImage(64, 0, 64, 96), x, y);
            } else {
                image.drawImage(new Point(px, py), tile.image.getSubImage(0, 0, 64, 96), x, y);
            }
        }
    }

    private void drawCursor(int px, int py, Tile tile) {
        int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        if (tile != null && tile.isFloor) {
            image.drawImage(CURSOR.getSubImage(1, 0), x, y);
        }
    }

    private void drawPathTile(int px, int py) {
        int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        image.drawImage(CURSOR.getSubImage(0, 0), x, y);
    }

    public Console getConsole() {
        return console;
    }

    public static void main(String[] args) throws SlickException {
        new Game("Moria", new GameView());
    }

}
