package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DungeonTest {

    @Test
    void defaultDungeonHasFiveRowsAndColumns() {
        final Dungeon dungeon = new Dungeon();

        assertEquals(5, dungeon.getRows());
        assertEquals(5, dungeon.getCols());
    }

    /**
     * Tests moving North.
     */
    @Test
    void createsRequestedRectangleWithEveryRoomInitialized() {
        final Dungeon dungeon = new Dungeon(4, 7, new Random(1));

        assertEquals(4, dungeon.getRows());
        assertEquals(7, dungeon.getCols());

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                assertNotNull(dungeon.getRoom(row, col));
            }
        }
    }

    @Test
    void rejectsInvalidDimensions() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(1, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(5, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(5, -1));
    }

    @Test
    void rejectsNullRandomGenerator() {
        assertThrows(NullPointerException.class,
                () -> new Dungeon(5, 5, null));
    }

    @Test
    void rejectsRoomCoordinatesOutsideMaze() {
        final Dungeon dungeon = new Dungeon(4, 7);

        assertThrows(IndexOutOfBoundsException.class,
                () -> dungeon.getRoom(-1, 0));
        assertThrows(IndexOutOfBoundsException.class,
                () -> dungeon.getRoom(0, -1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> dungeon.getRoom(4, 0));
        assertThrows(IndexOutOfBoundsException.class,
                () -> dungeon.getRoom(0, 7));
    }

    @Test
    void outerBoundariesHaveNoDoors() {
        final Dungeon dungeon = new Dungeon(5, 7, new Random(2));

        for (int col = 0; col < dungeon.getCols(); col++) {
            assertFalse(dungeon.getRoom(0, col).hasDoor("NORTH"));
            assertFalse(dungeon.getRoom(
                    dungeon.getRows() - 1, col).hasDoor("SOUTH"));
        }

        for (int row = 0; row < dungeon.getRows(); row++) {
            assertFalse(dungeon.getRoom(row, 0).hasDoor("WEST"));
            assertFalse(dungeon.getRoom(
                    row, dungeon.getCols() - 1).hasDoor("EAST"));
        }
    }

    @Test
    void neighboringRoomsHaveMatchingDoors() {
        final Dungeon dungeon = new Dungeon(5, 7, new Random(3));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (col + 1 < dungeon.getCols()) {
                    assertEquals(
                            room.hasDoor("EAST"),
                            dungeon.getRoom(row, col + 1).hasDoor("WEST"),
                            "East/west doors disagree at " + row + "," + col);
                }

                if (row + 1 < dungeon.getRows()) {
                    assertEquals(
                            room.hasDoor("SOUTH"),
                            dungeon.getRoom(row + 1, col).hasDoor("NORTH"),
                            "North/south doors disagree at " + row + "," + col);
                }
            }
        }
    }

    @Test
    void everyRoomIsReachableAndMazeContainsDeadEnds() {
        // Check several sizes and seeds without relying on random test inputs.
        final int[][] sizes = {{2, 2}, {3, 8}, {7, 4}, {10, 10}};

        for (final int[] size : sizes) {
            for (int seed = 0; seed < 10; seed++) {
                final Dungeon dungeon =
                        new Dungeon(size[0], size[1], new Random(seed));

                assertEquals(
                        size[0] * size[1],
                        countReachableRooms(dungeon),
                        "Disconnected maze for seed " + seed);

                int deadEnds = 0;

                for (int row = 0; row < dungeon.getRows(); row++) {
                    for (int col = 0; col < dungeon.getCols(); col++) {
                        if (countDoors(dungeon.getRoom(row, col)) == 1) {
                            deadEnds++;
                        }
                    }
                }

                assertTrue(deadEnds >= 2,
                        "Expected dead ends for seed " + seed);
            }
        }
    }

    @Test
    void sameSeedProducesSameMaze() {
        final Dungeon first = new Dungeon(5, 7, new Random(42));
        final Dungeon second = new Dungeon(5, 7, new Random(42));

        assertEquals(first.toString(), second.toString());
    }

    @Test
    void differentSeedsProduceMoreThanOneLayout() {
        final String first =
                new Dungeon(5, 7, new Random(0)).toString();

        boolean foundDifferent = false;

        for (int seed = 1; seed <= 10; seed++) {
            final String other =
                    new Dungeon(5, 7, new Random(seed)).toString();

            if (!first.equals(other)) {
                foundDifferent = true;
                break;
            }
        }

        assertTrue(foundDifferent,
                "All tested seeds produced the same maze");
    }

    @Test
    void heroStartsInTopLeftRoom() {
        final Dungeon dungeon = new Dungeon(5, 7);

        assertEquals(0, dungeon.getHeroRow());
        assertEquals(0, dungeon.getHeroCol());
        assertSame(dungeon.getRoom(0, 0), dungeon.getHeroCurrentRoom());
    }

    @Test
    void invalidDirectionsDoNotMoveHero() {
        final Dungeon dungeon = new Dungeon(5, 7);

        assertFalse(dungeon.isValidDoor(null));
        assertFalse(dungeon.isValidDoor("UP"));

        dungeon.move(null);
        dungeon.move("UP");
        dungeon.move("NORTH");
        dungeon.move("WEST");

        assertEquals(0, dungeon.getHeroRow());
        assertEquals(0, dungeon.getHeroCol());
    }

    /**
     * Tests that movement code ignores casing. For ease of development later.
     */
    @Test
    void heroCanMoveThroughDoorAndReturn() {
        final Dungeon dungeon = new Dungeon(5, 7, new Random(4));
        final Room start = dungeon.getHeroCurrentRoom();

        // A connected maze must allow east or south from the top-left room.
        if (start.hasDoor("EAST")) {
            assertTrue(dungeon.isValidDoor("east"));
            dungeon.move("east");

            assertEquals(0, dungeon.getHeroRow());
            assertEquals(1, dungeon.getHeroCol());

            dungeon.move("WEST");
        } else {
            assertTrue(dungeon.isValidDoor("south"));
            dungeon.move("south");

            assertEquals(1, dungeon.getHeroRow());
            assertEquals(0, dungeon.getHeroCol());

            dungeon.move("NORTH");
        }

        assertSame(start, dungeon.getHeroCurrentRoom());
    }

    @Test
    void closedDoorDoesNotAllowMovement() {
        final Dungeon dungeon = new Dungeon(5, 7);

        // Close both sides of this connection to set up a wall.
        dungeon.getRoom(0, 0).setDoor("EAST", false);
        dungeon.getRoom(0, 1).setDoor("WEST", false);

        assertFalse(dungeon.isValidDoor("EAST"));

        dungeon.move("EAST");

        assertEquals(0, dungeon.getHeroRow());
        assertEquals(0, dungeon.getHeroCol());
    }

    @Test
    void dungeonTextMatchesKnownTwoByTwoLayout() {
        final Dungeon dungeon = new Dungeon(2, 2, new Random(5));
        final String[] directions = {"NORTH", "EAST", "SOUTH", "WEST"};

        // Clear the generated connections for a predictable drawing.
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                for (final String direction : directions) {
                    dungeon.getRoom(row, col).setDoor(direction, false);
                }
            }
        }

        // Connect the two top rooms.
        dungeon.getRoom(0, 0).setDoor("EAST", true);
        dungeon.getRoom(0, 1).setDoor("WEST", true);

        // Connect the two rooms on the right.
        dungeon.getRoom(0, 1).setDoor("SOUTH", true);
        dungeon.getRoom(1, 1).setDoor("NORTH", true);

        final String expected =
                "*****\n"
                + "* | *\n"
                + "***-*\n"
                + "* * *\n"
                + "*****";

        assertEquals(expected, dungeon.toString());
    }

    private int countDoors(final Room theRoom) {
        int count = 0;

        for (final String direction :
                new String[] {"NORTH", "EAST", "SOUTH", "WEST"}) {
            if (theRoom.hasDoor(direction)) {
                count++;
            }
        }

        return count;
    }

    private int countReachableRooms(final Dungeon theDungeon) {
        final boolean[][] visited =
                new boolean[theDungeon.getRows()][theDungeon.getCols()];
        final Deque<int[]> pending = new ArrayDeque<>();

        final String[] directions = {"NORTH", "EAST", "SOUTH", "WEST"};
        final int[] rowChange = {-1, 0, 1, 0};
        final int[] colChange = {0, 1, 0, -1};

        pending.addLast(new int[] {0, 0});
        visited[0][0] = true;
        int count = 0;

        while (!pending.isEmpty()) {
            final int[] current = pending.removeFirst();
            final int row = current[0];
            final int col = current[1];
            count++;

            for (int direction = 0; direction < 4; direction++) {
                if (theDungeon.getRoom(row, col)
                        .hasDoor(directions[direction])) {

                    final int nextRow = row + rowChange[direction];
                    final int nextCol = col + colChange[direction];

                    assertTrue(nextRow >= 0
                                    && nextRow < theDungeon.getRows()
                                    && nextCol >= 0
                                    && nextCol < theDungeon.getCols(),
                            "Door leads outside the maze");

                    if (!visited[nextRow][nextCol]) {
                        visited[nextRow][nextCol] = true;
                        pending.addLast(new int[] {nextRow, nextCol});
                    }
                }
            }
        }

        return count;
    }
}
