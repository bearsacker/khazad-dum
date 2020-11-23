package com.guillot.moria.views;

import static com.guillot.moria.configs.LevelingConfig.LEVELING_LEVELS;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.ProgressBar;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Attack;
import com.guillot.moria.character.Monster;
import com.guillot.moria.component.CharacterDialog;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.DoorDialog;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.component.MapDialog;
import com.guillot.moria.component.SmallCharacterDialog;
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

    private SmallCharacterDialog smallCharacterDialog;

    private MapDialog mapDialog;

    private DoorDialog doorDialog;

    private TextBox cursorTextBox;

    private Console console;

    private ProgressBar lifeBar;

    private ProgressBar xpBar;

    @Override
    public void start() throws Exception {
        game = new GameState();
        game.init();

        image = new DepthBufferedImage(EngineConfig.WIDTH, EngineConfig.HEIGHT);

        inventoryDialog = new InventoryDialog(this, getPlayer());
        characterDialog = new CharacterDialog(this, getPlayer());
        smallCharacterDialog = new SmallCharacterDialog(this);

        doorDialog = new DoorDialog(this, getPlayer());

        mapDialog = new MapDialog(this, game);

        console = new Console(EngineConfig.WIDTH, 5);
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        lifeBar = new ProgressBar(EngineConfig.WIDTH / 2 - 128, EngineConfig.HEIGHT - 44, 256, 32, 100);
        xpBar = new ProgressBar(EngineConfig.WIDTH / 2 - 128, EngineConfig.HEIGHT - 12, 256, 12, 0);
        xpBar.setValueColor(new Color(223, 207, 134));

        add(console, cursorTextBox, lifeBar, xpBar, inventoryDialog, characterDialog, doorDialog, mapDialog, smallCharacterDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        smallCharacterDialog.setVisible(false);
        cursorTextBox.setVisible(false);
        cursor = null;

        lifeBar.setValue(getPlayer().getCurrentLife() / (float) getPlayer().getLife());
        xpBar.setValue(getPlayer().getXp() / (float) LEVELING_LEVELS[getPlayer().getLevel() - 1]);

        ArrayList<Monster> deadMonsters = new ArrayList<>();
        getDungeon().getMonsters().stream()
                .filter(x -> x.isDead())
                .forEach(x -> {
                    console.addMessage(x.getName() + " is dead!");
                    x.dropEquipment(getDungeon());
                    deadMonsters.add(x);

                    int xp = x.getXPValue();
                    console.addMessage(getPlayer().getName() + " earns " + x.getXPValue() + " xp.");
                    int levelsGained = getPlayer().earnXP(xp);
                    if (levelsGained > 0) {
                        console.addMessage(getPlayer().getName() + " gains " + levelsGained + " levels!");
                    }
                });
        getDungeon().getMonsters().removeAll(deadMonsters);

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
                    getDungeon().getItemsAt(getPlayer().getPosition()).forEach(x -> {
                        if (getPlayer().pickUpItem(x)) {
                            getDungeon().removeItem(x);
                            console.addMessage(getPlayer().getName() + " picked up " + x.getName());
                        }
                    });
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
                    smallCharacterDialog.setCharacter((AbstractCharacter) cursorObject);
                    smallCharacterDialog.setPosition(64, 64);
                    smallCharacterDialog.setVisible(true);
                    cursor = ((AbstractCharacter) cursorObject).getPosition().inverseXY();
                }
            }

            if (cursor != null) {
                if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                    Door door = getDungeon().getDoorAt(cursor.inverseXY());
                    Monster monster = getDungeon().getMonsterAt(cursor.inverseXY());

                    if (door != null) {
                        if (getPlayer().getPosition().inverseXY().distanceFrom(cursor) == 1) {
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
                            path = getDungeon().findPathNear(getPlayer().getPosition().inverseXY(), cursor, getPlayer().getLightRadius());
                            currentStep = 0;
                        }
                    } else if (monster != null) {
                        if (getPlayer().getPosition().inverseXY().distanceFrom(cursor) == 1) {
                            Attack attack = getPlayer().doAttack();
                            if (attack == null) {
                                console.addMessage(getPlayer().getName() + " misses his attack!");
                            } else {
                                console.addMessage((attack.isCritical() ? "WOW! " : "") + getPlayer().getName() + " inflicts "
                                        + attack.getDamages() + " damages!");
                                int damagesReceived = monster.takeAHit(attack);
                                if (damagesReceived < 0) {
                                    console.addMessage(monster.getName() + " dodges the attack!");
                                } else {
                                    console.addMessage(monster.getName() + " takes " + damagesReceived + " damages!");
                                }
                            }
                        } else {
                            path = getDungeon().findPathNear(getPlayer().getPosition().inverseXY(), cursor, getPlayer().getLightRadius());
                            currentStep = 0;
                        }
                    } else {
                        path = getDungeon().findPath(getPlayer().getPosition().inverseXY(), cursor, getPlayer().getLightRadius());
                        currentStep = 0;
                    }
                }

                List<AbstractItem> items = getDungeon().getItemsAt(cursor.inverseXY());
                if (!items.isEmpty()) {
                    String itemsName = "";
                    for (int i = 0; i < items.size(); i++) {
                        if (i > 0) {
                            itemsName += "\n";
                        }

                        itemsName += items.get(i).getName();
                    }
                    showTextBox(itemsName);
                }
            }
        }

        if (GUI.get().isKeyPressed(KEY_I)) {
            characterDialog.setVisible(false);
            mapDialog.setVisible(false);
            inventoryDialog.toggleVisible();
        }

        if (GUI.get().isKeyPressed(KEY_C)) {
            inventoryDialog.setVisible(false);
            mapDialog.setVisible(false);
            characterDialog.toggleVisible();
        }

        if (GUI.get().isKeyPressed(KEY_M)) {
            inventoryDialog.setVisible(false);
            characterDialog.setVisible(false);
            mapDialog.toggleVisible();
        }
    }

    @Override
    public void paint(Graphics g) throws Exception {
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
                                && (grid[i + 1][j].isFloor || isDoor(new Point(j, i + 1)))) {
                            alternate = true;
                        } else if (j - 1 >= 0 && grid[i][j - 1] != null && grid[i][j - 1].isFloor) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && grid[i + 1][j - 1] != null
                                && (grid[i + 1][j - 1].isFloor || isDoor(new Point(j - 1, i + 1)))) {
                            alternate = true;
                        }
                    }

                    drawTile(tile, i, j, alternate, door);

                    getDungeon().getItemsAt(new Point(j, i)).forEach(x -> x.draw(image, getPlayer().getPosition()));

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
                                        || isDoor(new Point(j, i + 1)))) {
                            alternate = true;
                        } else if (j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i][j - 1].isFloor)) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i + 1][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i + 1][j - 1].isFloor
                                        || isDoor(new Point(j - 1, i + 1)))) {
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

        super.paint(g);
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

    private boolean isDoor(Point point) {
        Door door = getDungeon().getDoorAt(point);
        return door != null && door.getState() != DoorState.SECRET;
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
