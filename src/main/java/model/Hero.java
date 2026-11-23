package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents Hero character in the dungeon.
 *
 * @author Vannak Traluch
 * @author Kevin Michalson
 * @version 11/22/2026
 */
public abstract class Hero extends DungeonCharacter {

    /** Maximum percentage value for block calculation. */
    private static final double MAX_PERCENTAGE = 100.0;

    /** Minimum HP restored by a standard healing potion. */
    private static final int MIN_POTION_HEAL = 10;

    /** Maximum HP restored by a standard healing potion. */
    private static final int MAX_POTION_HEAL = 16;

    /** Chance for the hero to block an attack. */
    private final double myBlockChance;

    /** Hero's inventory of collectibles. */
    private final List<Collectible> myInventory;

    /** Number of pillars the hero has found. */
    private int myPillarsFound;

    /** Optional custom player name. */
    private String myCustomName;

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
    public Hero(final int theHitPoints, final int theSpeed,
                final int theAttackMinimum, final int theAttackMaximum,
                final String theName, final int theBlock,
                final double theHitChance) {
        super(theHitPoints, theSpeed, theAttackMinimum,
                theAttackMaximum, theName, theHitChance);
        if (theBlock < 0 || theBlock > MAX_PERCENTAGE) {
            throw new IllegalArgumentException(
                    "Block chance must be between 0 and 100.");
        }
        myBlockChance = theBlock;
        myInventory = new ArrayList<>();
        myPillarsFound = 0;
        myCustomName = null;
        setPlayer(true);
    }

    /**
     * Sets a custom name for the hero.
     *
     * @param theName the custom player name
     */
    public void setName(final String theName) {
        if (theName == null || theName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Hero name cannot be null or empty.");
        }
        myCustomName = theName.trim();
    }

    @Override
    public String getName() {
        return myCustomName != null ? myCustomName : super.getName();
    }

    /**
     * Adds item to the hero's inventory and updates pillar count.
     *
     * @param theItem the collectible to add
     */
    public void addCollectible(final Collectible theItem) {
        if (theItem == null) {
            throw new IllegalArgumentException(
                    "Collectible cannot be null.");
        }
        myInventory.add(theItem);
        if (theItem instanceof Pillar) {
            myPillarsFound++;
        }
    }

    /**
     * Uses a potion that is in the hero's inventory.
     *
     * @param thePotion the potion to use
     * @return true if the potion was found and used
     */
    public boolean usePotion(final Potion thePotion) {
        if (thePotion == null) {
            throw new IllegalArgumentException("Potion cannot be null.");
        }
        boolean usedPotion = false;
        if (myInventory.contains(thePotion)) {
            if (thePotion instanceof HealingPotion healingPotion) {
                healingPotion.use(this);
            } else if (thePotion.getType() == Potion.Type.HEALING) {
                final Random random = new Random();
                heal(random.nextInt(MIN_POTION_HEAL, MAX_POTION_HEAL));
            } else {
                thePotion.use();
            }
            myInventory.remove(thePotion);
            usedPotion = true;
        }
        return usedPotion;
    }

    /**
     * Executes the hero's class-specific special skill.
     *
     * @param theEnemy the target enemy
     * @return true if the hero earns an extra turn
     */
    abstract boolean skill(DungeonCharacter theEnemy);

    /**
     * Gets number of pillars the hero has collected.
     *
     * @return the number of pillars
     */
    public int getPillarsFound() {
        return myPillarsFound;
    }

    /**
     * Attempts to block incoming damage based on block chance.
     *
     * @return true if the attack is blocked
     */
    public boolean blockAttempt() {
        final Random randomBlock = new Random();
        return randomBlock.nextDouble() * MAX_PERCENTAGE < myBlockChance;
    }

    /**
     * Gets the hero's inventory list.
     *
     * @return the list of collectibles
     */
    public List<Collectible> getInventory() {
        return myInventory;
    }
}