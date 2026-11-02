package model;

public abstract class DungeonCharacter {
    private int myHitPoints;

    public int getHitPoints() {
        return myHitPoints;
    }

    public void takeDamage(int amount) {
        myHitPoints -= amount;

        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }
}