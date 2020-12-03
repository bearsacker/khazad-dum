package com.guillot.moria.save;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.guillot.moria.character.Monster;
import com.guillot.moria.dungeon.Door;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.dungeon.entity.AbstractEntity;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.Point;

public class DungeonEncoder {

    public static void encode(ObjectOutputStream stream, Dungeon dungeon) throws IOException {
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
    public static Dungeon decode(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        Dungeon dungeon = new Dungeon(stream.readInt());
        dungeon.setWidth(stream.readInt());
        dungeon.setHeight(stream.readInt());
        dungeon.setSpawnDownStairs((Point) stream.readObject());
        dungeon.setSpawnUpStairs((Point) stream.readObject());
        dungeon.setFloor((Tile[][]) stream.readObject());
        dungeon.setDiscoveredTiles((Tile[][]) stream.readObject());
        dungeon.setEntities((ArrayList<AbstractEntity>) stream.readObject());
        dungeon.setItems((ArrayList<AbstractItem>) stream.readObject());
        dungeon.setDoors((ArrayList<Door>) stream.readObject());
        dungeon.setMonsters((ArrayList<Monster>) stream.readObject());
        dungeon.initAStar();

        return dungeon;
    }
}
