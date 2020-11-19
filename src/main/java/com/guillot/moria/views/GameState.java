package com.guillot.moria.views;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Human;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.utils.RNG;

public class GameState {

    private final static int NUMBER_LEVELS = 10;

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

    public static void main(String[] args) throws SlickException {
        RNG.get().setSeed(1605796012050L);

        new Game("Khazad-dûm", new GameView());
    }
}
