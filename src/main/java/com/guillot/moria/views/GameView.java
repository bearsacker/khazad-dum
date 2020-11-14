package com.guillot.moria.views;

import static com.guillot.moria.dungeon.Direction.EAST;
import static com.guillot.moria.dungeon.Direction.NORTH;
import static com.guillot.moria.dungeon.Direction.SOUTH;
import static com.guillot.moria.dungeon.Direction.WEST;
import static com.guillot.moria.dungeon.Tile.DOWN_STAIR;
import static com.guillot.moria.dungeon.Tile.PILLAR;
import static com.guillot.moria.dungeon.Tile.RUBBLE;
import static com.guillot.moria.dungeon.Tile.UP_STAIR;
import static com.guillot.moria.ressources.Images.CURSOR;
import static org.newdawn.slick.Input.KEY_C;
import static org.newdawn.slick.Input.KEY_I;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Human;
import com.guillot.moria.component.CharacterDialog;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.DoorDialog;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.dungeon.Direction;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class GameView extends View {

    public final static int SIZE = 64;

    private Dungeon dungeon;

    private AbstractCharacter player;

    private DepthBufferedImage image;

    private Point cursor;

    private Path path;

    private int currentStep;

    private long lastStep;

    // Components

    private InventoryDialog inventoryDialog;

    private CharacterDialog characterDialog;

    private DoorDialog doorDialog;

    private TextBox cursorTextBox;

    private Console console;

    @Override
    public void start() throws Exception {
        RNG.get().setSeed(1605372358709L);
        player = new Human("Jean");

        dungeon = new Dungeon(1);
        boolean eligible = false;
        while (!eligible) {
            eligible = dungeon.generate();
        }
        player.setPosition(dungeon.getSpawnUpStairs());

        dungeon.getItems().forEach(x -> player.pickUpItem(x));

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);

        inventoryDialog = new InventoryDialog(this, player);
        characterDialog = new CharacterDialog(this, player);

        doorDialog = new DoorDialog(this, player);

        console = new Console(EngineConfig.WIDTH, 5);
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        add(console, cursorTextBox, inventoryDialog, characterDialog, doorDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        cursorTextBox.setVisible(false);

        long time = System.currentTimeMillis();
        if (time - lastStep > 50) {
            if (path != null) {
                Point newPosition = path.getStep(currentStep).inverseXY();
                currentStep++;

                if (player.getPosition().x - newPosition.x == 1) {
                    player.setDirection(NORTH);
                } else if (player.getPosition().x - newPosition.x == -1) {
                    player.setDirection(SOUTH);
                } else if (player.getPosition().y - newPosition.y == 1) {
                    player.setDirection(WEST);
                } else if (player.getPosition().y - newPosition.y == -1) {
                    player.setDirection(EAST);
                }
                player.setPosition(newPosition);

                if (currentStep > player.getMovement() || currentStep >= path.getLength()) {
                    path = null;
                }
            } else {
                Tile tile = dungeon.getFloor()[player.getPosition().y][player.getPosition().x];
                if (tile == UP_STAIR) {
                    dungeon = new Dungeon(dungeon.getLevel() - 1);
                    dungeon.generate();
                    player.setPosition(dungeon.getSpawnDownStairs());
                    console.addMessage(player.getName() + " comes back to level " + dungeon.getLevel());
                } else if (tile == DOWN_STAIR) {
                    dungeon = new Dungeon(dungeon.getLevel() + 1);
                    dungeon.generate();
                    player.setPosition(dungeon.getSpawnUpStairs());
                    console.addMessage(player.getName() + " goes to level " + dungeon.getLevel());
                } else {
                    AbstractItem item = dungeon.getItemAt(player.getPosition());
                    if (item != null && player.pickUpItem(item)) {
                        dungeon.removeItem(item);
                        console.addMessage(player.getName() + " picked up " + item.getName());
                    }
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
                        switch (door.getState()) {
                        case LOCKED:
                            GUI.get().getInput().clearMousePressedRecord();
                            GUI.get().getInput().clearKeyPressedRecord();
                            doorDialog.setDoor(door);
                            doorDialog.setVisible(true);
                            break;
                        case SECRET:
                            door.setState(DoorState.OPEN);
                            console.addMessage(player.getName() + " discover a secret door !");
                        case OPEN:
                            player.setPosition(door.getDirectionPosition(player.getPosition()));
                            break;
                        case STUCK:
                            break;
                        }
                    } else {
                        path = dungeon.findPath(player.getPosition().inverseXY(), cursor);
                        currentStep = 0;
                    }
                }

                if (player.getPosition().is(cursor.inverseXY())) {
                    showTextBox(player.getName() + " - Level " + player.getLevel());
                } else {
                    AbstractItem item = dungeon.getItemAt(cursor.inverseXY());
                    if (item != null) {
                        showTextBox(item.getName());
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

    @Override
    public void paintComponents(Graphics g) throws Exception {
        g.setColor(Color.white);
        GUI.get().getFont().drawString(8, 8, "Level " + dungeon.getLevel());

        image.clear();

        Tile[][] grid = new Tile[dungeon.getHeight()][dungeon.getWidth()];
        computeViewedTiles(new HashMap<>(), grid, player.getPosition(), 0);

        for (int i = dungeon.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < dungeon.getWidth(); j++) {
                if (grid[i][j] != null) {
                    Tile tile = grid[i][j];
                    dungeon.getDiscoveredTiles()[i][j] = tile;

                    boolean alternate = false;

                    Door door = dungeon.getDoorAt(new Point(j, i));
                    if (grid[i][j].isWall && (door == null || door.getState() == DoorState.SECRET)) {
                        if (i + 1 < dungeon.getHeight() && grid[i + 1][j] != null
                                && (grid[i + 1][j].isFloor || grid[i + 1][j].isStairs
                                        || dungeon.getDoorAt(new Point(j, i + 1)) != null)) {
                            alternate = true;
                        } else if (j - 1 >= 0 && grid[i][j - 1] != null
                                && (grid[i][j - 1].isFloor || grid[i][j - 1].isStairs
                                        || dungeon.getDoorAt(new Point(j - 1, i)) != null)) {
                            alternate = true;
                        } else if (i + 1 < dungeon.getHeight() && j - 1 >= 0 && grid[i + 1][j - 1] != null
                                && (grid[i + 1][j - 1].isFloor || grid[i + 1][j - 1].isStairs
                                        || dungeon.getDoorAt(new Point(j - 1, i + 1)) != null)) {
                            alternate = true;
                        }
                    }

                    drawTile(tile, i, j, alternate, door);

                    AbstractItem item = dungeon.getItemAt(new Point(j, i));
                    if (item != null) {
                        item.draw(image, player.getPosition());
                    }

                    if (player.getPosition().is(j, i)) {
                        player.draw(image);
                    }
                } else if (dungeon.getDiscoveredTiles()[i][j] != null) {
                    Door door = dungeon.getDoorAt(new Point(j, i));
                    drawTile(dungeon.getDiscoveredTiles()[i][j], i, j, door);
                }
            }
        }

        g.drawImage(image.getImage(), 0, 0);

        if (cursor != null) {
            drawCursor(g, cursor.x, cursor.y, dungeon.getFloor()[cursor.x][cursor.y]);
        }

        super.paintComponents(g);
    }

    private void computeViewedTiles(HashMap<Point, Integer> depthList, Tile[][] grid, Point position, int length) {
        if (length >= player.getLightRadius()) {
            return;
        }

        Integer depthValue = depthList.get(position);
        if (depthValue != null && depthValue < length) {
            return;
        }

        depthList.put(position, length);

        if (position.x >= 0 && position.y >= 0 && position.x < dungeon.getWidth() && position.y < dungeon.getHeight()) {
            Tile dungeonTile = dungeon.getFloor()[position.y][position.x];
            grid[position.y][position.x] = dungeonTile;

            if (dungeonTile.isFloor || dungeonTile == RUBBLE || dungeonTile == PILLAR || dungeonTile.isStairs) {
                computeViewedTiles(depthList, grid, new Point(position.x - 1, position.y), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x + 1, position.y), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y - 1), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y + 1), length + 1);
            }
        }
    }

    private void drawTile(Tile tile, int px, int py, Door door) {
        int x = (px - player.getPosition().y) * 32 + (py - player.getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - player.getPosition().x) * 16 - (px - player.getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        if (tile.image != null) {
            image.drawImage(tile.image.getSubImage(0, 0, 64, 96), x, y, Color.gray);

            if (door != null && door.getState() != DoorState.SECRET) {
                if (door.getDirection() == Direction.NORTH) {
                    image.drawImage(Images.DOOR.getSubImage(1, 0), x, y, Color.gray);
                } else if (door.getDirection() == Direction.WEST) {
                    image.drawImage(Images.DOOR.getSubImage(0, 0), x, y, Color.gray);
                }
            }
        }
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

                showTextBox(door.toString());
            } else if (tile.isStairs) {
                if (tile == UP_STAIR) {
                    g.drawImage(CURSOR.getSubImage(3, 0), x, y);
                    showTextBox("Back to level " + (dungeon.getLevel() - 1));
                } else {
                    g.drawImage(CURSOR.getSubImage(5, 0), x, y);
                    showTextBox("Go to level " + (dungeon.getLevel() + 1));
                }
            }
        }
    }

    private void showTextBox(String text) {
        cursorTextBox.setText(text);
        cursorTextBox.setX(GUI.get().getMouseX() + 16);
        cursorTextBox.setY(GUI.get().getMouseY() - cursorTextBox.getHeight() - 16);
        cursorTextBox.setVisible(true);
    }

    public Console getConsole() {
        return console;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public static void main(String[] args) throws SlickException {
        new Game("Khazad-dûm", new GameView());
    }

}
