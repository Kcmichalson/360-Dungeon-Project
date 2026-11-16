package model;

public abstract class Monster extends DungeonCharacter {

    private final int myMinimumDamage;
    private final int myMaximumDamage;

    protected Monster(final int theHitPoints,
                      final int theMinimumDamage,
                      final int theMaximumDamage) {
        super(theHitPoints);
        myMinimumDamage = theMinimumDamage;
        myMaximumDamage = theMaximumDamage;
    }

    public int getMinimumDamage() {
        return myMinimumDamage;
    }

    public int getMaximumDamage() {
        return myMaximumDamage;
    }
}
