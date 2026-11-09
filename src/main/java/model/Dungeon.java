package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Dungeon {

    private static final String[] DIRECTIONS = {
        "NORTH", "EAST", "SOUTH", "WEST"
    };

    private static final int[] ROW_CHANGE = {-1, 0, 1, 0};
    private static final int[] COL_CHANGE = {0, 1, 0, -1};

    private final Room[][] myMaze;
    private int myHeroRow;
    private int myHeroCol;

    public Dungeon() {
        this(5, 5);
    }

    public Dungeon(final int theRows, final int theCols) {
        this(theRows, theCols, new Random());
    }

    /**
     * Creates a connected maze, one entrance, one exit,
     * and all four distinct pillars.
     */
    public Dungeon(final int theRows, final int theCols,
                   final Random theRandom) {

        if (theRows < 2 || theCols < 2) {
            throw new IllegalArgumentException(
                    "The dungeon needs at least two rows and columns.");
        }

        if ((long) theRows * theCols < 6) {
            throw new IllegalArgumentException(
                    "Four pillars and two endpoints require six rooms.");
        }

        Objects.requireNonNull(
                theRandom, "Random generator cannot be null.");

        myMaze = new Room[theRows][theCols];

        // The entrance uses index zero.
        // The exit uses a different randomly selected index.
        final int exitIndex =
                1 + theRandom.nextInt(theRows * theCols - 1);

        for (int row = 0; row < theRows; row++) {
            for (int col = 0; col < theCols; col++) {
                final int roomIndex = row * theCols + col;
                Room.Role role = Room.Role.ORDINARY;

                if (roomIndex == 0) {
                    role = Room.Role.ENTRANCE;
                } else if (roomIndex == exitIndex) {
                    role = Room.Role.EXIT;
                }

                myMaze[row][col] = new Room(role);
            }
        }

        generateMaze(theRandom);
        placePillars(theRandom);

        myHeroRow = 0;
        myHeroCol = 0;
    }

    /**
     * Places each pillar in a different ordinary room.
     */
    private void placePillars(final Random theRandom) {
        final List<Room> eligibleRooms = new ArrayList<>();

        for (final Room[] row : myMaze) {
            for (final Room room : row) {
                if (room.canPlaceContents()) {
                    eligibleRooms.add(room);
                }
            }
        }

        Collections.shuffle(eligibleRooms, theRandom);

        int index = 0;
        for (final Pillar.Type type : Pillar.Type.values()) {
            eligibleRooms.get(index).setPillar(new Pillar(type));
            index++;
        }
    }

    /**
     * Connects every room using randomized depth-first search.
     */
    private void generateMaze(final Random theRandom) {
        final boolean[][] visited =
                new boolean[getRows()][getCols()];
        final Deque<int[]> path = new ArrayDeque<>();

        final int startRow = theRandom.nextInt(getRows());
        final int startCol = theRandom.nextInt(getCols());

        visited[startRow][startCol] = true;
        path.push(new int[] {startRow, startCol});

        while (!path.isEmpty()) {
            final int[] current = path.peek();
            final int row = current[0];
            final int col = current[1];

            final List<Integer> availableDirections =
                    new ArrayList<>();

            for (int direction = 0;
                 direction < DIRECTIONS.length;
                 direction++) {

                final int nextRow = row + ROW_CHANGE[direction];
                final int nextCol = col + COL_CHANGE[direction];

                if (isInside(nextRow, nextCol)
                        && !visited[nextRow][nextCol]) {
                    availableDirections.add(direction);
                }
            }

            if (availableDirections.isEmpty()) {
                path.pop();
            } else {
                final int direction = availableDirections.get(
                        theRandom.nextInt(availableDirections.size()));

                final int nextRow = row + ROW_CHANGE[direction];
                final int nextCol = col + COL_CHANGE[direction];
                final int oppositeDirection = (direction + 2) % 4;

                myMaze[row][col].setDoor(
                        DIRECTIONS[direction], true);
                myMaze[nextRow][nextCol].setDoor(
                        DIRECTIONS[oppositeDirection], true);

                visited[nextRow][nextCol] = true;
                path.push(new int[] {nextRow, nextCol});
            }
        }
    }

    private boolean isInside(final int theRow, final int theCol) {
        return theRow >= 0 && theRow < getRows()
                && theCol >= 0 && theCol < getCols();
    }

    private int getDirectionIndex(final String theDirection) {
        if (theDirection == null) {
            return -1;
        }

        final String direction =
                theDirection.toUpperCase(Locale.ROOT);

        for (int index = 0; index < DIRECTIONS.length; index++) {
            if (DIRECTIONS[index].equals(direction)) {
                return index;
            }
        }

        return -1;
    }

    public boolean isValidDoor(final String theDirection) {
        final int direction = getDirectionIndex(theDirection);

        if (direction < 0) {
            return false;
        }

        final int nextRow = myHeroRow + ROW_CHANGE[direction];
        final int nextCol = myHeroCol + COL_CHANGE[direction];

        return isInside(nextRow, nextCol)
                && getHeroCurrentRoom().hasDoor(
                        DIRECTIONS[direction])
                && myMaze[nextRow][nextCol].hasDoor(
                        DIRECTIONS[(direction + 2) % 4]);
    }

    public Room getHeroCurrentRoom() {
        return myMaze[myHeroRow][myHeroCol];
    }

    public void move(final String theDirection) {
        if (isValidDoor(theDirection)) {
            final int direction = getDirectionIndex(theDirection);
            myHeroRow += ROW_CHANGE[direction];
            myHeroCol += COL_CHANGE[direction];
        }
    }

    public int getRows() {
        return myMaze.length;
    }

    public int getCols() {
        return myMaze[0].length;
    }

    public Room getRoom(final int theRow, final int theCol) {
        if (!isInside(theRow, theCol)) {
            throw new IndexOutOfBoundsException(
                    "Room coordinates are outside the dungeon.");
        }

        return myMaze[theRow][theCol];
    }

    public int getHeroRow() {
        return myHeroRow;
    }

    public int getHeroCol() {
        return myHeroCol;
    }

    /**
     * Shows walls, doors, entrance, exit, and pillars.
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                result.append('*');
                result.append(
                        myMaze[row][col].hasDoor("NORTH") ? '-' : '*');
            }
            result.append("*\n");

            for (int col = 0; col < getCols(); col++) {
                final Room room = myMaze[row][col];

                result.append(room.hasDoor("WEST") ? '|' : '*');

                if (room.isEntrance()) {
                    result.append('i');
                } else if (room.isExit()) {
                    result.append('O');
                } else if (room.hasPillar()) {
                    result.append(
                            room.getPillar().getType().name().charAt(0));
                } else {
                    result.append(' ');
                }
            }
            result.append("*\n");
        }

        result.append("*".repeat(getCols() * 2 + 1));
        return result.toString();
    }
}
