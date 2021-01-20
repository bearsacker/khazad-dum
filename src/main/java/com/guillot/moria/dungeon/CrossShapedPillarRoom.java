package com.guillot.moria.dungeon;

import static com.guillot.moria.dungeon.Tile.MAGMA_WALL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.dungeon.entity.Pillar;
import com.guillot.moria.utils.Point;

public class CrossShapedPillarRoom extends AbstractCrossShapedRoom {

    private final static Logger logger = LogManager.getLogger(CrossShapedPillarRoom.class);

    public CrossShapedPillarRoom(Dungeon dungeon, Point position) {
        super(dungeon, position);

        logger.info("Building room cross shaped (large middle pillar) at " + position + "...");
        placeLargeMiddlePillar();
    }

    private void placeLargeMiddlePillar() {
        logger.info("\t-> Placing large middle pillar at " + position + "...");

        dungeon.getEntities().add(new Pillar(new Point(position.x, position.y - 1)));
        dungeon.getEntities().add(new Pillar(new Point(position.x, position.y + 1)));
        dungeon.setTile(position.y, position.x, MAGMA_WALL);
        dungeon.getEntities().add(new Pillar(new Point(position.x + 1, position.y)));
        dungeon.getEntities().add(new Pillar(new Point(position.x - 1, position.y)));
    }

}
