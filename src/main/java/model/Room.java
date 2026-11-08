package model;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Cuts all potions from the room.
     *
     * @return a list of the collected potions
     */
    public List<Potion> takePotions() {
        final List<Potion> collectedPotions = new ArrayList<>(myPotions);
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
     * Cuts the pillar from the room.
     *
     * @return the collected pillar
     */
    public Pillar takePillar() {
        final Pillar temp = myPillar;
        myPillar = null;
        return temp;
    }

    /**
     * Creates a door in a specific direction.
     *
     * @param theDirection the direction
     * @param theExists true if the door should exist
     */
    public void setDoor(final String theDirection, final boolean theExists) {
        switch (theDirection.toUpperCase()) {
            case "NORTH" -> myDoors[NORTH_INDEX] = theExists;
            case "EAST"  -> myDoors[EAST_INDEX] = theExists;
            case "SOUTH" -> myDoors[SOUTH_INDEX] = theExists;
            case "WEST"  -> myDoors[WEST_INDEX] = theExists;
        }
    }

    /**
     * Adds a potion to the room.
     *
     * @param thePotion the potion to add
     */
    public void addPotion(final Potion thePotion) {
        myPotions.add(thePotion);
    }

    /**
     * Adds a pillar to the room.
     *
     * @param thePillar the pillar to place in the room
     */
    public void setPillar(final Pillar thePillar) {
        myPillar = thePillar;
    }
}