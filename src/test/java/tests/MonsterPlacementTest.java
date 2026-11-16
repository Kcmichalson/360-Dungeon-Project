package tests;

import factory.MonsterFactory;
import model.Dungeon;
import model.Gremlin;
import model.Monster;
import model.Ogre;
import model.Room;
import model.Skeleton;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MonsterPlacementTest {

    @Test
    void everyPillarHasItsOwnLivingOgre() {
        for (int seed = 0; seed < 40; seed++) {
            for (final int cols : new int[] {3, 7}) {
                final Dungeon dungeon =
                        new Dungeon(2, cols, new Random(seed));

                final Set<Monster> monsters =
                        Collections.newSetFromMap(
                                new IdentityHashMap<>());

                int guards = 0;

                for (int row = 0; row < dungeon.getRows(); row++) {
                    for (int col = 0; col < cols; col++) {
                        final Room room = dungeon.getRoom(row, col);
                        final Monster monster = room.getMonster();

                        if (room.isEntrance() || room.isExit()) {
                            assertNull(monster);
                        }

                        if (room.hasPillar()) {
                            assertInstanceOf(Ogre.class, monster);
                            guards++;
                        }

                        if (monster != null) {
                            assertTrue(monsters.add(monster),
                                    "Rooms must have separate monsters");

                            assertTrue(monster.getHitPoints() > 0);

                            if (!room.hasPillar()) {
                                assertTrue(monster instanceof Gremlin
                                        || monster instanceof Skeleton);
                            }
                        }
                    }
                }

                assertEquals(4, guards);
            }
        }
    }

    @Test
    void pillarGuardsHaveMoreHealthAndHigherMaximumDamage() {
        final Monster guard =
                new MonsterFactory().createPillarGuard();

        for (final Monster ordinary :
                new Monster[] {new Gremlin(), new Skeleton()}) {

            assertTrue(
                    guard.getHitPoints() > ordinary.getHitPoints());

            // Fixed: Changed from getMaximumDamage() to getAttackMax()
            assertTrue(
                    guard.getAttackMax()
                            > ordinary.getAttackMax());
        }
    }

    @Test
    void entranceAndExitRejectMonsters() {
        final Dungeon dungeon =
                new Dungeon(3, 7, new Random(12));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.isEntrance() || room.isExit()) {
                    assertThrows(IllegalStateException.class,
                            () -> room.setMonster(new Ogre()));

                    assertNull(room.getMonster());
                }
            }
        }
    }

    @Test
    void monsterCanBeRemovedAfterCombat() {
        final Dungeon dungeon =
                new Dungeon(3, 7, new Random(15));

        for (int row = 0; row < dungeon.getRows(); row++) {
            for (int col = 0; col < dungeon.getCols(); col++) {
                final Room room = dungeon.getRoom(row, col);

                if (room.canPlaceContents()) {
                    final Monster monster = new Gremlin();

                    room.setMonster(monster);
                    assertSame(monster, room.getMonster());

                    room.setMonster(null);
                    assertNull(room.getMonster());
                }
            }
        }
    }

    @Test
    void sameSeedProducesSameMonsterPlacement() {
        final Dungeon first =
                new Dungeon(8, 8, new Random(42));

        final Dungeon second =
                new Dungeon(8, 8, new Random(42));

        assertEquals(signature(first), signature(second));
    }

    @Test
    void monsterPlacementVariesAcrossSeeds() {
        final Set<String> layouts = new HashSet<>();

        for (int seed = 0; seed < 20; seed++) {
            final Dungeon dungeon =
                    new Dungeon(8, 8, new Random(seed));

            int occupied = 0;
            int empty = 0;

            for (int row = 0; row < dungeon.getRows(); row++) {
                for (int col = 0; col < dungeon.getCols(); col++) {
                    final Room room = dungeon.getRoom(row, col);

                    if (room.canPlaceContents() && !room.hasPillar()) {
                        if (room.getMonster() == null) {
                            empty++;
                        } else {
                            occupied++;
                        }
                    }
                }
            }

            assertTrue(occupied > 0);
            assertTrue(empty > 0);
            layouts.add(signature(dungeon));
        }

        assertTrue(layouts.size() > 1);
    }

    private String signature(final Dungeon theDungeon) {
        final StringBuilder result = new StringBuilder();

        for (int row = 0; row < theDungeon.getRows(); row++) {
            for (int col = 0; col < theDungeon.getCols(); col++) {
                final Monster monster =
                        theDungeon.getRoom(row, col).getMonster();

                result.append(monster == null
                        ? "."
                        : monster.getClass().getSimpleName());

                result.append(',');
            }
        }

        return result.toString();
    }
}