package tests;
import model.Room;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void newRoomStartsEmpty() {
        Room room = new Room();

        assertFalse(room.hasPit());
        assertFalse(room.isExit());
        assertNull(room.getMonster());
    }
}