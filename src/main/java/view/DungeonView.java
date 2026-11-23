package view;

import java.util.List;
import model.Collectible;
import model.Hero;
import model.Pillar;
import model.Potion;
import model.Room;

/**
 * Handles console output for the dungeon game.
 *
 * @author Kevin Michalson
 * @version 11/22/2026
 */
public class DungeonView {

    private static final int TOTAL_PILLARS = 4;

    public void displayMessage(final String theMessage) {
        if (theMessage == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        System.out.println(theMessage);
    }

    public void displayError(final String theError) {
        if (theError == null) {
            throw new IllegalArgumentException("Error cannot be null.");
        }
        System.err.println(theError);
    }

    public void displayStatus(final Hero theHero) {
        if (theHero == null) {
            throw new IllegalArgumentException("Hero cannot be null.");
        }
        System.out.println("\n[" + theHero.getName()
                + " | HP: " + theHero.getHitPoints()
                + " | Pillars: " + theHero.getPillarsFound()
                + "/" + TOTAL_PILLARS + "]");
    }

    public void displayRoom(final Room theRoom) {
        if (theRoom == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        final StringBuilder doors = new StringBuilder();
        if (theRoom.hasDoor("NORTH")) {
            doors.append("NORTH ");
        }
        if (theRoom.hasDoor("EAST")) {
            doors.append("EAST ");
        }
        if (theRoom.hasDoor("SOUTH")) {
            doors.append("SOUTH ");
        }
        if (theRoom.hasDoor("WEST")) {
            doors.append("WEST ");
        }

        if (theRoom.isEntrance()) {
            System.out.println("Location: Dungeon Entrance");
        } else if (theRoom.isExit()) {
            System.out.println("Location: Dungeon Exit");
        } else {
            System.out.println("Location: Dungeon Room");
        }
        System.out.println("Available Doors: " + doors.toString().trim());
    }

    public void displayInventory(final Hero theHero) {
        if (theHero == null) {
            throw new IllegalArgumentException("Hero cannot be null.");
        }
        final List<Collectible> inventory = theHero.getInventory();
        int healingPotions = 0;
        int visionPotions = 0;
        final StringBuilder pillars = new StringBuilder();

        for (final Collectible item : inventory) {
            if (item instanceof Potion potion) {
                if (potion.getType() == Potion.Type.HEALING) {
                    healingPotions++;
                } else if (potion.getType() == Potion.Type.VISION) {
                    visionPotions++;
                }
            } else if (item instanceof Pillar pillar) {
                if (!pillars.isEmpty()) {
                    pillars.append(", ");
                }
                pillars.append(pillar.getName());
            }
        }

        System.out.println("\n--- Hero Status & Inventory ---");
        System.out.println("Name: " + theHero.getName());
        System.out.println("HP: " + theHero.getHitPoints());
        System.out.println("Healing Potions: " + healingPotions);
        System.out.println("Vision Potions: " + visionPotions);
        System.out.println("Pillars (" + theHero.getPillarsFound()
                + "/" + TOTAL_PILLARS + "): "
                + (pillars.isEmpty() ? "None" : pillars));
    }
}