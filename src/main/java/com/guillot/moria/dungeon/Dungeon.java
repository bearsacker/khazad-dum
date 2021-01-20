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
import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.MAGMA_WALL;
import static com.guillot.moria.dungeon.Tile.NULL;
import static com.guillot.moria.dungeon.Tile.QUARTZ_WALL;
import static com.guillot.moria.dungeon.Tile.ROOM_FLOOR;
import static com.guillot.moria.dungeon.Tile.TMP1_WALL;
import static com.guillot.moria.dungeon.Tile.TMP2_WALL;
import static com.guillot.moria.dungeon.entity.Direction.NORTH;
import static com.guillot.moria.dungeon.entity.Direction.WEST;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.guillot.moria.ai.AStar;
import com.guillot.moria.ai.Path;
import com.guillot.moria.character.Monster;
import com.guillot.moria.character.MonsterRace;
import com.guillot.moria.dungeon.entity.AbstractEntity;
import com.guillot.moria.dungeon.entity.Direction;
import com.guillot.moria.dungeon.entity.Door;
import com.guillot.moria.dungeon.entity.DoorState;
import com.guillot.moria.dungeon.entity.DownStairs;
import com.guillot.moria.dungeon.entity.Entity;
import com.guillot.moria.dungeon.entity.FireCamp;
import com.guillot.moria.dungeon.entity.Merchant;
import com.guillot.moria.dungeon.entity.Rubble;
import com.guillot.moria.dungeon.entity.UpStairs;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Gold;
import com.guillot.moria.item.ItemGenerator;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class Dungeon {

    private final static Logger logger = LogManager.getLogger(Dungeon.class);

    private int level;

    private int width;

    private int height;

    private Tile[][] tiles;

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
        tiles = new Tile[DUNGEON_MAX_HEIGHT][DUNGEON_MAX_WIDTH];
        for (int y = 0; y < DUNGEON_MAX_HEIGHT; y++) {
            for (int x = 0; x < DUNGEON_MAX_WIDTH; x++) {
                tiles[y][x] = NULL;
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
                tiles[y][x] = NULL;
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

        ArrayList<Point> locations = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>();
        int randomRoomCount = RNG.get().randomNumberNormalDistribution(DUNGEON_ROOMS_MEAN, 2);

        logger.info("Building " + randomRoomCount + " rooms...");
        for (int i = 0; i < randomRoomCount; i++) {
            Room room = null;
            Point position = new Point(RNG.get().randomNumber(row_rooms) - 1, RNG.get().randomNumber(col_rooms) - 1);
            position.y = (position.y * (SCREEN_HEIGHT >> 1) + QUART_HEIGHT);
            position.x = (position.x * (SCREEN_WIDTH >> 1) + QUART_WIDTH);

            RoomType type = RoomType.NORMAL;
            if (level + RNG.get().randomNumberNormalDistribution(1, 2) > RNG.get().randomNumber(DUNGEON_UNUSUAL_ROOMS)) {
                type = RoomType.randomSpecialRooms();
            }

            switch (type) {
            case CROSS_SHAPED:
                room = new CrossShapedRoom(this, position);
                break;
            case CROSS_SHAPED_PILLAR:
                room = new CrossShapedPillarRoom(this, position);
                break;
            case CROSS_SHAPED_TREASURE:
                room = new CrossShapedTreasureRoom(this, position);
                break;
            case INNER_ROOMS_FOUR_ROOMS:
                room = new InnerRoomsFourRoom(this, position);
                break;
            case INNER_ROOMS_MAZE:
                room = new InnerRoomsMazeRoom(this, position);
                break;
            case INNER_ROOMS_PILLARS:
                room = new InnerRoomsPillarsRoom(this, position);
                break;
            case INNER_ROOMS_PLAIN:
                room = new InnerRoomsPlainRoom(this, position);
                break;
            case INNER_ROOMS_TREASURE:
                room = new InnerRoomsTreasureRoom(this, position);
                break;
            case NORMAL:
                room = new EmptyRoom(this, position);
                break;
            case OVERLAPPING_RECTANGLES:
                room = new OverlappingRectanglesRoom(this, position);
                break;
            }

            locations.add(room.getPosition());
            rooms.add(room);
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
            tiles[upStairs.y][upStairs.x] = ROOM_FLOOR;
            entities.remove(getEntityAt(upStairs));
            entities.add(new FireCamp(new Point(upStairs)));
            entities.add(new Merchant(new Point(upStairs.x, upStairs.y + 1), 2, 0));
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

        // Place merchant
        boolean merchantPlaced = false;
        for (Room room : rooms) {
            if (tiles[room.getPosition().y][room.getPosition().x].isTraversable
                    && tiles[room.getPosition().y + 1][room.getPosition().x].isTraversable) {
                FireCamp firecamp = new FireCamp(room.getPosition());
                Merchant merchant = new Merchant(new Point(room.getPosition().x, room.getPosition().y + 1), level + 1);

                entities.add(firecamp);
                entities.add(merchant);

                if (astar.findPath(room.getPosition().inverseXY(), spawnDownStairs.inverseXY(), 300, false, true) != null) {
                    merchantPlaced = true;
                    break;
                } else {
                    entities.remove(firecamp);
                    entities.remove(merchant);
                }
            }
        }

        // Verify level eligibility
        if (spawnUpStairs == null || spawnDownStairs == null || !merchantPlaced) {
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
                if (tiles[y][x] == NULL || tiles[y][x] == TMP1_WALL || tiles[y][x] == TMP2_WALL) {
                    tiles[y][x] = rock_type;
                }
            }
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

            switch (tiles[tmp_row][tmp_col]) {
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
                            if (tiles[y][x] == GRANITE_WALL) {
                                tiles[y][x] = TMP2_WALL;
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
            tiles[tunnel.y][tunnel.x] = CORRIDOR_FLOOR;
        }

        for (Point wall : walls) {
            if (tiles[wall.y][wall.x] == TMP2_WALL) {
                if (RNG.get().randomNumber(100) < DUNGEON_ROOM_DOORS) {
                    Direction doorDirection = isNextTo(wall);
                    if (doorDirection != null) {
                        placeDoor(wall, doorDirection);
                    }
                } else {
                    // these have to be doorways to rooms
                    tiles[wall.y][wall.x] = CORRIDOR_FLOOR;
                }
            }
        }
    }

    public void placeDoor(Point coord, Direction direction) {
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

    public void placeOpenDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing open door at " + coord + "...");
        tiles[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.OPEN, direction));
    }

    public void placeLockedDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing locked door at " + coord + "...");
        tiles[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.LOCKED, direction));
    }

    public void placeStuckDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing stuck door at " + coord + "...");
        tiles[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.STUCK, direction));
    }

    public void placeSecretDoor(Point coord) {
        Direction direction = isNextTo(coord);
        if (direction != null) {
            placeSecretDoor(coord, direction);
        }
    }

    public void placeSecretDoor(Point coord, Direction direction) {
        logger.info("\t-> Placing secret door at " + coord + "...");
        tiles[coord.y][coord.x] = GRANITE_WALL;

        entities.add(new Door(coord, DoorState.SECRET, direction));
    }

    public void placeRandomSecretDoor(Point coord, int depth, int height, int left, int right) {
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

    // Places "streamers" of rock through dungeon
    public void placeStreamerRock(Tile rock_type, int chance_of_treasure) {
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
                    if (tiles[spot.y][spot.x] == GRANITE_WALL) {
                        tiles[spot.y][spot.x] = rock_type;

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
                    if (tiles[coord1.y][coord1.x].isTraversable && coordWallsNextTo(coord1, true) == walls) {
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
    public void placeUpStairs(Point coord) {
        logger.info("\t-> Placing up stairs at " + coord + "...");
        entities.add(new UpStairs(coord, level - 1));
    }

    // Place a down staircase at given y, x
    public void placeDownStairs(Point coord) {
        logger.info("\t-> Placing down stairs at " + coord + "...");
        entities.add(new DownStairs(coord, level + 1));
    }

    // Places rubble at location y, x
    public void placeRubble(Point coord) {
        logger.info("\t-> Placing rubble at " + coord + "...");
        entities.add(new Rubble(new Point(coord.x, coord.y)));
    }

    // Places door at y, x position if at least 2 walls found
    public void placeDoorIfNextToTwoWalls(Point coord) {
        if (tiles[coord.y][coord.x] == CORRIDOR_FLOOR && RNG.get().randomNumber(100) > DUNGEON_TUNNEL_DOORS) {
            Direction direction = isNextTo(coord);
            if (direction != null) {
                placeDoor(coord, direction);
            }
        }
    }

    public void placeVault(Point coord) {
        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            tiles[y][coord.x - 1] = TMP1_WALL;
            tiles[y][coord.x + 1] = TMP1_WALL;
        }

        tiles[coord.y - 1][coord.x] = TMP1_WALL;
        tiles[coord.y + 1][coord.x] = TMP1_WALL;
    }

    public void placeTreasureVault(Point coord, int depth, int height, int left, int right) {
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
    public void placeVaultMonster(Point coord, int number) {
        logger.info("\t-> Placing vault monster at " + coord + "...");
        Point spot = new Point();

        for (int i = 0; i < number; i++) {
            spot.y = coord.y;
            spot.x = coord.x;
            monsterSummon(spot, true);
        }
    }

    // Place a trap with a given displacement of point
    public void placeVaultTrap(Point coord, Point displacement, int number) {
        logger.info("\t-> Placing vault trap at " + coord + "...");
        Point spot = new Point();

        for (int i = 0; i < number; i++) {
            boolean placed = false;

            for (int count = 0; !placed && count <= 5; count++) {
                spot.y = coord.y - displacement.y - 1 + RNG.get().randomNumber(2 * displacement.y + 1);
                spot.x = coord.x - displacement.x - 1 + RNG.get().randomNumber(2 * displacement.x + 1);

                if (tiles[spot.y][spot.x] != NULL && tiles[spot.y][spot.x].isTraversable && !isItemAt(spot)) {
                    // TODO
                    // setTrap(spot, RNG.get().randomNumber(MAX_TRAPS) - 1);
                    placed = true;
                }
            }
        }
    }

    // Checks points north, south, east, and west for a wall
    // note that y,x is always coordInBounds(), i.e. 0 < y < height - 1,
    // and 0 < x < width - 1
    private int coordWallsNextTo(Point coord, boolean allowDiag) {
        int walls = 0;

        if (!tiles[coord.y - 1][coord.x].isTraversable) {
            walls++;
        }

        if (!tiles[coord.y + 1][coord.x].isTraversable) {
            walls++;
        }

        if (!tiles[coord.y][coord.x - 1].isTraversable) {
            walls++;
        }

        if (!tiles[coord.y][coord.x + 1].isTraversable) {
            walls++;
        }

        if (allowDiag) {
            if (!tiles[coord.y - 1][coord.x - 1].isTraversable) {
                walls++;
            }

            if (!tiles[coord.y + 1][coord.x + 1].isTraversable) {
                walls++;
            }

            if (!tiles[coord.y + 1][coord.x - 1].isTraversable) {
                walls++;
            }

            if (!tiles[coord.y - 1][coord.x + 1].isTraversable) {
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
            boolean north = !tiles[coord.y - 1][coord.x].isTraversable;
            boolean south = !tiles[coord.y + 1][coord.x].isTraversable;
            boolean west = !tiles[coord.y][coord.x - 1].isTraversable;
            boolean east = !tiles[coord.y][coord.x + 1].isTraversable;

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
    public void placeBoundaryWalls() {
        logger.info("Placing boundary walls...");

        // put permanent wall on leftmost row and rightmost row
        for (int i = 0; i < this.height; i++) {
            tiles[i][0] = GRANITE_WALL;
            tiles[i][this.width - 1] = GRANITE_WALL;
        }

        // put permanent wall on top row and bottom row
        for (int i = 0; i < this.width; i++) {
            tiles[0][i] = GRANITE_WALL;
            tiles[this.height - 1][i] = GRANITE_WALL;
        }
    }

    private void cleaningGraniteWalls() {
        logger.info("Cleaning granite walls...");

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (tiles[i][j] == GRANITE_WALL && !isVisibleFromFloor(i, j)) {
                    tiles[i][j] = NULL;
                }
            }
        }
    }

    private void cleaningDoors() {
        logger.info("Cleaning doors...");

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Door door = getDoorAt(new Point(j, i));
                if (door != null && tiles[i][j].isTraversable) {
                    entities.remove(door);
                }
            }
        }
    }

    // Places an object at given row, column co-ordinate
    public void placeRandomObjectAt(Point coord) {
        logger.info("\t-> Placing random object at " + coord + "...");
        int qualityLevel = level * 2 + RNG.get().randomNumberNormalDistribution(0, 1);

        AbstractItem item = ItemGenerator.generateItem(level, qualityLevel);
        item.setPosition(coord);

        items.add(item);
    }

    // Creates objects nearby the coordinates given
    public void placeRandomObjectNear(Point coord, int tries) {
        do {
            for (int i = 0; i <= 10; i++) {
                Point at = new Point(coord.x - 4 + RNG.get().randomNumber(7), coord.y - 3 + RNG.get().randomNumber(5));

                if (coordInBounds(at) && tiles[at.y][at.x].isTraversable && !isItemAt(at)) {
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
    public void placeGoldNear(Point coord, int tries) {
        do {
            for (int i = 0; i <= 10; i++) {
                Point at = new Point(coord.x - 4 + RNG.get().randomNumber(7), coord.y - 3 + RNG.get().randomNumber(5));

                if (coordInBounds(at) && tiles[at.y][at.x].isTraversable && !isItemAt(at)) {
                    placeGold(at);
                    i = 9;
                }
            }

            tries--;
        } while (tries != 0);
    }

    // Places a treasure (Gold or Gems) at given row, column
    public void placeGold(Point coord) {
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
            } while (!types.contains(tiles[coord.y][coord.x]) || spawnUpStairs.is(coord) || spawnDownStairs.is(coord)
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
            } while (!tiles[position.y][position.x].isTraversable || getMonsterAt(position) != null
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
                if (tiles[position.y][position.x].isTraversable && getMonsterAt(coord) == null) {
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
                    if (coordInBounds(at) && tiles[at.y][at.x].isTraversable) {
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
            if (coordInBounds(coord) && (tiles[coord.y][coord.x].isTraversable || getDoorAt(coord) != null)) {
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

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
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

    public void setTile(int x, int y, Tile value) {
        tiles[x][y] = value;
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
        AbstractEntity entity = getEntityAt(coord);

        if (allowObstacle) {
            return tiles[coord.y][coord.x] == NULL
                    || (entity != null && entity.getType() != Entity.UPSTAIRS && entity.getType() != Entity.DOWNSTAIRS)
                    || !(tiles[coord.y][coord.x].isTraversable || isOpenDoor(coord));
        }

        return tiles[coord.y][coord.x] == NULL
                || (entity != null && entity.getType() != Entity.UPSTAIRS && entity.getType() != Entity.DOWNSTAIRS)
                || !tiles[coord.y][coord.x].isTraversable || getMonsterAt(coord) != null;
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
