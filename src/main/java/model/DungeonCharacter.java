package model;

import java.util.Random;

public abstract class DungeonCharacter {

    private int myHitPoints;
    private int myAttackSpeed;
    private int myAttackMin;
    private int myAttackMax;
    private String myCharacterName;
    private double myChanceToHit;
    private boolean myIsPlayer;

    protected DungeonCharacter() {
    }

    protected DungeonCharacter(final int theHitPoints) {
        if (theHitPoints < 0) {
            throw new IllegalArgumentException("Hit points cannot be negative.");
        }
        myHitPoints = theHitPoints;
    }

    protected DungeonCharacter(final int theHitPoints, final int theSpeed, final int theAttackMinimum, final int theAttackMaximum, final String theName, final double theHitChance) {
        if (theHitPoints < 0 || theSpeed < 0 || theAttackMinimum < 0 || theAttackMaximum < 0 || theHitChance < 0) {
            throw new IllegalArgumentException("Stats cannot be negative.");
        }
        if (theName == null || theName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        myHitPoints = theHitPoints;
        myAttackSpeed = theSpeed;
        myAttackMin = theAttackMinimum;
        myAttackMax = theAttackMaximum;
        myCharacterName = theName;
        myChanceToHit = theHitChance;
    }

    public int getHitPoints() {
        return myHitPoints;
    }

    public int getAttackSpeed() {
        return myAttackSpeed;
    }

    // Added this getter to fix the MonsterPlacementTest error
    public int getAttackMax() {
        return myAttackMax;
    }

    public String getName() {
        return myCharacterName;
    }

    public boolean isPlayer() {
        return myIsPlayer;
    }

    public void takeDamage(final int theAmount) {
        if (theAmount < 0) {
            throw new IllegalArgumentException("Damage amount cannot be negative.");
        }
        myHitPoints -= theAmount;
        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }

    public void attack(final DungeonCharacter theTarget) {
        if (theTarget == null) {
            throw new IllegalArgumentException("Target cannot be null.");
        }

        final Random attackMod = new Random();
        if (attackMod.nextDouble() < myChanceToHit) {
            theTarget.takeDamage(attackMod.nextInt(myAttackMin, myAttackMax));
        }
    }

    protected void setPlayer(final boolean thePlayerStatus) {
        myIsPlayer = thePlayerStatus;
    }

    public void heal(final int theAmount) {
        if (theAmount < 0) {
            throw new IllegalArgumentException("Heal amount cannot be negative.");
        }
        myHitPoints += theAmount;
    }
}