package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Room {

    enum Role {
        ORDINARY,
        ENTRANCE,
        EXIT
    }

    private final Role myRole;
    private boolean myHasPit;
    private final boolean[] myDoors;
    private Monster myMonster;
    private final List<Potion> myPotions;
    private Pillar myPillar;

    public Room() {
        this(Role.ORDINARY);
    }

    Room(final Role theRole) {
        myRole = Objects.requireNonNull(theRole);
        myHasPit = false;
        myDoors = new boolean[4];
        myMonster = null;
        myPotions = new ArrayList<>();
        myPillar = null;
    }

    public boolean isEntrance() {
        return myRole == Role.ENTRANCE;
    }

    public boolean isExit() {
        return myRole == Role.EXIT;
    }

    public boolean canPlaceContents() {
        return myRole == Role.ORDINARY;
    }

    public boolean hasDoor(final String theDirection) {
        if (theDirection == null) {
            return false;
        }

        return switch (theDirection.toUpperCase(Locale.ROOT)) {
            case "NORTH" -> myDoors[0];
            case "EAST" -> myDoors[1];
            case "SOUTH" -> myDoors[2];
            case "WEST" -> myDoors[3];
            default -> false;
        };
    }

    public void setDoor(final String theDirection,
                        final boolean theExists) {
        switch (theDirection.toUpperCase(Locale.ROOT)) {
            case "NORTH" -> myDoors[0] = theExists;
            case "EAST" -> myDoors[1] = theExists;
            case "SOUTH" -> myDoors[2] = theExists;
            case "WEST" -> myDoors[3] = theExists;
        }
    }

    public boolean hasPit() {
        return myHasPit;
    }

    public Monster getMonster() {
        return myMonster;
    }

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

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
