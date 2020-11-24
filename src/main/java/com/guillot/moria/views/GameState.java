package com.guillot.moria.views;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Human;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.utils.RNG;

public class GameState {

    private final static int NUMBER_LEVELS = 9;

    private ArrayList<Dungeon> levels;

    private int currentLevel;

    private AbstractCharacter player;

    public void init() {
        levels = new ArrayList<>();
        currentLevel = 0;

        for (int i = 1; i <= NUMBER_LEVELS; i++) {
            Dungeon dungeon = new Dungeon(i);
            boolean eligible = false;
            while (!eligible) {
                eligible = dungeon.generate();
            }

            levels.add(dungeon);
        }

        player = new Human("Jean");
        player.setPosition(levels.get(currentLevel).getSpawnUpStairs());
    }

    public void init(String path) {
        try {
            SaveManager.loadSaveFile(this, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        }
    }

    public void toGoDownstairs() {
        if (currentLevel <= NUMBER_LEVELS) {
            currentLevel++;
            player.setPosition(getDungeon().getSpawnUpStairs());
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
        RNG.get().setSeed(1606213583967L);

        new Game("Khazad-dûm", new MenuView());
    }
}
