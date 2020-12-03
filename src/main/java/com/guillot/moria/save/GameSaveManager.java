package com.guillot.moria.save;

import static com.guillot.moria.configs.DungeonConfig.DUNGEON_VERSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.guillot.engine.particles.Generator;
import com.guillot.engine.particles.Particles;
import com.guillot.engine.utils.LinkedNonBlockingQueue;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.Turn;

public class GameSaveManager {

    public final static String GAME_SAVE_PATH = "game.dat";

    public static boolean isSaveFilePresent(String path) {
        return new File(path).exists();
    }

    public static void deleteSaveFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void writeSaveFile(String path, GameState game) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream stream = new ObjectOutputStream(fos);

        stream.writeInt(DUNGEON_VERSION);
        stream.writeInt(game.getCurrentLevel());
        stream.writeObject(game.getTurn());
        stream.writeObject(game.getPlayer());
        stream.writeObject(game.getMessages());
        stream.writeInt(game.getLevels().size());
        for (Dungeon dungeon : game.getLevels()) {
            DungeonEncoder.encode(stream, dungeon);
        }
        stream.writeObject(Particles.get().getGenerators());

        stream.close();
        fos.close();
    }

    @SuppressWarnings("unchecked")
    public static void loadSaveFile(GameState game, String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream stream = new ObjectInputStream(fis);

        int version = stream.readInt();
        if (version == DUNGEON_VERSION) {
            game.setCurrentLevel(stream.readInt());
            game.setTurn((Turn) stream.readObject());
            game.setPlayer((AbstractCharacter) stream.readObject());
            game.setMessages((LinkedNonBlockingQueue<String>) stream.readObject());
            game.setLevels(new ArrayList<>());

            int dungeonNumber = stream.readInt();
            for (int i = 0; i < dungeonNumber; i++) {
                game.getLevels().add(DungeonEncoder.decode(stream));
            }

            Particles.get().setGenerators((ArrayList<Generator>) stream.readObject());
        }

        stream.close();
        fis.close();
    }
}
