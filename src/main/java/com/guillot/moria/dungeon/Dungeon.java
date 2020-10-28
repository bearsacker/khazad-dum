package com.guillot.moria.dungeon;

import static com.guillot.moria.configs.DungeonConfig.DUN_DIR_CHANGE;
import static com.guillot.moria.configs.DungeonConfig.DUN_MAGMA_STREAMER;
import static com.guillot.moria.configs.DungeonConfig.DUN_MAGMA_TREASURE;
import static com.guillot.moria.configs.DungeonConfig.DUN_MAX_HEIGHT;
import static com.guillot.moria.configs.DungeonConfig.DUN_MAX_WIDTH;
import static com.guillot.moria.configs.DungeonConfig.DUN_QUARTZ_STREAMER;
import static com.guillot.moria.configs.DungeonConfig.DUN_QUARTZ_TREASURE;
import static com.guillot.moria.configs.DungeonConfig.DUN_RANDOM_DIR;
import static com.guillot.moria.configs.DungeonConfig.DUN_ROOMS_MEAN;
import static com.guillot.moria.configs.DungeonConfig.DUN_ROOM_DOORS;
import static com.guillot.moria.configs.DungeonConfig.DUN_STREAMER_DENSITY;
import static com.guillot.moria.configs.DungeonConfig.DUN_STREAMER_WIDTH;
import static com.guillot.moria.configs.DungeonConfig.DUN_TUNNELING;
import static com.guillot.moria.configs.DungeonConfig.DUN_TUNNEL_DOORS;
import static com.guillot.moria.configs.DungeonConfig.DUN_UNUSUAL_ROOMS;
import static com.guillot.moria.configs.ScreenConfig.QUART_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.QUART_WIDTH;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_HEIGHT;
import static com.guillot.moria.configs.ScreenConfig.SCREEN_WIDTH;
import static com.guillot.moria.dungeon.Tile.CLOSED_DOOR;
import static com.guillot.moria.dungeon.Tile.CORRIDOR_FLOOR;
import static com.guillot.moria.dungeon.Tile.DARK_FLOOR;
import static com.guillot.moria.dungeon.Tile.DOWN_STAIR;
import static com.guillot.moria.dungeon.Tile.GRANITE_WALL;
import static com.guillot.moria.dungeon.Tile.LIGHT_FLOOR;
import static com.guillot.moria.dungeon.Tile.MAGMA_WALL;
import static com.guillot.moria.dungeon.Tile.NULL;
import static com.guillot.moria.dungeon.Tile.OPEN_DOOR;
import static com.guillot.moria.dungeon.Tile.PILLAR;
import static com.guillot.moria.dungeon.Tile.QUARTZ_WALL;
import static com.guillot.moria.dungeon.Tile.SECRET_DOOR;
import static com.guillot.moria.dungeon.Tile.TMP1_WALL;
import static com.guillot.moria.dungeon.Tile.TMP2_WALL;
import static com.guillot.moria.dungeon.Tile.UP_STAIR;

import java.util.ArrayList;
import java.util.Collections;

