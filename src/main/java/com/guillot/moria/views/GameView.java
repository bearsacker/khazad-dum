package com.guillot.moria.views;

import static com.guillot.moria.Images.CURSOR;
import static com.guillot.moria.dungeon.Tile.PILLAR;
import static org.newdawn.slick.Input.KEY_C;
import static org.newdawn.slick.Input.KEY_I;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.moria.Images;
import com.guillot.moria.ai.AStar;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Human;
import com.guillot.moria.component.CharacterDialog;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.dungeon.Direction;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class GameView extends View {

    public final static int SIZE = 64;

    private Dungeon dungeon;

    private AbstractCharacter player;

    private DepthBufferedImage image;

    private boolean needRepaint;

    private Point cursor;

    private AStar astar;

    private Path path;

    private int currentStep;

    private long lastStep;

    // Components

    private InventoryDialog inventoryDialog;

    private CharacterDialog characterDialog;

    private TextBox cursorTextBox;

    private Console console;

    @Override
    public void start() throws Exception {
        RNG.get().setSeed(1604912991583L);
        player = new Human("Jean Castex");

        dungeon = new Dungeon(player, 25);
        dungeon.generate();

        astar = new AStar(dungeon, 100);

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);
        needRepaint = true;

        inventoryDialog = new InventoryDialog(this, player);
        characterDialog = new CharacterDialog(this, player);

        console = new Console(EngineConfig.WIDTH, 5);
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        add(console, cursorTextBox, inventoryDialog, characterDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        cursorTextBox.setVisible(false);

        long time = System.currentTimeMillis();
        if (time - lastStep > 100) {
            if (path != null) {
                player.setPosition(path.getStep(currentStep).inverseXY());
                currentStep++;
                needRepaint = true;

                if (currentStep > player.getMovement() || currentStep >= path.getLength()) {
                    path = null;
                }
            } else {
                AbstractItem item = dungeon.getItemAt(player.getPosition());
                if (item != null && player.pickUpItem(item)) {
                    dungeon.removeItem(item);
                    console.addMessage(player.getName() + " picked up " + item.getName());
                    needRepaint = true;
                }
            }

            lastStep = time;
        }

        if (isFocused()) {
            cursor = image.getDepth(GUI.get().getMouseX(), GUI.get().getMouseY());

            if (cursor != null) {
                if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                    Door door = dungeon.getDoorAt(cursor.inverseXY());
                    if (door != null && player.getPosition().inverseXY().distanceFrom(cursor) == 1) {
                        if (door.getState() == DoorState.SECRET) {
                            door.setState(DoorState.OPEN);
                            console.addMessage(player.getName() + " discover a secret door !");
                        }

                        player.setPosition(door.getDirectionPosition(player.getPosition()));
                        needRepaint = true;
                    } else {
                        path = astar.findPath(player.getPosition().inverseXY(), cursor, false);
                        currentStep = 0;
                    }
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

            if (GUI.get().isKeyPressed(KEY_C)) {
                characterDialog.setVisible(true);
            }
        }
    }

    private void compute(Tile[][] grid, Point position, int length) {
        if (length >= player.getLightRadius()) {
            return;
        }

        if (position.x >= 0 && position.y >= 0 && position.x < dungeon.getWidth() && position.y < dungeon.getHeight()) {
            Tile dungeonTile = dungeon.getFloor()[position.y][position.x];

            int gridY = position.y - player.getPosition().y + player.getLightRadius();
            int gridX = position.x - player.getPosition().x + player.getLightRadius();
            if (gridX < 0 || gridY < 0 || gridX >= player.getLightRadius() * 2 + 1 || gridY >= player.getLightRadius() * 2 + 1) {
                return;
            }

            grid[gridY][gridX] = dungeonTile;

            if (dungeonTile.isFloor || dungeonTile == PILLAR || dungeonTile.isStairs) {
                compute(grid, new Point(position.x - 1, position.y), length + 1);
                compute(grid, new Point(position.x + 1, position.y), length + 1);
                compute(grid, new Point(position.x, position.y - 1), length + 1);
                compute(grid, new Point(position.x, position.y + 1), length + 1);
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        g.setColor(Color.white);
        GUI.get().getFont().drawString(8, 8, "Level " + dungeon.getLevel());

        if (needRepaint) {
            image.clear();

            Tile[][] grid = new Tile[player.getLightRadius() * 2 + 1][player.getLightRadius() * 2 + 1];
            compute(grid, player.getPosition(), 0);

            for (int y = player.getLightRadius() * 2; y >= 0; y--) {
                for (int x = 0; x < player.getLightRadius() * 2 + 1; x++) {
                    int i = player.getPosition().y + y - player.getLightRadius();
                    int j = player.getPosition().x + x - player.getLightRadius();

                    if (grid[y][x] != null) {
                        Tile tile = grid[y][x];

                        boolean alternate = false;

                        Door door = dungeon.getDoorAt(new Point(j, i));
                        if (grid[y][x].isWall && (door == null || door.getState() == DoorState.SECRET)) {
                            if (y + 1 < player.getLightRadius() * 2 + 1 && grid[y + 1][x] != null
                                    && (grid[y + 1][x].isFloor || dungeon.getDoorAt(new Point(x, y + 1)) != null)) {
                                alternate = true;
                            } else if (x - 1 >= 0 && grid[y][x - 1] != null
                                    && (grid[y][x - 1].isFloor || dungeon.getDoorAt(new Point(x - 1, y)) != null)) {
                                alternate = true;
                            } else if (y + 1 < player.getLightRadius() * 2 + 1 && x - 1 >= 0
                                    && grid[y + 1][x - 1] != null
                                    && (grid[y + 1][x - 1].isFloor || dungeon.getDoorAt(new Point(x - 1, y + 1)) != null)) {
                                alternate = true;
                            }
                        }

                        drawTile(tile, i, j, alternate, door);

                        AbstractItem item = dungeon.getItemAt(new Point(j, i));
                        if (item != null) {
                            item.draw(image, player.getPosition());
                        }

                        if (x == player.getLightRadius() && y == player.getLightRadius()) {
                            player.draw(image);
                        }
                    }
                }
            }

            needRepaint = false;
        }

        g.drawImage(image.getImage(), 0, 0);

        if (cursor != null) {
            drawCursor(g, cursor.x, cursor.y, dungeon.getFloor()[cursor.x][cursor.y]);
        }

        super.paintComponents(g);
    }

    private void drawTile(Tile tile, int px, int py, boolean alternate, Door door) {
        int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        if (tile.image != null) {
            if (alternate) {
                image.drawImage(new Point(px, py), tile.image.getSubImage(64, 0, 64, 96), x, y);
            } else {
                image.drawImage(new Point(px, py), tile.image.getSubImage(0, 0, 64, 96), x, y);
            }

            if (door != null && door.getState() != DoorState.SECRET) {
                if (door.getDirection() == Direction.NORTH) {
                    image.drawImage(new Point(px, py), Images.DOOR.getSubImage(1, 0), x, y);
                } else if (door.getDirection() == Direction.WEST) {
                    image.drawImage(new Point(px, py), Images.DOOR.getSubImage(0, 0), x, y);
                }
            }
        }
    }

    private void drawCursor(Graphics g, int px, int py, Tile tile) {
        if (tile != null) {
            int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
            int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

            Door door = dungeon.getDoorAt(new Point(py, px));
            if (tile.isFloor) {
                g.drawImage(CURSOR.getSubImage(4, 0), x, y);
            } else if (door != null && door.getState() != DoorState.SECRET) {
                g.drawImage(CURSOR.getSubImage(3, 0), x, y);
                cursorTextBox.setText(door.toString());
                cursorTextBox.setX(GUI.get().getMouseX());
                cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight());
                cursorTextBox.setVisible(true);
            } else if (tile.isStairs) {
                g.drawImage(CURSOR.getSubImage(3, 0), x, y);
            }
        }
    }

    public Console getConsole() {
        return console;
    }

    public static void main(String[] args) throws SlickException {
        new Game("Khazad-dûm", new GameView());
    }

}
