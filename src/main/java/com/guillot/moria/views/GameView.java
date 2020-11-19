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
import static org.newdawn.slick.Input.KEY_M;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.ProgressBar;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Monster;
import com.guillot.moria.component.CharacterDialog;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.DoorDialog;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.component.MapDialog;
import com.guillot.moria.dungeon.Direction;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;

public class GameView extends View {

    public final static int SIZE = 64;

    private GameState game;

    private DepthBufferedImage image;

    private Point cursor;

    private Path path;

    private int currentStep;

    private long lastStep;

    // Components

    private InventoryDialog inventoryDialog;

    private CharacterDialog characterDialog;

    private MapDialog mapDialog;

    private DoorDialog doorDialog;

    private TextBox cursorTextBox;

    private Console console;

    private ProgressBar lifeProgressBar;

    @Override
    public void start() throws Exception {
        game = new GameState();
        game.init();

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);

        inventoryDialog = new InventoryDialog(this, getPlayer());
        characterDialog = new CharacterDialog(this, getPlayer());

        doorDialog = new DoorDialog(this, getPlayer());

        mapDialog = new MapDialog(this, game);

        console = new Console(EngineConfig.WIDTH, 5);
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        lifeProgressBar = new ProgressBar(EngineConfig.WIDTH / 2 - 128, EngineConfig.HEIGHT - 32, 256, 32, 100);

