package tests;

import controller.DungeonController;
import model.Dungeon;
import model.GameState;
import model.Hero;
import model.Pillar;
import model.Potion;
import model.Room;
import model.Warrior;
import persistence.SaveManager;
import view.DungeonView;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SaveManagerTest {

    @TempDir
    Path myTemporaryDirectory;

    private GameState createGame(final int theSeed) {
        final Hero hero = new Warrior();

        hero.takeDamage(17);
        hero.addCollectible(new Potion(Potion.Type.VISION));
        hero.addCollectible(
                new Pillar(Pillar.Type.ABSTRACTION));

        final Dungeon dungeon =
                new Dungeon(5, 7, new Random(theSeed));

        for (final String direction :
                new String[] {"NORTH", "EAST", "SOUTH", "WEST"}) {

            if (dungeon.isValidDoor(direction)) {
                dungeon.move(direction);
                break;
            }
        }

        return new GameState(hero, dungeon);
    }

    private byte[] snapshot(final Object theObject)
            throws IOException {

        final ByteArrayOutputStream bytes =
                new ByteArrayOutputStream();

        try (final ObjectOutputStream output =
                     new ObjectOutputStream(bytes)) {
            output.writeObject(theObject);
        }

        return bytes.toByteArray();
    }

    @Test
    void namedSaveRestoresEntireGameSnapshot() throws Exception {
        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        final GameState game = createGame(42);
        Room guarded = null;

        for (int row = 0;
             row < game.getDungeon().getRows();
             row++) {

            for (int col = 0;
                 col < game.getDungeon().getCols();
                 col++) {

                final Room room =
                        game.getDungeon().getRoom(row, col);

                if (room.hasPillar()) {
                    guarded = room;
                }
            }
        }

        assertNotNull(guarded);
        guarded.getMonster().takeDamage(13);

        final byte[] expected = snapshot(game);

        manager.save("Before battle", game);

        assertTrue(Files.isRegularFile(
                myTemporaryDirectory.resolve(
                        "Before battle.sav")));

        // Changes after saving must not change the saved snapshot.
        game.getHero().takeDamage(9);
        guarded.getMonster().takeDamage(7);

        final GameState restored =
                manager.load("Before battle");

        assertNotSame(game.getHero(), restored.getHero());
        assertNotSame(game.getDungeon(), restored.getDungeon());

        assertEquals(108, restored.getHero().getHitPoints());
        assertEquals(2, restored.getHero().getInventory().size());

        assertEquals(
                game.getDungeon().getHeroRow(),
                restored.getDungeon().getHeroRow());

        assertEquals(
                game.getDungeon().getHeroCol(),
                restored.getDungeon().getHeroCol());

        // Compares the complete serialized state, including
        // inventory, monsters, room contents, doors and location.
        assertArrayEquals(expected, snapshot(restored));
    }

    @Test
    void multipleNamesKeepIndependentSnapshots() throws Exception {
        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        final GameState first = createGame(1);
        final GameState second = createGame(2);

        second.getHero().takeDamage(10);

        manager.save("slot-one", first);
        manager.save("slot-two", second);

        assertEquals(
                List.of("slot-one", "slot-two"),
                manager.listSaves());

        assertArrayEquals(
                snapshot(first),
                snapshot(manager.load("slot-one")));

        assertArrayEquals(
                snapshot(second),
                snapshot(manager.load("slot-two")));
    }

    @Test
    void duplicateNamePreservesExistingSaveAndCleansTemporaryFile()
            throws Exception {

        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        final GameState first = createGame(3);

        manager.save("slot", first);

        assertThrows(
                IOException.class,
                () -> manager.save("slot", createGame(4)));

        assertArrayEquals(
                snapshot(first),
                snapshot(manager.load("slot")));

        try (final var paths = Files.list(myTemporaryDirectory)) {
            assertEquals(
                    List.of("slot.sav"),
                    paths.map(path ->
                            path.getFileName().toString()).toList());
        }
    }

    @Test
    void missingCorruptTruncatedAndWrongTypeSavesPreserveLiveGame()
            throws Exception {

        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        Files.writeString(
                myTemporaryDirectory.resolve("corrupt.sav"),
                "invalid data");

        manager.save("valid", createGame(8));

        final byte[] valid = Files.readAllBytes(
                myTemporaryDirectory.resolve("valid.sav"));

        Files.write(
                myTemporaryDirectory.resolve("truncated.sav"),
                Arrays.copyOf(valid, valid.length / 2));

        Files.write(
                myTemporaryDirectory.resolve("wrong-type.sav"),
                snapshot("not a game"));

        final GameState live = createGame(9);
        final RecordingView view = new RecordingView();

        final DungeonController controller =
                new DungeonController(
                        live.getDungeon(),
                        view,
                        live.getHero(),
                        manager);

        final byte[] before = snapshot(live);

        for (final String name :
                List.of("missing", "corrupt",
                        "truncated", "wrong-type")) {

            assertFalse(controller.loadGame(name));

            assertSame(
                    live.getHero(),
                    controller.getHero());

            assertSame(
                    live.getDungeon(),
                    controller.getDungeon());

            assertArrayEquals(
                    before,
                    snapshot(new GameState(
                            controller.getHero(),
                            controller.getDungeon())));
        }

        assertEquals(4, view.myErrors.size());
    }

    @Test
    void controllerLoadsBothHeroAndDungeonOnlyOnSuccess()
            throws Exception {

        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        final GameState saved = createGame(11);
        manager.save("checkpoint", saved);

        final GameState live = createGame(12);

        final DungeonController controller =
                new DungeonController(
                        live.getDungeon(),
                        new RecordingView(),
                        live.getHero(),
                        manager);

        assertTrue(controller.loadGame("checkpoint"));

        assertNotSame(live.getHero(), controller.getHero());
        assertNotSame(live.getDungeon(), controller.getDungeon());

        assertArrayEquals(
                snapshot(saved),
                snapshot(new GameState(
                        controller.getHero(),
                        controller.getDungeon())));

        assertTrue(controller.saveGame("another"));

        assertEquals(
                List.of("another", "checkpoint"),
                manager.listSaves());
    }

    @Test
    void invalidNamesAndWriteFailuresLeaveLiveGameUnchanged()
            throws Exception {

        final SaveManager manager =
                new SaveManager(myTemporaryDirectory);

        for (final String name :
                new String[] {
                    "", "../outside", "bad/name", "bad:name", null
                }) {

            assertThrows(
                    IllegalArgumentException.class,
                    () -> manager.save(name, createGame(13)));
        }

        final Path fileAsDirectory =
                myTemporaryDirectory.resolve("blocked");

        Files.writeString(fileAsDirectory, "file");

        final GameState live = createGame(14);
        final RecordingView view = new RecordingView();

        final DungeonController controller =
                new DungeonController(
                        live.getDungeon(),
                        view,
                        live.getHero(),
                        new SaveManager(fileAsDirectory));

        final byte[] before = snapshot(live);

        assertFalse(controller.saveGame("slot"));
        assertArrayEquals(before, snapshot(live));
        assertEquals(1, view.myErrors.size());

        assertTrue(
                new SaveManager(
                        myTemporaryDirectory.resolve("new"))
                        .listSaves()
                        .isEmpty());
    }

    private static class RecordingView extends DungeonView {

        private final List<String> myErrors = new ArrayList<>();

        @Override
        public void displayError(final String theError) {
            myErrors.add(theError);
        }

        @Override
        public void displayMessage(final String theMessage) {
        }
    }
}
