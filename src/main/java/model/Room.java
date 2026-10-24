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
        throw new UnsupportedOperationException("Not implemented yet");
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
}
