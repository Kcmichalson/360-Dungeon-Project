package tests;

import model.Dungeon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DungeonTest {

    private Dungeon dungeon;

    @BeforeEach
    public void setUp() {
        dungeon = new Dungeon();
    }

    @Test
    public void testMoveNorthUpdatesCoordinates() {
        int startRow = dungeon.getHeroRow();

        dungeon.move("NORTH");

        assertEquals(startRow - 1, dungeon.getHeroRow(), "Hero row should decrease by 1 moving North.");
    }

    @Test
    public void testMoveCaseInsensitive() {
        int startCol = dungeon.getHeroCol();

        dungeon.move("eaSt");

        assertEquals(startCol + 1, dungeon.getHeroCol(), "Hero col should increase by 1 moving East regardless of case.");
    }
}