        add(console, cursorTextBox, lifeProgressBar, inventoryDialog, characterDialog, doorDialog, mapDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        cursorTextBox.setVisible(false);
        cursor = null;

        long time = System.currentTimeMillis();
        if (time - lastStep > 50) {
            if (path != null) {
                Point newPosition = path.getStep(currentStep).inverseXY();
                currentStep++;

                if (getPlayer().getPosition().x - newPosition.x == 1) {
                    getPlayer().setDirection(NORTH);
                } else if (getPlayer().getPosition().x - newPosition.x == -1) {
                    getPlayer().setDirection(SOUTH);
                } else if (getPlayer().getPosition().y - newPosition.y == 1) {
                    getPlayer().setDirection(WEST);
                } else if (getPlayer().getPosition().y - newPosition.y == -1) {
                    getPlayer().setDirection(EAST);
                }
                getPlayer().setPosition(newPosition);

                if (currentStep > getPlayer().getMovement() || currentStep >= path.getLength()) {
                    path = null;
                }
            } else {
                Tile tile = getDungeon().getFloor()[getPlayer().getPosition().y][getPlayer().getPosition().x];
                if (tile == UP_STAIR) {
                    game.toGoUpstairs();
                    console.addMessage(getPlayer().getName() + " comes back to level " + getDungeon().getLevel());
                } else if (tile == DOWN_STAIR) {
                    game.toGoDownstairs();
                    console.addMessage(getPlayer().getName() + " goes to level " + getDungeon().getLevel());
                } else {
                    AbstractItem item = getDungeon().getItemAt(getPlayer().getPosition());
                    if (item != null && getPlayer().pickUpItem(item)) {
                        getDungeon().removeItem(item);
                        console.addMessage(getPlayer().getName() + " picked up " + item.getName());
                    }
                }
            }

            lastStep = time;
        }
        if (isFocused()) {
            Object cursorObject = image.getDepth(GUI.get().getMouseX(), GUI.get().getMouseY());
            if (cursorObject != null) {
                if (cursorObject instanceof Point) {
                    cursor = (Point) cursorObject;
                } else if (cursorObject instanceof AbstractCharacter) {
                    showTextBox(((AbstractCharacter) cursorObject).toString());
                    cursor = ((AbstractCharacter) cursorObject).getPosition().inverseXY();
                }
            }

            if (cursor != null) {
                if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                    Door door = getDungeon().getDoorAt(cursor.inverseXY());
                    if (door != null && getPlayer().getPosition().inverseXY().distanceFrom(cursor) == 1) {
                        switch (door.getState()) {
                        case LOCKED:
                            GUI.get().getInput().clearMousePressedRecord();
                            GUI.get().getInput().clearKeyPressedRecord();
                            doorDialog.setDoor(door);
                            doorDialog.setVisible(true);
                            break;
                        case SECRET:
                            door.setState(DoorState.OPEN);
                            console.addMessage(getPlayer().getName() + " discover a secret door !");
                        case OPEN:
                            getPlayer().setPosition(door.getDirectionPosition(getPlayer().getPosition()));
                            break;
                        case STUCK:
                            break;
                        }
                    } else {
                        path = getDungeon().findPath(getPlayer().getPosition().inverseXY(), cursor, getPlayer().getLightRadius());
                        currentStep = 0;
                    }
                }

                AbstractItem item = getDungeon().getItemAt(cursor.inverseXY());
                if (item != null) {
                    showTextBox(item.getName());
                }
            }

            if (GUI.get().isKeyPressed(KEY_I)) {
                inventoryDialog.setVisible(true);
            }

            if (GUI.get().isKeyPressed(KEY_C)) {
                characterDialog.setVisible(true);
            }

            if (GUI.get().isKeyPressed(KEY_M)) {
                mapDialog.setVisible(true);
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        image.clear();

        Tile[][] grid = new Tile[getDungeon().getHeight()][getDungeon().getWidth()];
        computeViewedTiles(new HashMap<>(), grid, getPlayer().getPosition(), 0);

        for (int i = getDungeon().getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < getDungeon().getWidth(); j++) {
                if (grid[i][j] != null) {
                    Tile tile = grid[i][j];
                    getDungeon().getDiscoveredTiles()[i][j] = tile;

                    boolean alternate = false;

                    Door door = getDungeon().getDoorAt(new Point(j, i));
                    if (grid[i][j].isWall && (door == null || door.getState() == DoorState.SECRET)) {
                        if (i + 1 < getDungeon().getHeight() && grid[i + 1][j] != null
                                && (grid[i + 1][j].isFloor || grid[i + 1][j].isStairs
                                        || getDungeon().getDoorAt(new Point(j, i + 1)) != null)) {
                            alternate = true;
                        } else if (j - 1 >= 0 && grid[i][j - 1] != null
                                && (grid[i][j - 1].isFloor || grid[i][j - 1].isStairs
                                        || getDungeon().getDoorAt(new Point(j - 1, i)) != null)) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && grid[i + 1][j - 1] != null
                                && (grid[i + 1][j - 1].isFloor || grid[i + 1][j - 1].isStairs
                                        || getDungeon().getDoorAt(new Point(j - 1, i + 1)) != null)) {
                            alternate = true;
                        }
                    }

                    drawTile(tile, i, j, alternate, door);

                    AbstractItem item = getDungeon().getItemAt(new Point(j, i));
                    if (item != null) {
                        item.draw(image, getPlayer().getPosition());
                    }

                    if (getPlayer().getPosition().is(j, i)) {
                        getPlayer().draw(image, getPlayer().getPosition());
                    }

                    Monster monster = getDungeon().getMonsterAt(new Point(j, i));
                    if (monster != null) {
                        monster.draw(image, getPlayer().getPosition());

                    }
                } else if (getDungeon().getDiscoveredTiles()[i][j] != null) {
                    boolean alternate = false;

                    Door door = getDungeon().getDoorAt(new Point(j, i));
                    if (getDungeon().getDiscoveredTiles()[i][j].isWall && (door == null || door.getState() == DoorState.SECRET)) {
                        if (i + 1 < getDungeon().getHeight() && getDungeon().getDiscoveredTiles()[i + 1][j] != null
                                && (getDungeon().getDiscoveredTiles()[i + 1][j].isFloor
                                        || getDungeon().getDiscoveredTiles()[i + 1][j].isStairs
                                        || getDungeon().getDoorAt(new Point(j, i + 1)) != null)) {
                            alternate = true;
                        } else if (j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i][j - 1].isFloor
                                        || getDungeon().getDiscoveredTiles()[i][j - 1].isStairs
                                        || getDungeon().getDoorAt(new Point(j - 1, i)) != null)) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i + 1][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i + 1][j - 1].isFloor
                                        || getDungeon().getDiscoveredTiles()[i + 1][j - 1].isStairs
                                        || getDungeon().getDoorAt(new Point(j - 1, i + 1)) != null)) {
                            alternate = true;
                        }
                    }

                    drawShadowedTile(getDungeon().getDiscoveredTiles()[i][j], i, j, alternate, door);
                }
            }
        }

        g.drawImage(image.getImage(), 0, 0);

        if (cursor != null) {
            drawCursor(g, cursor.x, cursor.y, getDungeon().getFloor()[cursor.x][cursor.y]);
        }

        super.paintComponents(g);
    }

    private void computeViewedTiles(HashMap<Point, Integer> depthList, Tile[][] grid, Point position, int length) {
        if (length >= getPlayer().getLightRadius()) {
            return;
        }

        Integer depthValue = depthList.get(position);
        if (depthValue != null && depthValue < length) {
            return;
        }

        depthList.put(position, length);

        if (position.x >= 0 && position.y >= 0 && position.x < getDungeon().getWidth() && position.y < getDungeon().getHeight()) {
            Tile dungeonTile = getDungeon().getFloor()[position.y][position.x];
            grid[position.y][position.x] = dungeonTile;

            if (dungeonTile.isFloor || dungeonTile == RUBBLE || dungeonTile == PILLAR || dungeonTile.isStairs) {
                computeViewedTiles(depthList, grid, new Point(position.x - 1, position.y), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x + 1, position.y), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y - 1), length + 1);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y + 1), length + 1);
            }
        }
    }

    private void drawShadowedTile(Tile tile, int px, int py, boolean alternate, Door door) {
        int x = (px - getPlayer().getPosition().y) * 32 + (py - getPlayer().getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - getPlayer().getPosition().x) * 16 - (px - getPlayer().getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

        if (tile.image != null) {
            if (alternate) {
                image.drawImage(tile.image.getSubImage(64, 0, 64, 96), x, y, Color.gray);
            } else {
                image.drawImage(tile.image.getSubImage(0, 0, 64, 96), x, y, Color.gray);
            }

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
        int x = (px - getPlayer().getPosition().y) * 32 + (py - getPlayer().getPosition().x) * 32 + (int) image.getCenterOfRotationX() - 32;
        int y = (py - getPlayer().getPosition().x) * 16 - (px - getPlayer().getPosition().y) * 16 + (int) image.getCenterOfRotationY() - 48;

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
            int x = (px - getPlayer().getPosition().y) * 32 + (py - getPlayer().getPosition().x) * 32 + (int) image.getCenterOfRotationX()
                    - 32;
            int y = (py - getPlayer().getPosition().x) * 16 - (px - getPlayer().getPosition().y) * 16 + (int) image.getCenterOfRotationY()
                    - 48;

            Door door = getDungeon().getDoorAt(new Point(py, px));
            if (tile.isFloor) {
                g.drawImage(CURSOR.getSubImage(4, 0), x, y);
            } else if (door != null && door.getState() != DoorState.SECRET) {
                g.drawImage(CURSOR.getSubImage(3, 0), x, y);

                showTextBox(door.toString());
            } else if (tile.isStairs) {
                if (tile == UP_STAIR) {
                    g.drawImage(CURSOR.getSubImage(3, 0), x, y);
                    showTextBox("Back to level " + (getDungeon().getLevel() - 1));
                } else {
                    g.drawImage(CURSOR.getSubImage(5, 0), x, y);
                    showTextBox("Go to level " + (getDungeon().getLevel() + 1));
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
        return game.getDungeon();
    }

    public AbstractCharacter getPlayer() {
        return game.getPlayer();
    }

}
