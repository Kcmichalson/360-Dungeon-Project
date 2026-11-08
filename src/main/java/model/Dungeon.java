package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Stores and generates a rectangular dungeon maze.
 *
 * @author Luke Frace
 * @version 11/08/2026
 */
public class Dungeon {

    /** Default number of rows for a new dungeon. */
    private static final int DEFAULT_ROWS = 5;

    /** Default number of columns for a new dungeon. */
    private static final int DEFAULT_COLS = 5;

    /** Minimum allowed dimension for dungeon generation. */
    private static final int MIN_DIMENSION = 2;

    /** Offset used to calculate opposite door directions. */
    private static final int OPPOSITE_OFFSET = 2;

    /** Number of cardinal directions. */
    private static final int NUM_DIRECTIONS = 4;

    /** The valid cardinal directions. */
    private static final String[] DIRECTIONS = {
        "NORTH", "EAST", "SOUTH", "WEST"
    };

    /** Row index modifiers mapping to cardinal directions.
     * IE: What changes the row? */
    private static final int[] ROW_CHANGE = {-1, 0, 1, 0};

    /** Column index modifiers mapping to cardinal directions.
     * IE: What changes the column? */
    private static final int[] COL_CHANGE = {0, 1, 0, -1};

    /** The array storing the layout of the rooms. */
    private final Room[][] myMaze;

    /** Current row index of the hero. */
    private int myHeroRow;

    /** Current column index of the hero. */
    private int myHeroCol;

    /**
     * Creates a random 5-by-5 dungeon.
     * This is a provisional default until the team chooses difficulty sizes.
     */
    public Dungeon() {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }

    /**
     * Creates a random dungeon with the requested dimensions.
     *
     * @param theRows number of rows, at least 2
     * @param theCols number of columns, at least 2
     */
    public Dungeon(final int theRows, final int theCols) {
        this(theRows, theCols, new Random());
    }

    /**
     * Allows RNG for reproducible tests.
     *
     * @param theRows number of rows, at least 2
     * @param theCols number of columns, at least 2
     * @param theRandom random generator
     */
    public Dungeon(final int theRows, final int theCols,
                   final Random theRandom) {

        if (theRows < MIN_DIMENSION || theCols < MIN_DIMENSION) {
            throw new IllegalArgumentException(
                    "The dungeon must have at least 2 rows and 2 columns.");
        }

        Objects.requireNonNull(theRandom, "Random generator cannot be null.");

        myMaze = new Room[theRows][theCols];

        for (int row = 0; row < theRows; row++) {
            for (int col = 0; col < theCols; col++) {
                myMaze[row][col] = new Room();
            }
        }

        generateMaze(theRandom);

        // Temporary starting position until entrance placement is implemented.
        myHeroRow = 0;
        myHeroCol = 0;
    }

    /**
     * Generates a maze using randomized depth-first search.
     * Every room is reachable, with no connections outside the grid.
     * 
     * @param theRandom the random number generator
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

            final List<Integer> availableDirections = new ArrayList<>();

            for (int direction = 0; direction < DIRECTIONS.length; direction++) {
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
                final int oppositeDirection = (direction + OPPOSITE_OFFSET) % NUM_DIRECTIONS;

                myMaze[row][col].setDoor(
                        DIRECTIONS[direction], true);

                myMaze[nextRow][nextCol].setDoor(
                        DIRECTIONS[oppositeDirection], true);

                visited[nextRow][nextCol] = true;
                path.push(new int[] {nextRow, nextCol});
            }
        }
    }

    /**
     * Checks if a given coordinate is within the maze.
     *
     * @param theRow the row to check
     * @param theCol the column to check
     * @return true if the coordinate is inside the maze
     */
    private boolean isInside(final int theRow, final int theCol) {
        return theRow >= 0 && theRow < getRows()
                && theCol >= 0 && theCol < getCols();
    }

    /**
     * Gets the index for a given direction.
     *
     * @param theDirection the direction string
     * @return the integer index, or -1 if invalid
     */
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

    /**
     * Checks both the door and the destination before allowing movement.
     *
     * @param theDirection the direction to check
     * @return true if a valid door and destination exist
     */
    public boolean isValidDoor(final String theDirection) {
        final int direction = getDirectionIndex(theDirection);

        if (direction < 0) {
            return false;
        }

        final int nextRow = myHeroRow + ROW_CHANGE[direction];
        final int nextCol = myHeroCol + COL_CHANGE[direction];

        return isInside(nextRow, nextCol)
                && getHeroCurrentRoom().hasDoor(DIRECTIONS[direction])
                && myMaze[nextRow][nextCol].hasDoor(DIRECTIONS[(direction + OPPOSITE_OFFSET) % NUM_DIRECTIONS]);
    }

    /**
     * Retrieves the room the hero is currently inside.
     *
     * @return the current room
     */
    public Room getHeroCurrentRoom() {
        return myMaze[myHeroRow][myHeroCol];
    }

    /**
     * Moves the hero only when a valid connection exists.
     *
     * @param theDirection the direction to move
     */
    public void move(final String theDirection) {
        if (isValidDoor(theDirection)) {
            final int direction = getDirectionIndex(theDirection);
            myHeroRow += ROW_CHANGE[direction];
            myHeroCol += COL_CHANGE[direction];
        }
    }

    /**
     * Gets the number of rows in the maze.
     *
     * @return the number of rows
     */
    public int getRows() {
        return myMaze.length;
    }

    /**
     * Gets the number of columns in the maze.
     *
     * @return the number of columns
     */
    public int getCols() {
        return myMaze[0].length;
    }

    /**
     * Gets a specific room from the maze.
     *
     * @param theRow the row index
     * @param theCol the column index
     * @return the room at the specified coordinates
     */
    public Room getRoom(final int theRow, final int theCol) {
        if (!isInside(theRow, theCol)) {
            throw new IndexOutOfBoundsException(
                    "Room coordinates are outside the dungeon.");
        }

        return myMaze[theRow][theCol];
    }

    /**
     * Gets the hero's current row.
     *
     * @return the hero's row
     */
    public int getHeroRow() {
        return myHeroRow;
    }

    /**
     * Gets the hero's current column.
     *
     * @return the hero's column
     */
    public int getHeroCol() {
        return myHeroCol;
    }

    /**
     * Prints the maze structure using shared walls between rooms.
     * Room contents are left blank in this structural representation.
     *
     * @return the formatted ASCII string representation of the maze
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        for (int row = 0; row < getRows(); row++) {

            // North walls and doors for this row.
            for (int col = 0; col < getCols(); col++) {
                result.append('*');
                result.append(
                        myMaze[row][col].hasDoor("NORTH") ? '-' : '*');
            }
            result.append("*\n");

            // West walls/doors and room centers.
            for (int col = 0; col < getCols(); col++) {
                result.append(
                        myMaze[row][col].hasDoor("WEST") ? '|' : '*');
                result.append(' ');
            }
            result.append("*\n");
        }

        // Bottom boundary.
        result.append("*".repeat(getCols() * 2 + 1));

        return result.toString();
    }
}
