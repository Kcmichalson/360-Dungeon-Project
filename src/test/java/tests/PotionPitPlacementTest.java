package tests;

import model.Dungeon;
import model.Potion;
import model.Room;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PotionPitPlacementTest {

    @Test
    void separateRollsAllowEveryCombinationOfContents() {
        final Random random = new Random(42) {
            private int myRoll;

            @Override
            public double nextDouble() {
                final int roomIndex = myRoll / 3;
                final int contentIndex = myRoll % 3;
                myRoll++;

                final boolean shouldPlace =
                        (roomIndex & (1 << contentIndex)) != 0;

                return shouldPlace ? 0.05 : 0.95;
            }
        };

        // Fourteen rooms minus two endpoints and four pillars
        // leaves eight rooms for all eight content combinations.
        final Dungeon dungeon = new Dungeon(2, 7, random);

        int roomIndex = 0;

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.canPlaceContents() && !room.hasPillar()) {
                    final Set<Potion.Type> types =
                            EnumSet.noneOf(Potion.Type.class);

                    final List<Potion> potions = room.takePotions();

                    for (final Potion potion : potions) {
                        assertTrue(types.add(potion.getType()),
                                "A potion type should appear at most once");
                    }

                    assertEquals(
                            (roomIndex & 1) != 0,
                            types.contains(Potion.Type.HEALING));

                    assertEquals(
                            (roomIndex & 2) != 0,
                            types.contains(Potion.Type.VISION));

                    assertEquals(
                            (roomIndex & 4) != 0,
                            room.hasPit());

                    assertFalse(room.hasPotions());
                    roomIndex++;
                } else {
                    assertFalse(room.hasPotions());
                    assertFalse(room.hasPit());
                }
            }
        }

        assertEquals(8, roomIndex);
    }

    @Test
    void tenPercentBoundaryIsExclusive() {
        for (final double value : new double[] {0.099, 0.10, 0.50}) {
            final Random random = new Random(12) {
                @Override
                public double nextDouble() {
                    return value;
                }
            };

            final Dungeon dungeon = new Dungeon(3, 7, random);

            for (int row = 0; row < dungeon.getRows(); row++) {
                for (int col = 0; col < dungeon.getCols(); col++) {
                    final Room room = dungeon.getRoom(row, col);

                    final boolean eligible =
                            room.canPlaceContents() && !room.hasPillar();

                    final boolean expected =
                            eligible && value < 0.10;

                    assertEquals(expected, room.hasPit());

                    assertEquals(
                            expected ? 2 : 0,
                            room.takePotions().size());
                }
            }
        }
    }

    @Test
    void endpointsRejectPitsAndOrdinaryPitCanBeCleared() {
        final Dungeon dungeon =
                new Dungeon(3, 7, new Random(3));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.canPlaceContents()) {
                    room.setPit(true);
                    assertTrue(room.hasPit());

                    room.setPit(false);
                    assertFalse(room.hasPit());
                } else {
                    assertThrows(IllegalStateException.class,
                            () -> room.setPit(true));

                    assertFalse(room.hasPit());
                }
            }
        }
    }
}
