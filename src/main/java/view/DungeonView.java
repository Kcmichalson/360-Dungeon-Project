package view;

public class DungeonView {
    public void displayMessage(final String theMessage) {
        System.out.println(theMessage);
    }

    public void displayError(final String theError) {
        System.err.println(theError);
    }

    public void displayRoom(final model.Room theRoom) {
        // Fallback for now until finish the room's toString()
        System.out.println("You are in a room.");
    }
}
