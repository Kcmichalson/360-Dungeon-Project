package model;

public abstract class DungeonCharacter {

    private int myHitPoints;

    protected DungeonCharacter() {
    }

    protected DungeonCharacter(final int theHitPoints) {
        myHitPoints = theHitPoints;
    }

    public int getHitPoints() {
        return myHitPoints;
    }

    public void takeDamage(final int theAmount) {
        myHitPoints -= theAmount;

        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }
}
