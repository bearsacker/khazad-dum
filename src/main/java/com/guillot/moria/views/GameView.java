package com.guillot.moria.views;

import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;
import static com.guillot.moria.configs.LevelingConfig.LEVELING_LEVELS;
import static com.guillot.moria.dungeon.Tile.UP_STAIR;
import static com.guillot.moria.ressources.Colors.DARK_GREY;
import static com.guillot.moria.ressources.Colors.WHITE;
import static com.guillot.moria.ressources.Colors.YELLOW;
import static com.guillot.moria.ressources.Images.CURSOR;
import static org.newdawn.slick.Input.KEY_C;
import static org.newdawn.slick.Input.KEY_ESCAPE;
import static org.newdawn.slick.Input.KEY_I;
import static org.newdawn.slick.Input.KEY_M;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.ProgressBar;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.TextBox;
import com.guillot.engine.gui.View;
import com.guillot.engine.particles.Particles;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Monster;
import com.guillot.moria.component.CharacterDialog;
import com.guillot.moria.component.Console;
import com.guillot.moria.component.DoorDialog;
import com.guillot.moria.component.InventoryDialog;
import com.guillot.moria.component.MapDialog;
import com.guillot.moria.component.MenuDialog;
import com.guillot.moria.component.SmallCharacterDialog;
import com.guillot.moria.dungeon.Direction;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.DoorState;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.dungeon.entity.AbstractEntity;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.ressources.Colors;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.DepthBuffer;
import com.guillot.moria.utils.Point;

public class GameView extends View {

    public final static int SIZE = 64;

    private GameState game;

    private DepthBuffer<Point> depthBuffer;

    private Point cursor;

    private long lastStep;

    // Components

    private Text turnText;

    private MenuDialog menuDialog;

    private InventoryDialog inventoryDialog;

    private CharacterDialog characterDialog;

    private SmallCharacterDialog smallCharacterDialog;

    private MapDialog mapDialog;

    private DoorDialog doorDialog;

    private TextBox cursorTextBox;

    private Console console;

    private Text lifeText;

    private ProgressBar lifeBar;

    private ProgressBar xpBar;

    private Button mapButton;

    private Button inventoryButton;

    private Button characterButton;

    private Button menuButton;

    public GameView(GameState game) {
        this.game = game;
    }

