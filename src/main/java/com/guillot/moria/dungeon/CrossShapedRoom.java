package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.TMP1_WALL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.dungeon.entity.Pillar;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class CrossShapedRoom extends AbstractCrossShapedRoom {

    private final static Logger logger = LogManager.getLogger(CrossShapedRoom.class);

    public CrossShapedRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room cross shaped at " + position + "...");
        if (RNG.get().randomNumber(3) == 1) {
            dungeon.getEntities().add(new Pillar(new Point(position.x - 2, position.y - 1)));
            dungeon.getEntities().add(new Pillar(new Point(position.x - 2, position.y + 1)));
            dungeon.getEntities().add(new Pillar(new Point(position.x + 2, position.y - 1)));
            dungeon.getEntities().add(new Pillar(new Point(position.x + 2, position.y + 1)));
            dungeon.getEntities().add(new Pillar(new Point(position.x - 1, position.y - 2)));
            dungeon.getEntities().add(new Pillar(new Point(position.x + 1, position.y - 2)));
            dungeon.getEntities().add(new Pillar(new Point(position.x - 1, position.y + 2)));
            dungeon.getEntities().add(new Pillar(new Point(position.x + 1, position.y + 2)));
        } else if (RNG.get().randomNumber(3) == 1) {
            dungeon.setTile(position.y, position.x, TMP1_WALL);
            dungeon.setTile(position.y - 1, position.x, TMP1_WALL);
            dungeon.setTile(position.y + 1, position.x, TMP1_WALL);
            dungeon.setTile(position.y, position.x - 1, TMP1_WALL);
            dungeon.setTile(position.y, position.x + 1, TMP1_WALL);
        } else if (RNG.get().randomNumber(3) == 1) {
            dungeon.setTile(position.y, position.x, TMP1_WALL);
        }
    }

}
