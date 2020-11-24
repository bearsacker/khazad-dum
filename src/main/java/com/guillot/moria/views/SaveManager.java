package com.guillot.moria.views;

import static com.guillot.moria.configs.DungeonConfig.DUNGEON_GENERATOR_VERSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.character.Monster;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Entity;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.Point;

public class SaveManager {

    public final static String SAVE_PATH = "game.sav";

    public static void writeSaveFile(String path, GameState game) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream stream = new ObjectOutputStream(fos);

        stream.writeInt(DUNGEON_GENERATOR_VERSION);
        stream.writeInt(game.getCurrentLevel());
        stream.writeObject(game.getPlayer());
        stream.writeInt(game.getLevels().size());
        for (Dungeon dungeon : game.getLevels()) {
            encodeDungeon(stream, dungeon);
        }

        stream.close();
        fos.close();
    }

    public static void loadSaveFile(GameState game, String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream stream = new ObjectInputStream(fis);

        int version = stream.readInt();
        if (version == DUNGEON_GENERATOR_VERSION) {
            game.setCurrentLevel(stream.readInt());
            game.setPlayer((AbstractCharacter) stream.readObject());
            game.setLevels(new ArrayList<>());

            int dungeonNumber = stream.readInt();
            for (int i = 0; i < dungeonNumber; i++) {
                game.getLevels().add(decodeDungeon(stream));
            }
        }

        stream.close();
        fis.close();
    }

    private static void encodeDungeon(ObjectOutputStream stream, Dungeon dungeon) throws IOException {
        stream.writeInt(dungeon.getLevel());
        stream.writeInt(dungeon.getWidth());
        stream.writeInt(dungeon.getHeight());
        stream.writeObject(dungeon.getSpawnDownStairs());
        stream.writeObject(dungeon.getSpawnUpStairs());
        stream.writeObject(dungeon.getFloor());
        stream.writeObject(dungeon.getDiscoveredTiles());
        stream.writeObject(dungeon.getEntities());
        stream.writeObject(dungeon.getItems());
        stream.writeObject(dungeon.getDoors());
        stream.writeObject(dungeon.getMonsters());
    }

    @SuppressWarnings("unchecked")
    private static Dungeon decodeDungeon(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        Dungeon dungeon = new Dungeon(stream.readInt());
        dungeon.setWidth(stream.readInt());
        dungeon.setHeight(stream.readInt());
        dungeon.setSpawnDownStairs((Point) stream.readObject());
        dungeon.setSpawnUpStairs((Point) stream.readObject());
        dungeon.setFloor((Tile[][]) stream.readObject());
        dungeon.setDiscoveredTiles((Tile[][]) stream.readObject());
        dungeon.setEntities((HashMap<Point, Entity>) stream.readObject());
        dungeon.setItems((ArrayList<AbstractItem>) stream.readObject());
        dungeon.setDoors((ArrayList<Door>) stream.readObject());
        dungeon.setMonsters((ArrayList<Monster>) stream.readObject());
        dungeon.initAStar();

        return dungeon;
    }
}
