package tests;

import model.Dungeon;
import model.Pillar;
import model.Potion;
import model.Room;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlacementTest {

    @Test
    void placesOneEntranceOneExitAndFourDistinctPillars() {
        final int[][] sizes = {
            {2, 3},
            {3, 7},
            {5, 5}
        };

        for (final int[] size : sizes) {
            for (int seed = 0; seed < 40; seed++) {
                final Dungeon dungeon =
                        new Dungeon(size[0], size[1], new Random(seed));

                final EnumSet<Pillar.Type> found =
                        EnumSet.noneOf(Pillar.Type.class);

                int entrances = 0;
                int exits = 0;
                int pillars = 0;

                assertTrue(dungeon.getHeroCurrentRoom().isEntrance());

                for (int row = 0; row < dungeon.getRows(); row++) {
                    for (int col = 0; col < dungeon.getCols(); col++) {
                        final Room room = dungeon.getRoom(row, col);

                        if (room.isEntrance()) {
                            entrances++;
                        }

                        if (room.isExit()) {
                            exits++;
                        }

                        assertFalse(
                                room.isEntrance() && room.isExit());

                        if (room.isEntrance() || room.isExit()) {
                            assertFalse(room.hasPillar());
                            assertFalse(room.hasPotions());
                            assertFalse(room.hasPit());
                            assertNull(room.getMonster());
                        }

                        if (room.hasPillar()) {
                            pillars++;
                            assertTrue(room.canPlaceContents());
                            assertTrue(found.add(
                                    room.getPillar().getType()),
                                    "Duplicate pillar type");
                        }
                    }
                }

                assertEquals(1, entrances);
                assertEquals(1, exits);
                assertEquals(4, pillars);
                assertEquals(
                        EnumSet.allOf(Pillar.Type.class), found);
            }
        }
    }

    @Test
    void entranceAndExitRejectPotionsAndPillars() {
        final Dungeon dungeon =
                new Dungeon(3, 7, new Random(10));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.isEntrance() || room.isExit()) {
                    assertThrows(IllegalStateException.class,
                            () -> room.addPotion(new Potion()));

                    assertThrows(IllegalStateException.class,
                            () -> room.setPillar(new Pillar()));

                    assertFalse(room.hasPotions());
                    assertFalse(room.hasPillar());
                }
            }
        }
    }

    @Test
    void mapShowsEntranceExitAndPillarsAtTheirLocations() {
        final Dungeon dungeon =
                new Dungeon(5, 7, new Random(20));

        final String[] lines = dungeon.toString().split("\n");

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);
                final char actual =
                        lines[2 * row + 1].charAt(2 * col + 1);

                if (room.isEntrance()) {
                    assertEquals('i', actual);
                } else if (room.isExit()) {
                    assertEquals('O', actual);
                } else if (room.hasPillar()) {
                    assertEquals(
                            room.getPillar().getType()
                                    .name().charAt(0),
                            actual);
                }
            }
        }
    }

    @Test
    void takingPillarRemovesItFromRoomAndMap() {
        final Dungeon dungeon =
                new Dungeon(3, 7, new Random(30));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.hasPillar()) {
                    final Pillar original = room.getPillar();

                    assertSame(original, room.takePillar());
                    assertFalse(room.hasPillar());
                    assertNull(room.getPillar());

                    final String[] lines =
                            dungeon.toString().split("\n");
                    assertEquals(
                            ' ', lines[2 * row + 1].charAt(2 * col + 1));
                }
            }
        }
    }

    @Test
    void rejectsMazeTooSmallForSixDistinctRooms() {
        assertThrows(IllegalArgumentException.class,
                () -> new Dungeon(2, 2));
    }
}
