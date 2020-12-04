package com.guillot.moria.dungeon;

import static com.guillot.moria.configs.DungeonConfig.DUNGEON_DIR_CHANGE;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_MAGMA_STREAMER;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_MAGMA_TREASURE;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_MAX_HEIGHT;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_MAX_WIDTH;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_QUARTZ_STREAMER;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_QUARTZ_TREASURE;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_RANDOM_DIR;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_ROOMS_MEAN;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_ROOM_DOORS;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_STREAMER_DENSITY;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_STREAMER_WIDTH;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_TUNNELING;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_TUNNEL_DOORS;
import static com.guillot.moria.configs.DungeonConfig.DUNGEON_UNUSUAL_ROOMS;
import static com.guillot.moria.configs.MonstersConfig.MON_CHANCE_OF_NASTY;
import static com.guillot.moria.configs.MonstersConfig.MON_MIN_PER_LEVEL;
import static com.guillot.moria.configs.MonstersConfig.MON_SUMMONED_LEVEL_ADJUST;
import static com.guillot.moria.configs.ObjectsConfig.LEVEL_OBJECTS_PER_CORRIDOR;
import static com.guillot.moria.configs.ObjectsConfig.LEVEL_OBJECTS_PER_ROOM;
import static com.guillot.moria.configs.ObjectsConfig.LEVEL_TOTAL_GOLD_AND_GEMS;
import static com.guillot.moria.configs.ObjectsConfig.MAX_GOLD_TYPES;
import static com.guillot.moria.configs.ObjectsConfig.TREASURE_CHANCE_OF_GREAT_ITEM;
import static com.guillot.moria.configs.ScreenConfig.QUART_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.QUART_WIDTH;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_WIDTH;
import static com.guillot.moria.dungeon.PlacedObject.GOLD;
import static com.guillot.moria.dungeon.PlacedObject.RANDOM;
import static com.guillot.moria.dungeon.PlacedObject.RUBBLE;
import static com.guillot.moria.dungeon.PlacedObject.TRAP;
import static com.guillot.moria.dungeon.Tile.CORRIDOR_FLOOR;
import static com.guillot.moria.dungeon.Tile.DOWN_STAIR;
import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.MAGMA_WALL;
import static com.guillot.moria.dungeon.Tile.NULL;
import static com.guillot.moria.dungeon.Tile.QUARTZ_WALL;
import static com.guillot.moria.dungeon.Tile.ROOM_FLOOR;
import static com.guillot.moria.dungeon.Tile.TMP1_WALL;
import static com.guillot.moria.dungeon.Tile.TMP2_WALL;
import static com.guillot.moria.dungeon.Tile.UP_STAIR;
import static com.guillot.moria.dungeon.entity.Direction.NORTH;
import static com.guillot.moria.dungeon.entity.Direction.WEST;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.guillot.moria.ai.AStar;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.Monster;
import com.guillot.moria.character.MonsterRace;
import com.guillot.moria.dungeon.entity.AbstractEntity;
import com.guillot.moria.dungeon.entity.Direction;
import com.guillot.moria.dungeon.entity.Door;
import com.guillot.moria.dungeon.entity.DoorState;
import com.guillot.moria.dungeon.entity.Entity;
import com.guillot.moria.dungeon.entity.FireCamp;
import com.guillot.moria.dungeon.entity.Pillar;
import com.guillot.moria.dungeon.entity.Rubble;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Gold;
import com.guillot.moria.item.ItemGenerator;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class Dungeon {

    private final static Logger logger = Logger.getLogger(Dungeon.class);

    private int level;

    private int width;

    private int height;

    private Tile[][] floor;

    private Tile[][] discoveredTiles;

    private ArrayList<AbstractItem> items;

    private ArrayList<Monster> monsters;

    private ArrayList<AbstractEntity> entities;

    private AStar astar;

    private Point spawnUpStairs;

    private Point spawnDownStairs;

    public Dungeon(int level) {
        this.level = level;
        height = DUNGEON_MAX_HEIGHT;
        width = DUNGEON_MAX_WIDTH;
        floor = new Tile[DUNGEON_MAX_HEIGHT][DUNGEON_MAX_WIDTH];
        for (int y = 0; y < DUNGEON_MAX_HEIGHT; y++) {
            for (int x = 0; x < DUNGEON_MAX_WIDTH; x++) {
                floor[y][x] = NULL;
            }
        }

        discoveredTiles = new Tile[DUNGEON_MAX_HEIGHT][DUNGEON_MAX_WIDTH];

        items = new ArrayList<>();
        monsters = new ArrayList<>();
        entities = new ArrayList<>();
    }

    // Cave logic flow for generation of new dungeon
    public boolean generate() {
        logger.info("Generating dungeon of level " + level + " [seed=" + RNG.get().getSeed() + "]...");

        for (int y = 0; y < DUNGEON_MAX_HEIGHT; y++) {
            for (int x = 0; x < DUNGEON_MAX_WIDTH; x++) {
                floor[y][x] = NULL;
            }
        }

        discoveredTiles = new Tile[DUNGEON_MAX_HEIGHT][DUNGEON_MAX_WIDTH];

        items.clear();
        monsters.clear();
        entities.clear();
        initAStar();

        // Room initialization
        int row_rooms = 2 * (height / SCREEN_HEIGHT);
        int col_rooms = 2 * (width / SCREEN_WIDTH);

        boolean[][] room_map = new boolean[20][20];

        int random_room_count = RNG.get().randomNumberNormalDistribution(DUNGEON_ROOMS_MEAN, 2);
        for (int i = 0; i < random_room_count; i++) {
            room_map[RNG.get().randomNumber(row_rooms) - 1][RNG.get().randomNumber(col_rooms) - 1] = true;
        }

        // Build rooms
        logger.info("Building " + random_room_count + " rooms...");
        ArrayList<Point> locations = new ArrayList<>();

        for (int row = 0; row < row_rooms; row++) {
            for (int col = 0; col < col_rooms; col++) {
                if (room_map[row][col]) {
                    Point location = new Point();
                    location.y = (row * (SCREEN_HEIGHT >> 1) + QUART_HEIGHT);
                    location.x = (col * (SCREEN_WIDTH >> 1) + QUART_WIDTH);

                    if (level + RNG.get().randomNumberNormalDistribution(1, 2) > RNG.get().randomNumber(DUNGEON_UNUSUAL_ROOMS)) {
                        int room_type = RNG.get().randomNumber(3);

                        if (room_type == 1) {
                            buildRoomOverlappingRectangles(location);
                        } else if (room_type == 2) {
                            buildRoomWithInnerRooms(location);
                        } else {
                            buildRoomCrossShaped(location);
                        }
                    } else {
                        buildRoom(location);
                    }

                    locations.add(location);
                }
            }
        }

        for (int i = 0; i < locations.size(); i++) {
            int pick1 = RNG.get().randomNumber(locations.size()) - 1;
            int pick2 = RNG.get().randomNumber(locations.size()) - 1;

            Collections.swap(locations, pick1, pick2);
        }

        ArrayList<Point> wantedDoors = new ArrayList<>();
        for (int i = 0; i < locations.size() - 1; i++) {
            buildTunnel(wantedDoors, locations.get(i + 1), locations.get(i));
        }

        // Generate walls and streamers
        fillEmptyTilesWith(GRANITE_WALL);
        for (int i = 0; i < DUNGEON_MAGMA_STREAMER; i++) {
            placeStreamerRock(MAGMA_WALL, DUNGEON_MAGMA_TREASURE);
        }
        for (int i = 0; i < DUNGEON_QUARTZ_STREAMER; i++) {
            placeStreamerRock(QUARTZ_WALL, DUNGEON_QUARTZ_TREASURE);
        }

        placeBoundaryWalls();

        // Place intersection doors
        for (Point door : wantedDoors) {
            placeDoorIfNextToTwoWalls(new Point(door.x - 1, door.y));
            placeDoorIfNextToTwoWalls(new Point(door.x + 1, door.y));
            placeDoorIfNextToTwoWalls(new Point(door.x, door.y - 1));
            placeDoorIfNextToTwoWalls(new Point(door.x, door.y + 1));
        }

        Point upStairs = placeStairs(1, 0);
        spawnUpStairs = newSpotNear(upStairs);

        Point downStairs = placeStairs(2, 0);
        spawnDownStairs = newSpotNear(downStairs);

        cleaningGraniteWalls();
        cleaningDoors();

        // Set up the character coords, used by monsterPlaceNewWithinDistance, monsterPlaceWinning
        if (level == 1) {
            this.floor[upStairs.y][upStairs.x] = ROOM_FLOOR;
            entities.add(new FireCamp(new Point(upStairs)));
        }

        int allocLevel = (level / 3);
        if (allocLevel < 2) {
            allocLevel = 2;
        } else if (allocLevel > 10) {
            allocLevel = 10;
        }

        monsterPlaceNewWithinDistance(spawnUpStairs, (RNG.get().randomNumber(8) + MON_MIN_PER_LEVEL + allocLevel), 0, true);
        allocateAndPlaceObject(asList(CORRIDOR_FLOOR), RUBBLE, RNG.get().randomNumber(allocLevel));
        allocateAndPlaceObject(asList(ROOM_FLOOR), RANDOM,
                RNG.get().randomNumberNormalDistribution(LEVEL_OBJECTS_PER_ROOM, 3));
        allocateAndPlaceObject(asList(CORRIDOR_FLOOR, ROOM_FLOOR), RANDOM,
                RNG.get().randomNumberNormalDistribution(LEVEL_OBJECTS_PER_CORRIDOR, 3));
        allocateAndPlaceObject(asList(CORRIDOR_FLOOR, ROOM_FLOOR), GOLD,
                RNG.get().randomNumberNormalDistribution(LEVEL_TOTAL_GOLD_AND_GEMS, 3));
        allocateAndPlaceObject(asList(CORRIDOR_FLOOR, ROOM_FLOOR), TRAP, RNG.get().randomNumber(allocLevel));

        // Verify level eligibility
        if (spawnUpStairs == null || spawnDownStairs == null) {
            return false;
        }

        Path path = astar.findPath(spawnUpStairs.inverseXY(), downStairs.inverseXY(), 300, false, true);
        if (path == null || path.getLength() < 30) {
            return false;
        }

        if (level > 1) {
            path = astar.findPath(spawnDownStairs.inverseXY(), upStairs.inverseXY(), 300, false, true);
            if (path == null || path.getLength() < 30) {
                return false;
            }
        }

        return true;
    }

    // Fills in empty spots with desired rock
    private void fillEmptyTilesWith(Tile rock_type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (this.floor[y][x] == NULL || this.floor[y][x] == TMP1_WALL || this.floor[y][x] == TMP2_WALL) {
                    this.floor[y][x] = rock_type;
                }
            }
        }
    }

    // Builds a room at a row, column coordinate
    // Type 1 unusual rooms are several overlapping rectangular ones
    private void buildRoomOverlappingRectangles(Point coord) {
        logger.info("Building room overlapping rectangles at " + coord + "...");
        Tile floor = ROOM_FLOOR;

        int limit = 1 + RNG.get().randomNumber(2);

        for (int count = 0; count < limit; count++) {
            int height = coord.y - RNG.get().randomNumber(4);
            int depth = coord.y + RNG.get().randomNumber(3);
            int left = coord.x - RNG.get().randomNumber(11);
            int right = coord.x + RNG.get().randomNumber(11);

            // the x dim of rooms tends to be much larger than the y dim,
            // so don't bother rewriting the y loop.
            for (int y = height; y <= depth; y++) {
                for (int x = left; x <= right; x++) {
                    this.floor[y][x] = floor;
                }
            }

            for (int y = (height - 1); y <= (depth + 1); y++) {
                if (this.floor[y][left - 1] != floor) {
                    this.floor[y][left - 1] = GRANITE_WALL;
                }

                if (this.floor[y][right + 1] != floor) {
                    this.floor[y][right + 1] = GRANITE_WALL;
                }
            }

            for (int x = left; x <= right; x++) {
                if (this.floor[height - 1][x] != floor) {
                    this.floor[height - 1][x] = GRANITE_WALL;
                }

                if (this.floor[depth + 1][x] != floor) {
                    this.floor[depth + 1][x] = GRANITE_WALL;
                }
            }
        }
    }

    // Builds a type 2 unusual room at a row, column coordinate
    private void buildRoomWithInnerRooms(Point coord) {
        Tile floor = ROOM_FLOOR;

        int height = coord.y - 4;
        int depth = coord.y + 4;
        int left = coord.x - 11;
        int right = coord.x + 11;

        // the x dim of rooms tends to be much larger than the y dim,
        // so don't bother rewriting the y loop.
        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                this.floor[i][j] = floor;
            }
        }

        for (int i = (height - 1); i <= (depth + 1); i++) {
            this.floor[i][left - 1] = GRANITE_WALL;
            this.floor[i][right + 1] = GRANITE_WALL;
        }

        for (int i = left; i <= right; i++) {
            this.floor[height - 1][i] = GRANITE_WALL;
            this.floor[depth + 1][i] = GRANITE_WALL;
        }

        // The inner room
        height = height + 2;
        depth = depth - 2;
        left = left + 2;
        right = right - 2;

        for (int i = (height - 1); i <= (depth + 1); i++) {
            this.floor[i][left - 1] = TMP1_WALL;
            this.floor[i][right + 1] = TMP1_WALL;
        }

        for (int i = left; i <= right; i++) {
            this.floor[height - 1][i] = TMP1_WALL;
            this.floor[depth + 1][i] = TMP1_WALL;
        }

        // Inner room variations
        switch (InnerRoom.random()) {
        case PLAIN:
            logger.info("Building room with inner rooms (plain) at " + coord + "...");
            eraseInnerRoom(depth, height, left, right);
            placeVaultMonster(coord, 1);
            break;
        case TREASURE_VAULT:
            logger.info("Building room with inner rooms (treasure vault) at " + coord + "...");
            placeTreasureVault(coord, depth, height, left, right);

            // Guard the treasure well
            placeVaultMonster(coord, 2 + RNG.get().randomNumber(3));

            // If the monsters don't get 'em.
            placeVaultTrap(coord, new Point(10, 4), 2 + RNG.get().randomNumber(3));
            break;
        case PILLARS:
            logger.info("Building room with inner rooms (pillars) at " + coord + "...");
            eraseInnerRoom(depth, height, left, right);

            placeInnerPillars(coord);

            if (RNG.get().randomNumber(3) != 1) {
                break;
            }

            // Inner rooms
            for (int i = coord.x - 5; i <= coord.x + 5; i++) {
                this.floor[coord.y - 1][i] = TMP1_WALL;
                this.floor[coord.y + 1][i] = TMP1_WALL;
            }
            this.floor[coord.y][coord.x - 5] = TMP1_WALL;
            this.floor[coord.y][coord.x + 5] = TMP1_WALL;

            placeSecretDoor(new Point(coord.x - 3, coord.y - 3 + (RNG.get().randomNumber(2) << 1)));
            placeSecretDoor(new Point(coord.x + 3, coord.y - 3 + (RNG.get().randomNumber(2) << 1)));

            if (RNG.get().randomNumber(3) == 1) {
                placeRandomObjectAt(new Point(coord.x - 2, coord.y));
            }

            if (RNG.get().randomNumber(3) == 1) {
                placeRandomObjectAt(new Point(coord.x + 2, coord.y));
            }

            placeVaultMonster(new Point(coord.x - 2, coord.y), RNG.get().randomNumber(2));
            placeVaultMonster(new Point(coord.x + 2, coord.y), RNG.get().randomNumber(2));
            break;
        case MAZE:
            logger.info("Building room with inner rooms (maze) at " + coord + "...");
            eraseInnerRoom(depth, height, left, right);

            placeMazeInsideRoom(depth, height, left, right);

            // Monsters just love mazes.
            placeVaultMonster(new Point(coord.x - 6, coord.y), RNG.get().randomNumber(3));
            placeVaultMonster(new Point(coord.x + 6, coord.y), RNG.get().randomNumber(3));

            // Traps make them entertaining.
            placeVaultTrap(new Point(coord.x - 2, coord.y), new Point(8, 2), RNG.get().randomNumber(3));
            placeVaultTrap(new Point(coord.x + 2, coord.y), new Point(8, 2), RNG.get().randomNumber(3));

            // Mazes should have some treasure too..
            for (int i = 0; i < 3; i++) {
                placeRandomObjectNear(coord, 1);
            }
            break;
        case FOUR_SMALL_ROOMS:
            logger.info("Building room with inner rooms (four small rooms) at " + coord + "...");
            placeFourSmallRooms(coord, depth, height, left, right);

            // Treasure in each one.
            placeRandomObjectNear(coord, 2 + RNG.get().randomNumber(2));

            // Gotta have some monsters.
            placeVaultMonster(new Point(coord.x - 4, coord.y + 2), RNG.get().randomNumber(2));
            placeVaultMonster(new Point(coord.x + 4, coord.y + 2), RNG.get().randomNumber(2));
            placeVaultMonster(new Point(coord.x - 4, coord.y - 2), RNG.get().randomNumber(2));
            placeVaultMonster(new Point(coord.x + 4, coord.y - 2), RNG.get().randomNumber(2));
            break;
        }
    }

    // Builds a room at a row, column coordinate
    // Type 3 unusual rooms are cross shaped
    private void buildRoomCrossShaped(Point coord) {
        Tile floor = ROOM_FLOOR;

        int random_offset = 2 + RNG.get().randomNumber(2);

        int height = coord.y - random_offset;
        int depth = coord.y + random_offset;
        int left = coord.x - 1;
        int right = coord.x + 1;

        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                this.floor[i][j] = floor;
            }
        }

        for (int i = height - 1; i <= depth + 1; i++) {
            this.floor[i][left - 1] = GRANITE_WALL;
            this.floor[i][right + 1] = GRANITE_WALL;
        }

        for (int i = left; i <= right; i++) {
            this.floor[height - 1][i] = GRANITE_WALL;
            this.floor[depth + 1][i] = GRANITE_WALL;
        }

        random_offset = 2 + RNG.get().randomNumber(9);

        height = coord.y - 1;
        depth = coord.y + 1;
        left = coord.x - random_offset;
        right = coord.x + random_offset;

        for (int i = height; i <= depth; i++) {
            for (int j = left; j <= right; j++) {
                this.floor[i][j] = floor;
            }
        }

        for (int i = height - 1; i <= depth + 1; i++) {
            if (this.floor[i][left - 1] != floor) {
                this.floor[i][left - 1] = GRANITE_WALL;
            }

            if (this.floor[i][right + 1] != floor) {
                this.floor[i][right + 1] = GRANITE_WALL;
            }
        }

        for (int i = left; i <= right; i++) {
            if (this.floor[height - 1][i] != floor) {
                this.floor[height - 1][i] = GRANITE_WALL;
            }

            if (this.floor[depth + 1][i] != floor) {
                this.floor[depth + 1][i] = GRANITE_WALL;
            }
        }

        // Special features.
        switch (RNG.get().randomNumber(4)) {
        case 1: // Large middle pillar
            logger.info("Building room cross shaped (large middle pillar) at " + coord + "...");
            placeLargeMiddlePillar(coord);
            break;
        case 2: // Inner treasure vault
            logger.info("Building room cross shaped (treasure vault) at " + coord + "...");
            placeVault(coord);

            // Place a secret door
            random_offset = RNG.get().randomNumber(4);
            if (random_offset < 3) {
                placeSecretDoor(new Point(coord.x, coord.y - 3 + (random_offset << 1)));
            } else {
                placeSecretDoor(new Point(coord.x - 7 + (random_offset << 1), coord.y));
            }

            // Place a treasure in the vault
            placeRandomObjectAt(coord);

            // Let's guard the treasure well.
            placeVaultMonster(coord, 2 + RNG.get().randomNumber(2));

            // Traps naturally
            placeVaultTrap(coord, new Point(4, 4), 1 + RNG.get().randomNumber(3));
            break;
        case 3:
            logger.info("Building room cross shaped at " + coord + "...");
            if (RNG.get().randomNumber(3) == 1) {
                entities.add(new Pillar(new Point(coord.x - 2, coord.y - 1)));
                entities.add(new Pillar(new Point(coord.x - 2, coord.y + 1)));
                entities.add(new Pillar(new Point(coord.x + 2, coord.y - 1)));
                entities.add(new Pillar(new Point(coord.x + 2, coord.y + 1)));
                entities.add(new Pillar(new Point(coord.x - 1, coord.y - 2)));
                entities.add(new Pillar(new Point(coord.x + 1, coord.y - 2)));
                entities.add(new Pillar(new Point(coord.x - 1, coord.y + 2)));
                entities.add(new Pillar(new Point(coord.x + 1, coord.y + 2)));
            } else if (RNG.get().randomNumber(3) == 1) {
                this.floor[coord.y][coord.x] = TMP1_WALL;
                this.floor[coord.y - 1][coord.x] = TMP1_WALL;
                this.floor[coord.y + 1][coord.x] = TMP1_WALL;
                this.floor[coord.y][coord.x - 1] = TMP1_WALL;
                this.floor[coord.y][coord.x + 1] = TMP1_WALL;
            } else if (RNG.get().randomNumber(3) == 1) {
                this.floor[coord.y][coord.x] = TMP1_WALL;
            }
            break;
        }
    }

    // Builds a room at a row, column coordinate
    private void buildRoom(Point coord) {
        logger.info("Building room at " + coord + "...");
        Tile floor = ROOM_FLOOR;

        int height = coord.y - RNG.get().randomNumber(4);
        int depth = coord.y + RNG.get().randomNumber(3);
        int left = coord.x - RNG.get().randomNumber(11);
        int right = coord.x + RNG.get().randomNumber(11);

        // the x dim of rooms tends to be much larger than the y dim,
        // so don't bother rewriting the y loop.
        for (int y = height; y <= depth; y++) {
            for (int x = left; x <= right; x++) {
                this.floor[y][x] = floor;
            }
        }

        for (int y = height - 1; y <= depth + 1; y++) {
            this.floor[y][left - 1] = GRANITE_WALL;
            this.floor[y][right + 1] = GRANITE_WALL;
        }

        for (int x = left; x <= right; x++) {
            this.floor[height - 1][x] = GRANITE_WALL;
            this.floor[depth + 1][x] = GRANITE_WALL;
        }
    }

    // Constructs a tunnel between two points
    private void buildTunnel(ArrayList<Point> wantedDoors, Point start, Point end) {
        logger.info("Building tunnel between " + start + " and " + end + "...");

        ArrayList<Point> tunnels = new ArrayList<>();
        ArrayList<Point> walls = new ArrayList<>();

        // Main procedure for Tunnel
        boolean door_flag = false;
        boolean stop_flag = false;
        int main_loop_count = 0;
        int start_row = start.y;
        int start_col = start.x;

        Point direction = new Point();
        pickCorrectDirection(direction, start, end);

        do {
            // prevent infinite loops, just in case
            main_loop_count++;
            if (main_loop_count > 2000) {
                stop_flag = true;
            }

            if (RNG.get().randomNumber(100) > DUNGEON_DIR_CHANGE) {
                if (RNG.get().randomNumber(DUNGEON_RANDOM_DIR) == 1) {
                    chanceOfRandomDirection(direction);
                } else {
                    pickCorrectDirection(direction, start, end);
                }
            }

            int tmp_row = start.y + direction.y;
            int tmp_col = start.x + direction.x;

            while (!coordInBounds(new Point(tmp_col, tmp_row))) {
                if (RNG.get().randomNumber(DUNGEON_RANDOM_DIR) == 1) {
                    chanceOfRandomDirection(direction);
                } else {
                    pickCorrectDirection(direction, start, end);
                }
                tmp_row = start.y + direction.y;
                tmp_col = start.x + direction.x;
            }

            switch (this.floor[tmp_row][tmp_col]) {
            case NULL:
                start.y = tmp_row;
                start.x = tmp_col;
                if (tunnels.size() < 1000) {
                    tunnels.add(new Point(start.x, start.y));
                }
                door_flag = false;
                break;
            case TMP2_WALL:
                // do nothing
                break;
            case GRANITE_WALL:
                start.y = tmp_row;
                start.x = tmp_col;

                if (walls.size() < 1000) {
                    walls.add(new Point(start.x, start.y));
                }

                for (int y = start.y - 1; y <= start.y + 1; y++) {
                    for (int x = start.x - 1; x <= start.x + 1; x++) {
                        if (coordInBounds(new Point(x, y))) {
                            // values 11 and 12 are impossible here, placeStreamerRock is never run before buildTunnel
                            if (this.floor[y][x] == GRANITE_WALL) {
                                this.floor[y][x] = TMP2_WALL;
                            }
                        }
                    }
                }
                break;
            case CORRIDOR_FLOOR:
                start.y = tmp_row;
                start.x = tmp_col;

                if (!door_flag) {
                    if (wantedDoors.size() < 100) {
                        wantedDoors.add(new Point(start.x, start.y));
                    }
                    door_flag = true;
                }

                if (RNG.get().randomNumber(100) > DUNGEON_TUNNELING) {
                    // make sure that tunnel has gone a reasonable distance before stopping it, this helps prevent isolated rooms
                    tmp_row = start.y - start_row;
                    if (tmp_row < 0) {
                        tmp_row = -tmp_row;
                    }

                    tmp_col = start.x - start_col;
                    if (tmp_col < 0) {
                        tmp_col = -tmp_col;
                    }

                    if (tmp_row > 10 || tmp_col > 10) {
                        stop_flag = true;
                    }
                }
                break;
            default:
                // none of: NULL, TMP2, GRANITE, CORR
                start.y = tmp_row;
                start.x = tmp_col;
            }
        } while ((start.y != end.y || start.x != end.x) && !stop_flag);

        for (Point tunnel : tunnels) {
            this.floor[tunnel.y][tunnel.x] = CORRIDOR_FLOOR;
        }

        for (Point wall : walls) {
            if (this.floor[wall.y][wall.x] == TMP2_WALL) {
                if (RNG.get().randomNumber(100) < DUNGEON_ROOM_DOORS) {
                    Direction doorDirection = isNextTo(wall);
                    if (doorDirection != null) {
                        placeDoor(wall, doorDirection);
                    }
                } else {
                    // these have to be doorways to rooms
                    this.floor[wall.y][wall.x] = CORRIDOR_FLOOR;
                }
            }
        }
    }

    void placeInnerPillars(Point coord) {
        logger.info("\t-> Placing inner pillars at " + coord + "...");
        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 1; x <= coord.x + 1; x++) {
                this.floor[y][x] = GRANITE_WALL;
            }
        }

        if (RNG.get().randomNumber(2) != 1) {
            return;
        }

        int offset = RNG.get().randomNumber(2);

        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 5 - offset; x <= coord.x - 3 - offset; x++) {
                entities.add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
            }
        }

        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x + 3 + offset; x <= coord.x + 5 + offset; x++) {
                entities.add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
            }
        }
    }

    private void placeLargeMiddlePillar(Point coord) {
        logger.info("\t-> Placing large middle pillar at " + coord + "...");

        entities.add(new Pillar(new Point(coord.x, coord.y - 1)));
        entities.add(new Pillar(new Point(coord.x, coord.y + 1)));
        floor[coord.y][coord.x] = MAGMA_WALL;
        entities.add(new Pillar(new Point(coord.x + 1, coord.y)));
        entities.add(new Pillar(new Point(coord.x - 1, coord.y)));
    }

    private void placeDoor(Point coord, Direction direction) {
        int doorType = RNG.get().randomNumber(3);

        if (doorType == 1) {
            placeOpenDoor(coord, direction);
        } else if (doorType == 2) {
            doorType = RNG.get().randomNumber(10);

            if (doorType == 1) {
                placeStuckDoor(coord, direction);
            } else {
                placeLockedDoor(coord, direction);
            }
        } else {
            placeSecretDoor(coord, direction);
        }
    }

    private void placeOpenDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing open door at " + coord + "...");
        floor[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.OPEN, direction));
    }

    private void placeLockedDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing locked door at " + coord + "...");
        this.floor[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.LOCKED, direction));
    }

    private void placeStuckDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing stuck door at " + coord + "...");
        this.floor[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.STUCK, direction));
    }

    private void placeSecretDoor(Point coord) {
        Direction direction = isNextTo(coord);
        if (direction != null) {
            placeSecretDoor(coord, direction);
        }
    }

    private void placeSecretDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing secret door at " + coord + "...");
        this.floor[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.SECRET, direction));
    }

    private void placeRandomSecretDoor(Point coord, int depth, int height, int left, int right) {
        switch (RNG.get().randomNumber(4)) {
        case 1:
            placeSecretDoor(new Point(coord.x, height - 1));
            break;
        case 2:
            placeSecretDoor(new Point(coord.x, depth + 1));
            break;
        case 3:
            placeSecretDoor(new Point(left - 1, coord.y));
            break;
        case 4:
            placeSecretDoor(new Point(right + 1, coord.y));
            break;
        }
    }

    private void eraseInnerRoom(int depth, int height, int left, int right) {
        for (int i = (height - 1); i <= (depth + 1); i++) {
            this.floor[i][left - 1] = ROOM_FLOOR;
            this.floor[i][right + 1] = ROOM_FLOOR;
        }

        for (int i = left; i <= right; i++) {
            this.floor[height - 1][i] = ROOM_FLOOR;
            this.floor[depth + 1][i] = ROOM_FLOOR;
        }
    }

    private void placeMazeInsideRoom(int depth, int height, int left, int right) {
        for (int y = height; y <= depth; y += 2) {
            for (int x = left; x <= right; x += 2) {
                if ((0x1 & (x + y)) != 0) {
                    entities.add(RNG.get().randomNumber(4) == 1 ? new Rubble(new Point(x, y)) : new Pillar(new Point(x, y)));
                }
            }
        }
    }

    private void placeFourSmallRooms(Point coord, int depth, int height, int left, int right) {
        for (int y = height; y <= depth; y++) {
            this.floor[y][coord.x] = TMP1_WALL;
        }

        for (int x = left; x <= right; x++) {
            this.floor[coord.y][x] = TMP1_WALL;
        }

        // place random secret door
        if (RNG.get().randomNumber(2) == 1) {
            int offset = RNG.get().randomNumber(10);
            placeSecretDoor(new Point(coord.x - offset, height - 1));
            placeSecretDoor(new Point(coord.x + offset, height - 1));
            placeSecretDoor(new Point(coord.x - offset, depth + 1));
            placeSecretDoor(new Point(coord.x + offset, depth + 1));
        } else {
            int offset = RNG.get().randomNumber(3);
            placeSecretDoor(new Point(left - 1, coord.y + offset));
            placeSecretDoor(new Point(left - 1, coord.y - offset));
            placeSecretDoor(new Point(right + 1, coord.y + offset));
            placeSecretDoor(new Point(right + 1, coord.y - offset));
        }
    }

    // Places "streamers" of rock through dungeon
    private void placeStreamerRock(Tile rock_type, int chance_of_treasure) {
        logger.info("Placing streamer of rock type " + rock_type + "...");

        // Choose starting point and direction
        Point coord = new Point(
                (this.width / 2) + 16 - RNG.get().randomNumber(33),
                (this.height) + 11 - RNG.get().randomNumber(23));

        // Get random direction
        Direction direction = Direction.random();

        // Place streamer into dungeon
        int t1 = 2 * DUNGEON_STREAMER_WIDTH + 1; // Constants
        int t2 = DUNGEON_STREAMER_WIDTH + 1;

        do {
            for (int i = 0; i < DUNGEON_STREAMER_DENSITY; i++) {
                Point spot = new Point(
                        coord.x + RNG.get().randomNumber(t1) - t2,
                        coord.y + RNG.get().randomNumber(t1) - t2);

                if (coordInBounds(spot)) {
                    if (this.floor[spot.y][spot.x] == GRANITE_WALL) {
                        this.floor[spot.y][spot.x] = rock_type;

                        if (RNG.get().randomNumber(chance_of_treasure) == 1) {
                            placeGoldNear(coord, 1);
                        }
                    }
                }
            }
        } while (moveIntoDirection(direction, coord));
    }

    // Places a staircase 1=up, 2=down
    private Point placeStairs(int stairType, int walls) {
        logger.info("Placing stairs of type " + stairType + "...");

        Point coord1 = new Point();
        Point coord2 = new Point();

        do {
            // Note:
            // don't let y1/x1 be zero,
            // don't let y2/x2 be equal to dg.height-1/dg.width-1,
            // these values are always BOUNDARY_ROCK.
            coord1.y = RNG.get().randomNumber(this.height - 14);
            coord1.x = RNG.get().randomNumber(this.width - 14);
            coord2.y = coord1.y + 12;
            coord2.x = coord1.x + 12;

            do {
                do {
                    if (!this.floor[coord1.y][coord1.x].isWall && coordWallsNextTo(coord1, true) == walls) {
                        if (stairType == 1) {
                            placeUpStairs(coord1);
                        } else {
                            placeDownStairs(coord1);
                        }

                        return coord1;
                    }
                    coord1.x++;
                } while (coord1.x != coord2.x);

                coord1.x = coord2.x - 12;
                coord1.y++;
            } while (coord1.y != coord2.y);
        } while (true);
    }

    // Place an up staircase at given y, x
    private void placeUpStairs(Point coord) {
        logger.info("\t-> Placing up stairs at " + coord + "...");
        this.floor[coord.y][coord.x] = UP_STAIR;
    }

    // Place a down staircase at given y, x
    private void placeDownStairs(Point coord) {
        logger.info("\t-> Placing down stairs at " + coord + "...");
        this.floor[coord.y][coord.x] = DOWN_STAIR;
    }

    // Places rubble at location y, x
    private void placeRubble(Point coord) {
        logger.info("\t-> Placing rubble at " + coord + "...");
        entities.add(new Rubble(new Point(coord.x, coord.y)));
    }

    // Places door at y, x position if at least 2 walls found
    private void placeDoorIfNextToTwoWalls(Point coord) {
        if (this.floor[coord.y][coord.x] == CORRIDOR_FLOOR && RNG.get().randomNumber(100) > DUNGEON_TUNNEL_DOORS) {
            Direction direction = isNextTo(coord);
            if (direction != null) {
                placeDoor(coord, direction);
            }
        }
    }

    private void placeVault(Point coord) {
        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            this.floor[y][coord.x - 1] = TMP1_WALL;
            this.floor[y][coord.x + 1] = TMP1_WALL;
        }

        this.floor[coord.y - 1][coord.x] = TMP1_WALL;
        this.floor[coord.y + 1][coord.x] = TMP1_WALL;
    }

    private void placeTreasureVault(Point coord, int depth, int height, int left, int right) {
        logger.info("\t-> Placing treasure vault at " + coord + "...");
        placeRandomSecretDoor(coord, depth, height, left, right);
        placeVault(coord);

        // Place a locked door
        int offset = RNG.get().randomNumber(4);
        if (offset < 3) {
            // 1 -> y-1; 2 -> y+1
            placeLockedDoor(new Point(coord.x, coord.y - 3 + (offset << 1)), WEST);
        } else {
            placeLockedDoor(new Point(coord.x - 7 + (offset << 1), coord.y), NORTH);
        }
    }

    // Place a trap with a given displacement of point
    private void placeVaultMonster(Point coord, int number) {
        logger.info("\t-> Placing vault monster at " + coord + "...");
        Point spot = new Point();

        for (int i = 0; i < number; i++) {
            spot.y = coord.y;
            spot.x = coord.x;
            monsterSummon(spot, true);
        }
    }

    // Place a trap with a given displacement of point
    private void placeVaultTrap(Point coord, Point displacement, int number) {
        logger.info("\t-> Placing vault trap at " + coord + "...");
        Point spot = new Point();

        for (int i = 0; i < number; i++) {
            boolean placed = false;

            for (int count = 0; !placed && count <= 5; count++) {
                spot.y = coord.y - displacement.y - 1 + RNG.get().randomNumber(2 * displacement.y + 1);
                spot.x = coord.x - displacement.x - 1 + RNG.get().randomNumber(2 * displacement.x + 1);

                if (this.floor[spot.y][spot.x] != NULL && this.floor[spot.y][spot.x].isFloor && !isItemAt(spot)) {
                    // TODO
                    // setTrap(spot, RNG.get().randomNumber(MAX_TRAPS) - 1);
                    placed = true;
                }
            }
        }
    }

    // Checks points north, south, east, and west for a wall
    // note that y,x is always coordInBounds(), i.e. 0 < y < dg.height-1,
    // and 0 < x < dg.width-1
    private int coordWallsNextTo(Point coord, boolean allowDiag) {
        int walls = 0;

        if (this.floor[coord.y - 1][coord.x].isWall) {
            walls++;
        }

        if (this.floor[coord.y + 1][coord.x].isWall) {
            walls++;
        }

        if (this.floor[coord.y][coord.x - 1].isWall) {
            walls++;
        }

        if (this.floor[coord.y][coord.x + 1].isWall) {
            walls++;
        }

        if (allowDiag) {
            if (this.floor[coord.y - 1][coord.x - 1].isWall) {
                walls++;
            }

            if (this.floor[coord.y + 1][coord.x + 1].isWall) {
                walls++;
            }

            if (this.floor[coord.y + 1][coord.x - 1].isWall) {
                walls++;
            }

            if (this.floor[coord.y - 1][coord.x + 1].isWall) {
                walls++;
            }
        }

        return walls;
    }

    public boolean coordInBounds(Point coord) {
        boolean y = coord.y > 0 && coord.y < this.height - 1;
        boolean x = coord.x > 0 && coord.x < this.width - 1;

        return y && x;
    }

    private Direction isNextTo(Point coord) {
        if (coordCorridorWallsNextTo(coord) > 2) {
            boolean north = this.floor[coord.y - 1][coord.x].isWall;
            boolean south = this.floor[coord.y + 1][coord.x].isWall;
            boolean west = this.floor[coord.y][coord.x - 1].isWall;
            boolean east = this.floor[coord.y][coord.x + 1].isWall;

            boolean vertical = north && south;
            boolean horizontal = west && east;

            if (vertical && !west && !east) {
                return NORTH;
            } else if (horizontal && !north && !south) {
                return WEST;
            }
        }

        return null;
    }

    private boolean moveIntoDirection(Direction direction, Point coord) {
        Point new_coord = new Point();

        switch (direction) {
        case SOUTHWEST:
            new_coord.y = coord.y + 1;
            new_coord.x = coord.x - 1;
            break;
        case SOUTH:
            new_coord.y = coord.y + 1;
            new_coord.x = coord.x;
            break;
        case SOUTHEAST:
            new_coord.y = coord.y + 1;
            new_coord.x = coord.x + 1;
            break;
        case WEST:
            new_coord.y = coord.y;
            new_coord.x = coord.x - 1;
            break;
        case EAST:
            new_coord.y = coord.y;
            new_coord.x = coord.x + 1;
            break;
        case NORTHWEST:
            new_coord.y = coord.y - 1;
            new_coord.x = coord.x - 1;
            break;
        case NORTH:
            new_coord.y = coord.y - 1;
            new_coord.x = coord.x;
            break;
        case NORTHEAST:
            new_coord.y = coord.y - 1;
            new_coord.x = coord.x + 1;
            break;
        }

        boolean can_move = false;

        if (new_coord.y >= 0 && new_coord.y < this.height && new_coord.x >= 0 && new_coord.x < this.width) {
            coord.x = new_coord.x;
            coord.y = new_coord.y;
            can_move = true;
        }

        return can_move;
    }

    // Always picks a correct direction
    private void pickCorrectDirection(Point direction, Point start, Point end) {
        if (start.y < end.y) {
            direction.y = 1;
        } else if (start.y == end.y) {
            direction.y = 0;
        } else {
            direction.y = -1;
        }

        if (start.x < end.x) {
            direction.x = 1;
        } else if (start.x == end.x) {
            direction.x = 0;
        } else {
            direction.x = -1;
        }

        if (direction.y != 0 && direction.x != 0) {
            if (RNG.get().randomNumber(2) == 1) {
                direction.y = 0;
            } else {
                direction.x = 0;
            }
        }
    }

    // Chance of wandering direction
    private void chanceOfRandomDirection(Point directionPoint) {
        int direction = RNG.get().randomNumber(4);

        if (direction < 3) {
            directionPoint.x = 0;
            directionPoint.y = -3 + (direction << 1); // direction=1 -> y=-1; direction=2 -> y=1
        } else {
            directionPoint.y = 0;
            directionPoint.x = -7 + (direction << 1); // direction=3 -> x=-1; direction=4 -> x=1
        }
    }

    // Checks all adjacent spots for corridors
    // note that y, x is always coordInBounds(), hence no need to check that
    // j, k are coordInBounds(), even if they are 0 or cur_x-1 is still works
    private int coordCorridorWallsNextTo(Point coord) {
        int walls = 0;

        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 1; x <= coord.x + 1; x++) {
                // should fail if there is already a door present
                if (getDoorAt(new Point(x, y)) == null) {
                    walls++;
                }
            }
        }

        return walls;
    }

    // Places indestructible rock around edges of dungeon
    private void placeBoundaryWalls() {
        logger.info("Placing boundary walls...");

        // put permanent wall on leftmost row and rightmost row
        for (int i = 0; i < this.height; i++) {
            this.floor[i][0] = GRANITE_WALL;
            this.floor[i][this.width - 1] = GRANITE_WALL;
        }

        // put permanent wall on top row and bottom row
        for (int i = 0; i < this.width; i++) {
            this.floor[0][i] = GRANITE_WALL;
            this.floor[this.height - 1][i] = GRANITE_WALL;
        }
    }

    private void cleaningGraniteWalls() {
        logger.info("Cleaning granite walls...");

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.floor[i][j] == GRANITE_WALL && !isVisibleFromFloor(i, j)) {
                    this.floor[i][j] = NULL;
                }
            }
        }
    }

    private void cleaningDoors() {
        logger.info("Cleaning doors...");

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Door door = getDoorAt(new Point(j, i));
                if (door != null && !this.floor[i][j].isWall) {
                    entities.remove(door);
                }
            }
        }
    }

    // Places an object at given row, column co-ordinate
    private void placeRandomObjectAt(Point coord) {
        logger.info("\t-> Placing random object at " + coord + "...");
        int qualityLevel = level * 2 + RNG.get().randomNumberNormalDistribution(0, 1);

        AbstractItem item = ItemGenerator.generateItem(level, qualityLevel);
        item.setPosition(coord);

        items.add(item);
    }

    // Creates objects nearby the coordinates given
    private void placeRandomObjectNear(Point coord, int tries) {
        do {
            for (int i = 0; i <= 10; i++) {
                Point at = new Point(coord.x - 4 + RNG.get().randomNumber(7), coord.y - 3 + RNG.get().randomNumber(5));

                if (coordInBounds(at) && this.floor[at.y][at.x].isFloor && !isItemAt(at)) {
                    if (RNG.get().randomNumber(100) < 75) {
                        placeRandomObjectAt(at);
                    } else {
                        placeGold(at);
                    }
                    i = 9;
                }
            }

            tries--;
        } while (tries != 0);
    }

    // Places a treasure (Gold or Gems) nearby the coordinates given
    private void placeGoldNear(Point coord, int tries) {
        do {
            for (int i = 0; i <= 10; i++) {
                Point at = new Point(coord.x - 4 + RNG.get().randomNumber(7), coord.y - 3 + RNG.get().randomNumber(5));

                if (coordInBounds(at) && this.floor[at.y][at.x].isFloor && !isItemAt(at)) {
                    placeGold(at);
                    i = 9;
                }
            }

            tries--;
        } while (tries != 0);
    }

    // Places a treasure (Gold or Gems) at given row, column
    private void placeGold(Point coord) {
        logger.info("\t-> Placing gold at " + coord + "...");
        int qualityLevel = ((RNG.get().randomNumber(this.level + 2) + 2) / 2) - 1;

        if (RNG.get().randomNumber(TREASURE_CHANCE_OF_GREAT_ITEM) == 1) {
            qualityLevel += RNG.get().randomNumber(this.level + 1);
        }
        qualityLevel = max(qualityLevel, 1);
        qualityLevel = min(qualityLevel, MAX_GOLD_TYPES - 1);

        Gold item = new Gold();
        item.setPosition(coord);
        item.generateBase(qualityLevel);

        items.add(item);
    }

    // Allocates an object for tunnels and rooms
    private void allocateAndPlaceObject(List<Tile> types, PlacedObject type, int number) {
        logger.info("Placing " + number + " objects of type " + type + "...");
        Point coord = new Point();

        for (int i = 0; i < number; i++) {
            // don't put an object beneath the player, this could cause
            // problems if player is standing under rubble, or on a trap.
            do {
                coord.y = RNG.get().randomNumber(height) - 1;
                coord.x = RNG.get().randomNumber(width) - 1;
            } while (!types.contains(this.floor[coord.y][coord.x]) || spawnUpStairs.is(coord) || spawnDownStairs.is(coord)
                    || isItemAt(coord));

            switch (type) {
            case TRAP:
                // TODO
                // setTrap(coord, RNG.get().randomNumber(ObjectsConfig.MAX_TRAPS) - 1);
                break;
            case RUBBLE:
                placeRubble(coord);
                break;
            case GOLD:
                placeGold(coord);
                break;
            case RANDOM:
                placeRandomObjectAt(coord);
                break;
            }
        }
    }

    // Allocates a random monster
    private void monsterPlaceNewWithinDistance(Point playerPosition, int number, int distanceFromSource, boolean sleeping) {
        logger.info("Placing " + number + " monsters...");

        Point position = new Point();

        for (int i = 0; i < number; i++) {
            do {
                position.y = RNG.get().randomNumber(height - 2);
                position.x = RNG.get().randomNumber(width - 2);
            } while (!floor[position.y][position.x].isFloor || getMonsterAt(position) != null
                    || position.distanceFrom(playerPosition) <= distanceFromSource);

            MonsterRace race = monsterGetOneSuitableForLevel(level);
            monsterPlaceNew(position, race, sleeping);
        }
    }

    // Places creature adjacent to given location
    private boolean monsterSummon(Point coord, boolean sleeping) {
        MonsterRace race = monsterGetOneSuitableForLevel(level + MON_SUMMONED_LEVEL_ADJUST);
        return placeMonsterAdjacentTo(race, coord, sleeping);
    }

    private boolean placeMonsterAdjacentTo(MonsterRace race, Point coord, boolean sleeping) {
        boolean placed = false;

        Point position = new Point();

        for (int i = 0; i <= 9; i++) {
            position.y = coord.y - 2 + RNG.get().randomNumber(3);
            position.x = coord.x - 2 + RNG.get().randomNumber(3);

            if (coordInBounds(position)) {
                if (floor[position.y][position.x].isFloor && getMonsterAt(coord) == null) {
                    monsterPlaceNew(position, race, sleeping);

                    coord.y = position.y;
                    coord.x = position.x;

                    placed = true;
                    i = 9;
                }
            }
        }

        return placed;
    }

    // Return a monster suitable to be placed at a given level. This
    // makes high level monsters (up to the given level) slightly more
    // common than low level monsters at any given level.
    private MonsterRace monsterGetOneSuitableForLevel(int level) {
        if (RNG.get().randomNumber(MON_CHANCE_OF_NASTY) == 1) {
            int absDistribution = RNG.get().randomNumberNormalDistribution(0, 4);
            level += absDistribution + 1;
        }

        return Monster.pickMonsterRace(level);
    }

    // Places a monster at given location
    private void monsterPlaceNew(Point coord, MonsterRace race, boolean sleeping) {
        logger.info("\t-> Placing monster at " + coord + "...");

        Monster monster = new Monster(race);
        monster.setSleeping(sleeping);
        monster.setPosition(coord);

        monsters.add(monster);
    }

    private Point newSpotNear(Point coord) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    Point at = new Point(coord.x + i, coord.y + j);
                    if (coordInBounds(at) && this.floor[at.y][at.x].isFloor) {
                        return at;
                    }
                }
            }
        }

        return null;
    }

    private boolean isVisibleFromFloor(int x, int y) {
        List<Point> coords = asList(
                new Point(y, x - 1), //
                new Point(y, x + 1), //
                new Point(y - 1, x), //
                new Point(y + 1, x), //
                new Point(y - 1, x - 1), //
                new Point(y + 1, x + 1), //
                new Point(y - 1, x + 1), //
                new Point(y + 1, x - 1));

        for (Point coord : coords) {
            if (coordInBounds(coord) && (this.floor[coord.y][coord.x].isFloor || getDoorAt(coord) != null)) {
                return true;
            }
        }

        return false;
    }

    public void initAStar() {
        astar = new AStar(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Tile[][] getFloor() {
        return floor;
    }

    public void setFloor(Tile[][] floor) {
        this.floor = floor;
    }

    public Point getSpawnUpStairs() {
        return spawnUpStairs;
    }

    public Point getSpawnDownStairs() {
        return spawnDownStairs;
    }

    public List<AbstractItem> getItemsAt(Point coord) {
        return items.stream().filter(x -> x.getPosition().is(coord)).collect(toList());
    }

    public boolean isItemAt(Point coord) {
        return !getItemsAt(coord).isEmpty();
    }

    public void removeItem(AbstractItem item) {
        items.remove(item);
    }

    public Monster getMonsterAt(Point coord) {
        return monsters.stream().filter(x -> x.getPosition().is(coord) || x.getDestination().is(coord)).findFirst().orElse(null);
    }

    public ArrayList<AbstractItem> getItems() {
        return items;
    }

    public Door getDoorAt(Point coord) {
        return (Door) entities.stream().filter(x -> x.getType() == Entity.DOOR && x.getPosition().is(coord)).findFirst().orElse(null);
    }

    public AbstractEntity getEntityAt(Point coord) {
        return entities.stream().filter(x -> x.getPosition().is(coord)).findFirst().orElse(null);
    }

    public Path findPath(Point start, Point end, int maxSearchDistance) {
        return astar.findPath(start.inverseXY(), end.inverseXY(), maxSearchDistance, false, false);
    }

    public Path findPathNear(Point start, Point end, int maxSearchDistance) {
        List<Point> points = asList(
                new Point(end.x - 1, end.y), //
                new Point(end.x + 1, end.y), //
                new Point(end.x, end.y - 1), //
                new Point(end.x, end.y + 1));

        Path minPath = null;
        for (Point point : points) {
            if (coordInBounds(point)) {
                Path path = astar.findPath(start.inverseXY(), point.inverseXY(), maxSearchDistance, false, false);
                if (path != null && (minPath == null || path.getLength() < minPath.getLength())) {
                    minPath = path;
                }
            }
        }

        return minPath;
    }

    public Tile[][] getDiscoveredTiles() {
        return discoveredTiles;
    }

    public void setDiscoveredTiles(Tile[][] discoveredTiles) {
        this.discoveredTiles = discoveredTiles;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
    }

    public ArrayList<AbstractEntity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<AbstractEntity> entities) {
        this.entities = entities;
    }

    public void setItems(ArrayList<AbstractItem> items) {
        this.items = items;
    }

    public void setSpawnUpStairs(Point spawnUpStairs) {
        this.spawnUpStairs = spawnUpStairs;
    }

    public void setSpawnDownStairs(Point spawnDownStairs) {
        this.spawnDownStairs = spawnDownStairs;
    }

    public boolean isNotTraversable(Point coord, boolean allowObstacle) {
        if (allowObstacle) {
            return floor[coord.y][coord.x] == NULL || getEntityAt(coord) != null
                    || !(floor[coord.y][coord.x].isFloor || floor[coord.y][coord.x].isStairs || isOpenDoor(coord));
        }

        return floor[coord.y][coord.x] == NULL || getEntityAt(coord) != null
                || !(floor[coord.y][coord.x].isFloor || floor[coord.y][coord.x].isStairs)
                || getMonsterAt(coord) != null;
    }

    public boolean isOpenDoor(Point coord) {
        Door door = getDoorAt(coord);
        return door != null && door.getState() == DoorState.OPEN;
    }

    public boolean isVisibleDoor(Point point) {
        Door door = getDoorAt(point);
        return door != null && door.getState() != DoorState.SECRET;
    }

}
