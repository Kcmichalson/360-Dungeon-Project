package model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private boolean myHasPit;
    private boolean myIsExit;
    private final boolean[] myDoors;
    private Monster myMonster;
    private final List<Potion> myPotions;
    private Pillar myPillar;

    public Room() {
        myHasPit = false;
        myIsExit = false;
        myDoors = new boolean[4];
        myMonster = null;
        myPotions = new ArrayList<>();
        myPillar = null;
    }

    public boolean hasDoor(final String theDir) {
        if (theDir == null) return false;

        return switch (theDir.toUpperCase()) {
            case "NORTH" -> myDoors[0];
            case "EAST"  -> myDoors[1];
            case "SOUTH" -> myDoors[2];
            case "WEST"  -> myDoors[3];
            default      -> false;
        };
    }

    public boolean hasPit() {
        return myHasPit;
    }

    public boolean isExit() {
        return myIsExit;
    }

    public Monster getMonster() {
        return myMonster;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean hasPotions() {
        return !myPotions.isEmpty();
    }

    public List<Potion> takePotions() {
        // Create a copy to pass to the hero, preventing reference issues
        List<Potion> collectedPotions = new ArrayList<>(myPotions);
        myPotions.clear(); // Removes them from the room
        return collectedPotions;
    }

    public boolean hasPillar() {
        return myPillar != null;
    }

    public Pillar takePillar() {
        Pillar temp = myPillar;
        myPillar = null; // Removes it from the room
        return temp;
    }

    // for tests
    public void setDoor(String direction, boolean exists) {
        switch (direction.toUpperCase()) {
            case "NORTH" -> myDoors[0] = exists;
            case "EAST"  -> myDoors[1] = exists;
            case "SOUTH" -> myDoors[2] = exists;
            case "WEST"  -> myDoors[3] = exists;
        }
    }

    public void addPotion(Potion potion) {
        myPotions.add(potion);
    }

    public void setPillar(Pillar pillar) {
        myPillar = pillar;
    }

}
