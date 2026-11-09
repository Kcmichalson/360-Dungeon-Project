package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a single room in the dungeon maze.
 *
 * @author Kevin Michalson
 * @version 11/08/2026
 */
public class Room {

    /** Number of possible doors in a room. */
    private static final int NUM_DOORS = 4;

    /** Index for the North door. */
    private static final int NORTH_INDEX = 0;

    /** Index for the East door. */
    private static final int EAST_INDEX = 1;

    /** Index for the South door. */
    private static final int SOUTH_INDEX = 2;

    /** Index for the West door. */
    private static final int WEST_INDEX = 3;

    /** Does the room contain a pit trap? */
    private boolean myHasPit;

    /** Does the room have the dungeon exit? */
    private boolean myIsExit;

    /** Array representing where the doors of a room are. */
    private final boolean[] myDoors;

    /** The monster currently in the room. */
    private Monster myMonster;

    /** The potions currently in the room. */
    private final List<Potion> myPotions;

    /** The pillar currently in the room. */
    private Pillar myPillar;

    /**
     * Constructs a new Room.
     */
    public Room() {
        this(Role.ORDINARY);
    }

    Room(final Role theRole) {
        myRole = Objects.requireNonNull(theRole);
        myHasPit = false;
        myIsExit = false;
        myDoors = new boolean[NUM_DOORS];
        myMonster = null;
        myPotions = new ArrayList<>();
        myPillar = null;
    }

    /**
     * Checks if the room has a door in the given direction.
     *
     * @param theDir the direction to check
     * @return true if a door exists, false otherwise
     */
    public boolean hasDoor(final String theDir) {
        if (theDir == null) {
            return false;
        }

        return switch (theDir.toUpperCase()) {
            case "NORTH" -> myDoors[NORTH_INDEX];
            case "EAST"  -> myDoors[EAST_INDEX];
            case "SOUTH" -> myDoors[SOUTH_INDEX];
            case "WEST"  -> myDoors[WEST_INDEX];
            default      -> false;
        };
    }

    /**
     * Checks if the room has a pit.
     *
     * @return true if the room has a pit
     */
    public boolean hasPit() {
        return myHasPit;
    }

    /**
     * Checks if the room has the dungeon exit.
     *
     * @return true if the room is the exit
     */
    public boolean isExit() {
        return myIsExit;
    }

    /**
     * Gets the monster in the room.
     *
     * @return the monster, or null if none exists
     */
    public Monster getMonster() {
        return myMonster;
    }

    /**
     * Returns a string representation of the room.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks if the room contains potions.
     *
     * @return true if there is at least one potion
     */
    public boolean hasPotions() {
        return !myPotions.isEmpty();
    }

    public void addPotion(final Potion thePotion) {
        if (!canPlaceContents()) {
            throw new IllegalStateException(
                    "Cannot place potions in the entrance or exit.");
        }

        myPotions.add(thePotion);
    }

    public List<Potion> takePotions() {
        final List<Potion> collectedPotions =
                new ArrayList<>(myPotions);
        myPotions.clear();
        return collectedPotions;
    }

    /**
     * Checks if the room has a pillar.
     *
     * @return true if a pillar is present
     */
    public boolean hasPillar() {
        return myPillar != null;
    }

    /**
     * Returns the pillar without removing it.
     */
    public Pillar getPillar() {
        return myPillar;
    }

    public void setPillar(final Pillar thePillar) {
        if (thePillar != null && !canPlaceContents()) {
            throw new IllegalStateException(
                    "Cannot place pillars in the entrance or exit.");
        }

        myPillar = thePillar;
    }

    public Pillar takePillar() {
        final Pillar collectedPillar = myPillar;
        myPillar = null;
        return collectedPillar;
    }
}
