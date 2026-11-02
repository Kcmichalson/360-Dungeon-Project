package tests;

import model.Pillar;
import model.Potion;
import model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room testRoom;

    @BeforeEach
    public void setUp() {
        testRoom = new Room();
    }

    @Test
    public void testHasDoorValid() {
        testRoom.setDoor("NORTH", true);

        assertTrue(testRoom.hasDoor("NORTH"), "Room should have a North door.");
        assertTrue(testRoom.hasDoor("north"), "Door check should be case-insensitive.");
        assertFalse(testRoom.hasDoor("SOUTH"), "Room should not have a South door by default.");
    }

    @Test
    public void testHasDoorInvalidString() {
        assertFalse(testRoom.hasDoor("INVALID"), "Invalid strings should return false.");
        assertFalse(testRoom.hasDoor(null), "Null strings should return false safely.");
    }

    @Test
    public void testTakePotionsClearsRoom() {
        testRoom.addPotion(new Potion());
        assertTrue(testRoom.hasPotions(), "Room should initially have potions.");

        List<Potion> collected = testRoom.takePotions();

        assertFalse(collected.isEmpty(), "The returned list should contain the potions.");
        assertFalse(testRoom.hasPotions(), "Room's potion list should be empty after taking.");
    }

    @Test
    public void testTakePillarClearsRoom() {
        testRoom.setPillar(new Pillar());
        assertTrue(testRoom.hasPillar(), "Room should initially have a pillar.");

        Pillar collected = testRoom.takePillar();

        assertNotNull(collected, "The returned pillar should not be null.");
        assertFalse(testRoom.hasPillar(), "Room should not have a pillar after it is taken.");
    }
}