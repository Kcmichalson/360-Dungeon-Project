package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents Hero character in the dungeon.
 *
 * @author Vannak Traluch
 * @author Kevin Michalson
 * @version 11/08/2026
 */
public abstract class Hero extends DungeonCharacter {

    /** Maximum percentage value for block calculation. */
    private static final double MAX_PERCENTAGE = 100.0;

    /** Chance for the hero to block an attack. */
    private final double myBlockChance;

    /** Hero's inventory of collectibles. */
    private final List<Collectible> myInventory;

    /** Number of pillars the hero has found. */
    private int myPillarsFound;

    /**
     * Constructs a Hero character.
     *
     * @param theHitPoints the starting hit points
     * @param theSpeed the attack speed
     * @param theAttackMinimum the minimum attack damage
     * @param theAttackMaximum the maximum attack damage
     * @param theName the character name
     * @param theBlock the block chance percentage
     * @param theHitChance the chance to land an attack
     */
    public Hero(final int theHitPoints, final int theSpeed, final int theAttackMinimum,
                final int theAttackMaximum, final String theName, final int theBlock,
                final double theHitChance) {
        super(theHitPoints, theSpeed, theAttackMinimum, theAttackMaximum, theName, theHitChance);
        myBlockChance = theBlock;
        myInventory = new ArrayList<>();
        myPillarsFound = 0;
    }

    /**
     * Adds item to the hero's inventory and updates pillar count.
     *
     * @param theItem the collectible to add
     */
    public void addCollectible(final Collectible theItem) {
        myInventory.add(theItem);
        if (theItem instanceof Pillar) {
            myPillarsFound++;
        }
    }

    /**
     * Gets number of pillars the hero has collected.
     *
     * @return the number of pillars
     */
    public int getPillarsFound() {
        return myPillarsFound;
    }

    /**
     * Block incoming damage based on the hero's block chance.
     *
     * @param theDamage the amount of incoming damage
     */
    public void blockAttempt(final int theDamage) {
        final Random randomBlock = new Random();

        // I multiply it by 100 here so it scales correctly with the whole-number blockChance
        if (randomBlock.nextDouble() * MAX_PERCENTAGE > myBlockChance) {
            takeDamage(theDamage);
        }
    }
}