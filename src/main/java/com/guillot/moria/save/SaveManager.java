package com.guillot.moria.save;

import static com.guillot.moria.configs.DungeonConfig.DUNGEON_GENERATOR_VERSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.views.GameState;

public class SaveManager {

    public final static String SAVE_PATH = "game.sav";

    public static boolean isSaveFilePresent(String path) {
        return new File(path).exists();
    }

    public static void writeSaveFile(String path, GameState game) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream stream = new ObjectOutputStream(fos);

        stream.writeInt(DUNGEON_GENERATOR_VERSION);
        stream.writeInt(game.getCurrentLevel());
        stream.writeObject(game.getPlayer());
        stream.writeInt(game.getLevels().size());
        for (Dungeon dungeon : game.getLevels()) {
            DungeonEncoder.encode(stream, dungeon);
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
                game.getLevels().add(DungeonEncoder.decode(stream));
            }
        }

        stream.close();
        fis.close();
    }
}