    @Override
    public void start() throws Exception {
        depthBuffer = new DepthBuffer<>(WIDTH, EngineConfig.HEIGHT);

        turnText = new Text("", 8, 16, GUI.get().getFont(2), YELLOW.getColor());

        menuDialog = new MenuDialog(this, game);
        inventoryDialog = new InventoryDialog(this, game);
        characterDialog = new CharacterDialog(this, game);
        smallCharacterDialog = new SmallCharacterDialog(this);
        doorDialog = new DoorDialog(this, game);
        mapDialog = new MapDialog(this, game);

        console = new Console(WIDTH, game.getMessages());
        console.setY(EngineConfig.HEIGHT - console.getHeight());

        cursorTextBox = new TextBox();
        cursorTextBox.setVisible(false);

        lifeText = new Text(getPlayer().getCurrentLife() + "/" + getPlayer().getLife(), 0, HEIGHT - 46, GUI.get().getFont(),
                WHITE.getColor());
        lifeText.setVisible(false);
        lifeText.setX(WIDTH / 2 - 96 - lifeText.getWidth() / 2);

        lifeBar = new ProgressBar(WIDTH / 2 - 224, HEIGHT - 54, 256, 32, 100);
        xpBar = new ProgressBar(WIDTH / 2 - 224, HEIGHT - 22, 256, 12, 0);
        xpBar.setValueColor(YELLOW.getColor());

        mapButton = new Button("", WIDTH / 2 + 40, HEIGHT - 56, 48, 48);
        mapButton.setIcon(Images.MAP.getImage());
        mapButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                mapDialog.setVisible(true);
            }
        });

        inventoryButton = new Button("", WIDTH / 2 + 84, EngineConfig.HEIGHT - 56, 48, 48);
        inventoryButton.setIcon(Images.INVENTORY.getImage());
        inventoryButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                inventoryDialog.setVisible(true);
            }
        });

        characterButton = new Button("", WIDTH / 2 + 128, EngineConfig.HEIGHT - 56, 48, 48);
        characterButton.setIcon(Images.CHARACTER.getImage());
        characterButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                characterDialog.setVisible(true);
            }
        });

        menuButton = new Button("", WIDTH / 2 + 172, EngineConfig.HEIGHT - 56, 48, 48);
        menuButton.setIcon(Images.MENU.getImage());
        menuButton.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                menuDialog.setVisible(true);
            }
        });

        add(turnText, lifeBar, lifeText, xpBar, mapButton, inventoryButton, characterButton, menuButton, cursorTextBox, doorDialog, console,
                menuDialog, inventoryDialog, characterDialog, mapDialog, smallCharacterDialog);
    }

    @Override
    public void update() throws Exception {
        super.update();

        Particles.get().update();

        smallCharacterDialog.setVisible(false);
        cursorTextBox.setVisible(false);
        cursor = null;

        lifeText.setText(getPlayer().getCurrentLife() + "/" + getPlayer().getLife());
        lifeText.setX(WIDTH / 2 - 96 - lifeText.getWidth() / 2);
        lifeText.setVisible(lifeBar.mouseOn());
        lifeBar.setValue(getPlayer().getCurrentLife() / (float) getPlayer().getLife());
        xpBar.setValue(getPlayer().getXp() / (float) LEVELING_LEVELS[getPlayer().getLevel() - 1]);

        turnText.setText(game.getTurn() == Turn.PLAYER ? "Your turn" : "GameMaster's turn");
        turnText.setX(WIDTH / 2 - turnText.getWidth() / 2);

        if (getPlayer().isDead()) {
            GUI.get().switchView(new DeathView(game));
        }

        long time = System.currentTimeMillis();
        if (time - lastStep > 75) {
            game.update();
            lastStep = time;
        }

        if (isFocused()) {
            cursor = depthBuffer.getDepth(GUI.get().getMouseX(), GUI.get().getMouseY());

            if (cursor != null) {
                if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
                    Door door = getDungeon().getDoorAt(cursor);
                    Monster monster = getDungeon().getMonsterAt(cursor);

                    if (door != null) {
                        if (getPlayer().getPosition().distanceFrom(cursor) == 1) {
                            switch (door.getState()) {
                            case LOCKED:
                                GUI.get().getInput().clearMousePressedRecord();
                                GUI.get().getInput().clearKeyPressedRecord();
                                doorDialog.setDoor(door);
                                doorDialog.setVisible(true);
                                break;
                            case SECRET:
                                door.setState(DoorState.OPEN);
                                game.addMessage("GREEN_PALE@@You@@WHITE@@ discover a secret door !");
                            case OPEN:
                                getPlayer().setPosition(door.getDirectionPosition(getPlayer().getPosition()));
                                break;
                            case STUCK:
                                break;
                            }
                        } else {
                            getPlayer().setPath(
                                    getDungeon().findPathNear(getPlayer().getPosition(), cursor, getPlayer().getLightRadius()));
                        }
                    } else if (monster != null) {
                        if (getPlayer().canAttack(cursor)) {
                            getPlayer().setTarget(monster);
                        } else {
                            getPlayer().setPath(
                                    getDungeon().findPathNear(getPlayer().getPosition(), cursor, getPlayer().getLightRadius()));
                        }
                    } else {
                        getPlayer().setPath(
                                getDungeon().findPath(getPlayer().getPosition(), cursor, getPlayer().getLightRadius()));
                    }

                    if (getPlayer().isMoving() || getPlayer().getTarget() != null) {
                        getPlayer().setActing(true);
                    }
                }

                AbstractCharacter character = getPlayer().getPosition().is(cursor) ? getPlayer() : getDungeon().getMonsterAt(cursor);
                if (character != null) {
                    smallCharacterDialog.setCharacter(character);
                    smallCharacterDialog.setPosition(64, 64);
                    smallCharacterDialog.setVisible(true);
                }

                List<AbstractItem> items = getDungeon().getItemsAt(cursor);
                if (!items.isEmpty()) {
                    String itemsName = "";
                    for (int i = 0; i < items.size(); i++) {
                        if (i > 0) {
                            itemsName += "\n";
                        }

                        itemsName += items.get(i).getFormattedName();
                    }
                    showTextBox(itemsName);
                }
            }

            if (mapButton.mouseOn()) {
                showTextBox("Map (M)");
            } else if (inventoryButton.mouseOn()) {
                showTextBox("Inventory (I)");
            } else if (characterButton.mouseOn()) {
                showTextBox("Character (C)");
            } else if (menuButton.mouseOn()) {
                showTextBox("Menu (Esc)");
            }
        }

        if (GUI.get().isKeyPressed(KEY_I)) {
            characterDialog.setVisible(false);
            mapDialog.setVisible(false);
            menuDialog.setVisible(false);
            inventoryDialog.toggleVisible();
        }

        if (GUI.get().isKeyPressed(KEY_C)) {
            inventoryDialog.setVisible(false);
            mapDialog.setVisible(false);
            menuDialog.setVisible(false);
            characterDialog.toggleVisible();
        }

        if (GUI.get().isKeyPressed(KEY_M)) {
            inventoryDialog.setVisible(false);
            characterDialog.setVisible(false);
            menuDialog.setVisible(false);
            mapDialog.toggleVisible();
        }

        if (GUI.get().isKeyPressed(KEY_ESCAPE)) {
            inventoryDialog.setVisible(false);
            characterDialog.setVisible(false);
            mapDialog.setVisible(false);
            menuDialog.toggleVisible();
        }
    }

    @Override
    public void paint(Graphics g) throws Exception {
        depthBuffer.clear();

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
                                && (grid[i + 1][j].isFloor || getDungeon().isVisibleDoor(new Point(j, i + 1)))) {
                            alternate = true;
                        } else if (j - 1 >= 0 && grid[i][j - 1] != null && grid[i][j - 1].isFloor) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && grid[i + 1][j - 1] != null
                                && (grid[i + 1][j - 1].isFloor || getDungeon().isVisibleDoor(new Point(j - 1, i + 1)))) {
                            alternate = true;
                        }
                    }

                    float alpha = (float) (1.25f
                            - getPlayer().getPosition().distanceFrom(new Point(j, i)) / (float) getPlayer().getLightRadius());
                    Color shadow = new Color(alpha, alpha, alpha);
                    drawTile(g, tile, i, j, alternate, door, new Point(i, j), shadow);

                    AbstractEntity entity = getDungeon().getEntityAt(new Point(j, i));
                    if (entity != null) {
                        entity.draw(g, getPlayer().getPosition(), shadow);
                    }

                    getDungeon().getItemsAt(new Point(j, i)).forEach(x -> x.draw(g, getPlayer().getPosition()));

                    if (getPlayer().getPosition().is(j, i)) {
                        getPlayer().draw(g, getPlayer().getPosition(), shadow);
                    }

                    Monster monster = getDungeon().getMonsterAt(new Point(j, i));
                    if (monster != null) {
                        monster.draw(g, getPlayer().getPosition(), shadow);
                    }
                } else if (getDungeon().getDiscoveredTiles()[i][j] != null) {
                    boolean alternate = false;

                    Door door = getDungeon().getDoorAt(new Point(j, i));
                    if (getDungeon().getDiscoveredTiles()[i][j].isWall && (door == null || door.getState() == DoorState.SECRET)) {
                        if (i + 1 < getDungeon().getHeight() && getDungeon().getDiscoveredTiles()[i + 1][j] != null
                                && (getDungeon().getDiscoveredTiles()[i + 1][j].isFloor
                                        || getDungeon().isVisibleDoor(new Point(j, i + 1)))) {
                            alternate = true;
                        } else if (j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i][j - 1].isFloor)) {
                            alternate = true;
                        } else if (i + 1 < getDungeon().getHeight() && j - 1 >= 0 && getDungeon().getDiscoveredTiles()[i + 1][j - 1] != null
                                && (getDungeon().getDiscoveredTiles()[i + 1][j - 1].isFloor
                                        || getDungeon().isVisibleDoor(new Point(j - 1, i + 1)))) {
                            alternate = true;
                        }
                    }

                    drawTile(g, getDungeon().getDiscoveredTiles()[i][j], i, j, alternate, door, null, DARK_GREY.getColor());

                    AbstractEntity entity = getDungeon().getEntityAt(new Point(j, i));
                    if (entity != null) {
                        entity.draw(g, getPlayer().getPosition(), DARK_GREY.getColor());
                    }

                    getDungeon().getItemsAt(new Point(j, i)).forEach(x -> x.draw(g, getPlayer().getPosition()));
                }
            }
        }

        if (cursor != null) {
            drawCursor(g, cursor.y, cursor.x, getDungeon().getFloor()[cursor.y][cursor.x]);
        }

        g.drawImage(Images.STONE.getImage(), WIDTH / 2 - Images.STONE.getImage().getWidth() / 2, HEIGHT - 64);

        super.paint(g);

        if (focused && getPlayer().getAttributesPoints() > 0) {
            g.setColor(Colors.ITEM_LEGENDARY.getColor());
            g.setLineWidth(2f);

            g.drawRect(characterButton.getX() + 2, characterButton.getY() + 2, characterButton.getWidth() - 6,
                    characterButton.getHeight() - 6);
        }
    }

    private void computeViewedTiles(HashMap<Point, Float> depthList, Tile[][] grid, Point position, float length) {
        if (length >= getPlayer().getLightRadius()) {
            return;
        }

        Float depthValue = depthList.get(position);
        if (depthValue != null && depthValue < length) {
            return;
        }

        depthList.put(position, length);

        if (position.x >= 0 && position.y >= 0 && position.x < getDungeon().getWidth() && position.y < getDungeon().getHeight()) {
            Tile dungeonTile = getDungeon().getFloor()[position.y][position.x];
            grid[position.y][position.x] = dungeonTile;

            if (!dungeonTile.isWall) {
                computeViewedTiles(depthList, grid, new Point(position.x - 1, position.y), length + 1f);
                computeViewedTiles(depthList, grid, new Point(position.x + 1, position.y), length + 1f);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y - 1), length + 1f);
                computeViewedTiles(depthList, grid, new Point(position.x, position.y + 1), length + 1f);

                Tile north = getDungeon().getFloor()[position.y - 1][position.x];
                Tile south = getDungeon().getFloor()[position.y + 1][position.x];
                Tile west = getDungeon().getFloor()[position.y][position.x - 1];
                Tile east = getDungeon().getFloor()[position.y][position.x + 1];
                if (!north.isWall && !west.isWall) {
                    computeViewedTiles(depthList, grid, new Point(position.x - 1, position.y - 1), length + 1.41f);
                }
                if (!north.isWall && !east.isWall) {
                    computeViewedTiles(depthList, grid, new Point(position.x + 1, position.y - 1), length + 1.41f);
                }
                if (!south.isWall && !west.isWall) {
                    computeViewedTiles(depthList, grid, new Point(position.x - 1, position.y + 1), length + 1.41f);
                }
                if (!south.isWall && !east.isWall) {
                    computeViewedTiles(depthList, grid, new Point(position.x + 1, position.y + 1), length + 1.41f);
                }
            }
        }
    }

    private void drawTile(Graphics g, Tile tile, int px, int py, boolean alternate, Door door, Object value, Color filter) {
        int x = (px - getPlayer().getPosition().y) * 32 + (py - getPlayer().getPosition().x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (py - getPlayer().getPosition().x) * 16 - (px - getPlayer().getPosition().y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        if (tile.image != null) {
            if (alternate) {
                depthBuffer.drawImageBuffer(new Point(py, px), tile.bufferAlternate, x, y);
                g.drawImage(tile.image.getSubImage(64, 0, 64, 96), x, y, filter);
            } else {
                depthBuffer.drawImageBuffer(new Point(py, px), tile.buffer, x, y);
                g.drawImage(tile.image.getSubImage(0, 0, 64, 96), x, y, filter);
            }

            if (door != null && door.getState() != DoorState.SECRET) {
                if (door.getDirection() == Direction.NORTH) {
                    g.drawImage(Images.DOOR.getSubImage(1, 0), x, y, filter);
                } else if (door.getDirection() == Direction.WEST) {
                    g.drawImage(Images.DOOR.getSubImage(0, 0), x, y, filter);
                }
            }
        }
    }

    private void drawCursor(Graphics g, int px, int py, Tile tile) {
        if (tile != null) {
            int x = (px - getPlayer().getPosition().y) * 32 + (py - getPlayer().getPosition().x) * 32 + EngineConfig.WIDTH / 2 - 32;
            int y = (py - getPlayer().getPosition().x) * 16 - (px - getPlayer().getPosition().y) * 16 + EngineConfig.HEIGHT / 2 - 48;

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

    public Dungeon getDungeon() {
        return game.getDungeon();
    }

    public AbstractCharacter getPlayer() {
        return game.getPlayer();
    }

}
