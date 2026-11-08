package tests;

import model.Dungeon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Dungeon.
 *
 * @author Kevin Michalson
 * @version 11/08/2026
 */
public class DungeonTest {

    /** The dungeon used for testing. */
    private Dungeon myDungeon;

    /**
     * Sets up the test before each test.
     */
    @BeforeEach
    public void setUp() {
        myDungeon = new Dungeon();
    }

    /**
     * Tests moving North.
     */
    @Test
    public void testMoveNorthUpdatesCoordinates() {
        final int startRow = myDungeon.getHeroRow();
        myDungeon.move("NORTH");

        assertEquals(startRow - 1, myDungeon.getHeroRow(),
                "Hero row should decrease by 1 moving North.");
    }

    /**
     * Tests that movement code ignores casing. For ease of development later.
     */
    @Test
    public void testMoveCaseInsensitive() {
        final int startCol = myDungeon.getHeroCol();
        myDungeon.move("eaSt");

        assertEquals(startCol + 1, myDungeon.getHeroCol(),
                "Hero col should increase by 1 moving East regardless of case.");
    }
}