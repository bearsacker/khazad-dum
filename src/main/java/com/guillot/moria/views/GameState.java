package com.guillot.moria.views;

import static com.guillot.moria.save.GameSaveManager.GAME_SAVE_PATH;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.utils.LinkedNonBlockingQueue;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Monster;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.save.GameSaveManager;

public class GameState {

    private final static int NUMBER_LEVELS = 9;

    private Turn turn;

    private ArrayList<Dungeon> levels;

    private int currentLevel;

    private AbstractCharacter player;

    private LinkedNonBlockingQueue<String> messages;

    public void init(AbstractCharacter player) throws Exception {
        levels = new ArrayList<>();
        messages = new LinkedNonBlockingQueue<>(10);
        turn = Turn.PLAYER;
        currentLevel = 0;

        for (int i = 1; i <= NUMBER_LEVELS; i++) {
            Dungeon dungeon = new Dungeon(i);
            boolean eligible = false;
            while (!eligible) {
                eligible = dungeon.generate();
            }

            levels.add(dungeon);
        }

        this.player = player;
        this.player.setPosition(levels.get(currentLevel).getSpawnUpStairs());

        GameSaveManager.writeSaveFile(GAME_SAVE_PATH, this);
    }

    public void initFromSave() throws Exception {
        GameSaveManager.loadSaveFile(this, GAME_SAVE_PATH);
    }

    public void update() {
        ArrayList<Monster> deadMonsters = new ArrayList<>();
        getDungeon().getMonsters().stream()
                .filter(x -> x.isDead())
                .forEach(x -> {
                    addMessage(x.getName() + " is dead!");
                    x.dropEquipment(getDungeon());
                    deadMonsters.add(x);

                    int xp = x.getXPValue();
                    addMessage("GREEN_PALE@@You@@WHITE@@ earn @@YELLOW_PALE@@" + x.getXPValue() + "@@WHITE@@ xp.");
                    int levelsGained = getPlayer().earnXP(xp);
                    if (levelsGained > 0) {
                        addMessage("GREEN_PALE@@You@@WHITE@@ gain @@YELLOW_PALE@@" + levelsGained + "@@WHITE@@ levels!");
                    }
                });
        getDungeon().getMonsters().removeAll(deadMonsters);

        if (turn == Turn.GAMEMASTER_BEGIN) {
            getDungeon().getMonsters().forEach(x -> x.takeDecision(this));
            endTurn();
        } else if (turn == Turn.GAMEMASTER) {
            getDungeon().getMonsters().forEach(x -> x.update(this));

            if (!getDungeon().getMonsters().stream().anyMatch(x -> x.isActing())) {
                endTurn();
            }
        } else if (turn == Turn.PLAYER) {
            player.update(this);

            if (!player.isMoving()) {
                getDungeon().getItemsAt(getPlayer().getPosition()).forEach(x -> {
                    if (getPlayer().pickUpItem(x)) {
                        getDungeon().removeItem(x);
                        addMessage("GREEN_PALE@@You@@WHITE@@ picked up @@" + x.getFormattedName());
                    }
                });

                if (player.isActing()) {
                    player.setActing(false);
                    endTurn();
                }
            }
        }
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public LinkedNonBlockingQueue<String> getMessages() {
        return messages;
    }

    public void setMessages(LinkedNonBlockingQueue<String> messages) {
        this.messages = messages;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Dungeon getDungeon() {
        return levels.get(currentLevel);
    }

    public AbstractCharacter getPlayer() {
        return player;
    }

    public void toGoUpstairs() {
        if (currentLevel > 0) {
            currentLevel--;
            player.setPosition(getDungeon().getSpawnDownStairs());

            addMessage("GREEN_PALE@@You@@WHITE@@ come back to level " + getDungeon().getLevel());
        }
    }

    public void toGoDownstairs() {
        if (currentLevel <= NUMBER_LEVELS) {
            currentLevel++;
            player.setPosition(getDungeon().getSpawnUpStairs());

            addMessage("GREEN_PALE@@You@@WHITE@@ go to level " + getDungeon().getLevel());
        }
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Turn getTurn() {
        return turn;
    }

    public void endTurn() {
        switch (turn) {
        case GAMEMASTER_BEGIN:
            turn = Turn.GAMEMASTER;
            break;
        case GAMEMASTER:
            turn = Turn.PLAYER;
            break;
        case PLAYER:
            turn = Turn.GAMEMASTER_BEGIN;
            break;
        }
    }

    public ArrayList<Dungeon> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Dungeon> levels) {
        this.levels = levels;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setPlayer(AbstractCharacter player) {
        this.player = player;
    }

    public static void main(String[] args) throws SlickException {
        new Game(new MenuView());
    }
}