import com.guillot.moria.utils.Direction;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class Dungeon {

    private int level;

    private int width;

    private int height;

    private Tile[][] floor;

    private ArrayList<Point> doors;

    public Dungeon(int level) {
        this.level = level;
        this.height = DUN_MAX_HEIGHT;
        this.width = DUN_MAX_WIDTH;
        this.floor = new Tile[DUN_MAX_HEIGHT][DUN_MAX_WIDTH];
        for (int y = 0; y < DUN_MAX_HEIGHT; y++) {
            for (int x = 0; x < DUN_MAX_WIDTH; x++) {
                this.floor[y][x] = NULL;
            }
        }

        this.doors = new ArrayList<>();
    }

    // Cave logic flow for generation of new dungeon
    public void generate() {
        System.out.println("generating dungeon [level=" + level + ", seed=" + RNG.get().getSeed() + "]...");

        // Room initialization
        int row_rooms = 2 * (height / SCREEN_HEIGHT);
        int col_rooms = 2 * (width / SCREEN_WIDTH);

        boolean[][] room_map = new boolean[20][20];

        int random_room_count = RNG.get().randomNumberNormalDistribution(DUN_ROOMS_MEAN, 2);
        for (int i = 0; i < random_room_count; i++) {
            room_map[RNG.get().randomNumber(row_rooms) - 1][RNG.get().randomNumber(col_rooms) - 1] = true;
        }

        // Build rooms
        ArrayList<Point> locations = new ArrayList<>();

        for (int row = 0; row < row_rooms; row++) {
            for (int col = 0; col < col_rooms; col++) {
                if (room_map[row][col]) {
                    Point location = new Point();
                    location.y = (row * (SCREEN_HEIGHT >> 1) + QUART_HEIGHT);
                    location.x = (col * (SCREEN_WIDTH >> 1) + QUART_WIDTH);

                    if (level > RNG.get().randomNumber(DUN_UNUSUAL_ROOMS)) {
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

        for (int i = 0; i < locations.size() - 1; i++) {
            buildTunnel(locations.get(i + 1), locations.get(i));
        }

        // Generate walls and streamers
        fillEmptyTilesWith(GRANITE_WALL);
        for (int i = 0; i < DUN_MAGMA_STREAMER; i++) {
            placeStreamerRock(MAGMA_WALL, DUN_MAGMA_TREASURE);
        }
        for (int i = 0; i < DUN_QUARTZ_STREAMER; i++) {
            placeStreamerRock(QUARTZ_WALL, DUN_QUARTZ_TREASURE);
        }

        placeBoundaryWalls();

        // Place intersection doors
        for (Point door : doors) {
            placeDoorIfNextToTwoWalls(new Point(door.x - 1, door.y));
            placeDoorIfNextToTwoWalls(new Point(door.x + 1, door.y));
            placeDoorIfNextToTwoWalls(new Point(door.x, door.y - 1));
            placeDoorIfNextToTwoWalls(new Point(door.x, door.y + 1));
        }

        placeStairs(2, RNG.get().randomNumber(2) + 2, 3);
        placeStairs(1, RNG.get().randomNumber(2), 3);

        cleaningGraniteWalls();
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

    // Builds a room at a row, column coordinate -RAK-
    // Type 1 unusual rooms are several overlapping rectangular ones
    private void buildRoomOverlappingRectangles(Point coord) {
        System.out.println("building room overlapping rectangles at " + coord + "...");
        Tile floor = floorTileForLevel();

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
        Tile floor = floorTileForLevel();

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
            System.out.println("building room with inner rooms (plain) at " + coord + "...");
            placeRandomSecretDoor(coord, depth, height, left, right);
            // dungeonPlaceVaultMonster(coord, 1);
            break;
        case TREASURE_VAULT:
            System.out.println("building room with inner rooms (treasure vault) at " + coord + "...");
            // dungeonPlaceTreasureVault(coord, depth, height, left, right);

            // Guard the treasure well
            // dungeonPlaceVaultMonster(coord, 2 + NumberGenerator.get().randomNumber(3));

            // If the monsters don't get 'em.
            // dungeonPlaceVaultTrap(coord, new Point(4, 10), 2 + NumberGenerator.get().randomNumber(3));
            break;
        case PILLARS:
            System.out.println("building room with inner rooms (pillars) at " + coord + "...");
            placeRandomSecretDoor(coord, depth, height, left, right);

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
                // dungeonPlaceRandomObjectAt(new Point(coord.y, coord.x - 2), false);
            }

            if (RNG.get().randomNumber(3) == 1) {
                // dungeonPlaceRandomObjectAt(new Point(coord.y, coord.x + 2), false);
            }

            // dungeonPlaceVaultMonster(new Point(coord.y, coord.x - 2), NumberGenerator.get().randomNumber(2));
            // dungeonPlaceVaultMonster(new Point(coord.y, coord.x + 2), NumberGenerator.get().randomNumber(2));
            break;
        case MAZE:
            System.out.println("building room with inner rooms (maze) at " + coord + "...");
            placeRandomSecretDoor(coord, depth, height, left, right);

            placeMazeInsideRoom(depth, height, left, right);

            // Monsters just love mazes.
            // dungeonPlaceVaultMonster(new Point(coord.y, coord.x - 5), NumberGenerator.get().randomNumber(3));
            // dungeonPlaceVaultMonster(new Point(coord.y, coord.x + 5), NumberGenerator.get().randomNumber(3));

            // Traps make them entertaining.
            // dungeonPlaceVaultTrap(new Point(coord.y, coord.x - 3), new Point(2, 8), NumberGenerator.get().randomNumber(3));
            // dungeonPlaceVaultTrap(new Point(coord.y, coord.x + 3), new Point(2, 8), NumberGenerator.get().randomNumber(3));

            // Mazes should have some treasure too..
            for (int i = 0; i < 3; i++) {
                // dungeonPlaceRandomObjectNear(coord, 1);
            }
            break;
        case FOUR_SMALL_ROOMS:
            System.out.println("building room with inner rooms (four small rooms) at " + coord + "...");
            placeFourSmallRooms(coord, depth, height, left, right);

            // Treasure in each one.
            // dungeonPlaceRandomObjectNear(coord, 2 + NumberGenerator.get().randomNumber(2));

            // Gotta have some monsters.
            // dungeonPlaceVaultMonster(new Point(coord.y + 2, coord.x - 4), NumberGenerator.get().randomNumber(2));
            // dungeonPlaceVaultMonster(new Point(coord.y + 2, coord.x + 4), NumberGenerator.get().randomNumber(2));
            // dungeonPlaceVaultMonster(new Point(coord.y - 2, coord.x - 4), NumberGenerator.get().randomNumber(2));
            // dungeonPlaceVaultMonster(new Point(coord.y - 2, coord.x + 4), NumberGenerator.get().randomNumber(2));
            break;
        }
    }

    // Builds a room at a row, column coordinate
    // Type 3 unusual rooms are cross shaped
    private void buildRoomCrossShaped(Point coord) {
        Tile floor = floorTileForLevel();

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
            System.out.println("building room cross shaped (large middle pillar) at " + coord + "...");
            placeLargeMiddlePillar(coord);
            break;
        case 2: // Inner treasure vault
            System.out.println("building room cross shaped (treasure vault) at " + coord + "...");
            // dungeonPlaceVault(coord);

            // Place a secret door
            random_offset = RNG.get().randomNumber(4);
            if (random_offset < 3) {
                placeSecretDoor(new Point(coord.x, coord.y - 3 + (random_offset << 1)));
            } else {
                placeSecretDoor(new Point(coord.x - 7 + (random_offset << 1), coord.y));
            }

            // Place a treasure in the vault
            // dungeonPlaceRandomObjectAt(coord, false);

            // Let's guard the treasure well.
            // dungeonPlaceVaultMonster(coord, 2 + NumberGenerator.get().randomNumber(2));

            // Traps naturally
            // dungeonPlaceVaultTrap(coord, new Point(4, 4), 1 + NumberGenerator.get().randomNumber(3));
            break;
        case 3:
            System.out.println("building room cross shaped at " + coord + "...");
            if (RNG.get().randomNumber(3) == 1) {
                this.floor[coord.y - 1][coord.x - 2] = TMP1_WALL;
                this.floor[coord.y + 1][coord.x - 2] = TMP1_WALL;
                this.floor[coord.y - 1][coord.x + 2] = TMP1_WALL;
                this.floor[coord.y + 1][coord.x + 2] = TMP1_WALL;
                this.floor[coord.y - 2][coord.x - 1] = TMP1_WALL;
                this.floor[coord.y - 2][coord.x + 1] = TMP1_WALL;
                this.floor[coord.y + 2][coord.x - 1] = TMP1_WALL;
                this.floor[coord.y + 2][coord.x + 1] = TMP1_WALL;
                if (RNG.get().randomNumber(3) == 1) {
                    placeSecretDoor(new Point(coord.x - 2, coord.y));
                    placeSecretDoor(new Point(coord.x + 2, coord.y));
                    placeSecretDoor(new Point(coord.x, coord.y - 2));
                    placeSecretDoor(new Point(coord.x, coord.y + 2));
                }
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
        System.out.println("building room at " + coord + "...");
        Tile floor = floorTileForLevel();

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
    private void buildTunnel(Point start, Point end) {
        System.out.println("building tunnel between " + start + " and " + end + "...");

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

            if (RNG.get().randomNumber(100) > DUN_DIR_CHANGE) {
                if (RNG.get().randomNumber(DUN_RANDOM_DIR) == 1) {
                    chanceOfRandomDirection(direction);
                } else {
                    pickCorrectDirection(direction, start, end);
                }
            }

            int tmp_row = start.y + direction.y;
            int tmp_col = start.x + direction.x;

            while (!coordInBounds(new Point(tmp_col, tmp_row))) {
                if (RNG.get().randomNumber(DUN_RANDOM_DIR) == 1) {
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
                    if (doors.size() < 100) {
                        doors.add(new Point(start.x, start.y));
                    }
                    door_flag = true;
                }

                if (RNG.get().randomNumber(100) > DUN_TUNNELING) {
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
                if (RNG.get().randomNumber(100) < DUN_ROOM_DOORS) {
                    placeDoor(wall);
                } else {
                    // these have to be doorways to rooms
                    this.floor[wall.y][wall.x] = CORRIDOR_FLOOR;
                }
            }
        }
    }

    void placeInnerPillars(Point coord) {
        System.out.println("\t-> placing inner pillars at " + coord + "...");
        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 1; x <= coord.x + 1; x++) {
                this.floor[y][x] = PILLAR;
            }
        }

        if (RNG.get().randomNumber(2) != 1) {
            return;
        }

        int offset = RNG.get().randomNumber(2);

        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 5 - offset; x <= coord.x - 3 - offset; x++) {
                this.floor[y][x] = PILLAR;
            }
        }

        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x + 3 + offset; x <= coord.x + 5 + offset; x++) {
                this.floor[y][x] = PILLAR;
            }
        }
    }

    private void placeLargeMiddlePillar(Point coord) {
        System.out.println("\t-> placing large middle pillar at " + coord + "...");
        for (int y = coord.y - 1; y <= coord.y + 1; y++) {
            for (int x = coord.x - 1; x <= coord.x + 1; x++) {
                this.floor[y][x] = PILLAR;
            }
        }
    }

    private void placeDoor(Point coord) {
        int door_type = RNG.get().randomNumber(3);

        if (door_type == 1) {
            if (RNG.get().randomNumber(4) == 1) {
                placeBrokenDoor(coord);
            } else {
                placeOpenDoor(coord);
            }
        } else if (door_type == 2) {
            door_type = RNG.get().randomNumber(12);

            if (door_type > 3) {
                placeClosedDoor(coord);
            } else if (door_type == 3) {
                placeStuckDoor(coord);
            } else {
                placeLockedDoor(coord);
            }
        } else {
            placeSecretDoor(coord);
        }
    }

    private void placeOpenDoor(Point coord) {
        System.out.println("\t-> placing open door at " + coord + "...");
        this.floor[coord.y][coord.x] = OPEN_DOOR;
    }

    private void placeBrokenDoor(Point coord) {
        System.out.println("\t-> placing broken door at " + coord + "...");
        this.floor[coord.y][coord.x] = OPEN_DOOR;
        // game.treasure.list[cur_pos].misc_use = 1;
    }

    private void placeClosedDoor(Point coord) {
        System.out.println("\t-> placing closed door at " + coord + "...");
        this.floor[coord.y][coord.x] = CLOSED_DOOR;
    }

    private void placeLockedDoor(Point coord) {
        System.out.println("\t-> placing locked door at " + coord + "...");
        this.floor[coord.y][coord.x] = CLOSED_DOOR;
        // game.treasure.list[cur_pos].misc_use = (int16_t)(randomNumber(10) + 10);
    }

    private void placeStuckDoor(Point coord) {
        System.out.println("\t-> placing stuck door at " + coord + "...");
        this.floor[coord.y][coord.x] = CLOSED_DOOR;
        // game.treasure.list[cur_pos].misc_use = (int16_t)(-randomNumber(10) - 10);
    }

    private void placeSecretDoor(Point coord) {
        System.out.println("\t-> placing secret door at " + coord + "...");
        this.floor[coord.y][coord.x] = SECRET_DOOR;
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
        default:
            placeSecretDoor(new Point(right + 1, coord.y));
            break;
        }
    }

    private void placeMazeInsideRoom(int depth, int height, int left, int right) {
        for (int y = height; y <= depth; y++) {
            for (int x = left; x <= right; x++) {
                if ((0x1 & (x + y)) != 0) {
                    this.floor[y][x] = TMP1_WALL;
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
        // Choose starting point and direction
        Point coord = new Point(
                (this.width / 2) + 16 - RNG.get().randomNumber(33),
                (this.height) + 11 - RNG.get().randomNumber(23));

        // Get random direction
        Direction direction = Direction.random();

        // Place streamer into dungeon
        int t1 = 2 * DUN_STREAMER_WIDTH + 1; // Constants
        int t2 = DUN_STREAMER_WIDTH + 1;

        do {
            for (int i = 0; i < DUN_STREAMER_DENSITY; i++) {
                Point spot = new Point(
                        coord.x + RNG.get().randomNumber(t1) - t2,
                        coord.y + RNG.get().randomNumber(t1) - t2);

                if (coordInBounds(spot)) {
                    if (this.floor[spot.y][spot.x] == GRANITE_WALL) {
                        this.floor[spot.y][spot.x] = rock_type;

                        if (RNG.get().randomNumber(chance_of_treasure) == 1) {
                            // dungeonPlaceGold(spot);
                        }
                    }
                }
            }
        } while (playerMovePosition(direction, coord));
    }

    // Places a staircase 1=up, 2=down
    private void placeStairs(int stair_type, int number, int walls) {
        Point coord1 = new Point();
        Point coord2 = new Point();

        for (int i = 0; i < number; i++) {
            boolean placed = false;

            while (!placed) {
                int j = 0;

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
                            if (!this.floor[coord1.y][coord1.x].isWall && coordWallsNextTo(coord1) >= walls) {
                                placed = true;
                                if (stair_type == 1) {
                                    placeUpStairs(coord1);
                                } else {
                                    placeDownStairs(coord1);
                                }
                            }
                            coord1.x++;
                        } while ((coord1.x != coord2.x) && (!placed));

                        coord1.x = coord2.x - 12;
                        coord1.y++;
                    } while ((coord1.y != coord2.y) && (!placed));

                    j++;
                } while ((!placed) && (j <= 30));

                walls--;
            }
        }
    }

    // Place an up staircase at given y, x
    private void placeUpStairs(Point coord) {
        System.out.println("\t-> placing up stairs at " + coord + "...");
        this.floor[coord.y][coord.x] = UP_STAIR;
    }

    // Place a down staircase at given y, x
    private void placeDownStairs(Point coord) {
        System.out.println("\t-> placing down stairs at " + coord + "...");
        this.floor[coord.y][coord.x] = DOWN_STAIR;
    }

    // Places door at y, x position if at least 2 walls found
    private void placeDoorIfNextToTwoWalls(Point coord) {
        if (this.floor[coord.y][coord.x] == CORRIDOR_FLOOR && RNG.get().randomNumber(100) > DUN_TUNNEL_DOORS
                && isNextTo(coord)) {
            placeDoor(coord);
        }
    }

    // Checks points north, south, east, and west for a wall -RAK-
    // note that y,x is always coordInBounds(), i.e. 0 < y < dg.height-1,
    // and 0 < x < dg.width-1
    private int coordWallsNextTo(Point coord) {
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

        return walls;
    }

    private boolean coordInBounds(Point coord) {
        boolean y = coord.y > 0 && coord.y < this.height - 1;
        boolean x = coord.x > 0 && coord.x < this.width - 1;

        return y && x;
    }

    private boolean isNextTo(Point coord) {
        if (coordCorridorWallsNextTo(coord) > 2) {
            boolean vertical = this.floor[coord.y - 1][coord.x].isWall && this.floor[coord.y + 1][coord.x].isWall;
            boolean horizontal = this.floor[coord.y][coord.x - 1].isWall && this.floor[coord.y][coord.x + 1].isWall;

            return vertical || horizontal;
        }

        return false;
    }

    private boolean playerMovePosition(Direction direction, Point coord) {
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
                if (this.floor[y][x].isDoor) {
                    walls++;
                }
            }
        }

        return walls;
    }

    // Returns a Dark/Light floor tile based on dg.current_level, and random number
    private Tile floorTileForLevel() {
        if (level <= RNG.get().randomNumber(25)) {
            return LIGHT_FLOOR;
        }
        return DARK_FLOOR;
    }

    // Places indestructible rock around edges of dungeon -RAK-
    private void placeBoundaryWalls() {
        System.out.println("\t-> placing boundary walls...");

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
        System.out.println("cleaning granite walls...");

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.floor[i][j] == GRANITE_WALL && !isVisibleFromFloor(i, j)) {
                    this.floor[i][j] = NULL;
                }
            }
        }
    }

    private boolean isVisibleFromFloor(int x, int y) {
        if (x - 1 >= 0 && (this.floor[x - 1][y].isFloor || this.floor[x - 1][y].isDoor)) {
            return true;
        }

        if (x + 1 < height && (this.floor[x + 1][y].isFloor || this.floor[x + 1][y].isDoor)) {
            return true;
        }

        if (y - 1 >= 0 && (this.floor[x][y - 1].isFloor || this.floor[x][y - 1].isDoor)) {
            return true;
        }

        if (y + 1 < width && (this.floor[x][y + 1].isFloor || this.floor[x][y + 1].isDoor)) {
            return true;
        }

        if (x - 1 >= 0 && y - 1 >= 0 && (this.floor[x - 1][y - 1].isFloor || this.floor[x - 1][y - 1].isDoor)) {
            return true;
        }

        if (x + 1 < height && y + 1 < width && (this.floor[x + 1][y + 1].isFloor || this.floor[x + 1][y + 1].isDoor)) {
            return true;
        }

        if (x + 1 < height && y - 1 >= 0 && (this.floor[x + 1][y - 1].isFloor || this.floor[x + 1][y - 1].isDoor)) {
            return true;
        }

        if (x - 1 >= 0 && y + 1 < width && (this.floor[x - 1][y + 1].isFloor || this.floor[x - 1][y + 1].isDoor)) {
            return true;
        }

        return false;
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

}
