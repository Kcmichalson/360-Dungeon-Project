package model;
import java.util.Random;

public abstract class DungeonCharacter {

    private int myHitPoints;
    private int attackSpeed;
    private int attackMin;
    private int attackMax;
    private String characterName;
    private double chanceToHit;
    private boolean isPlayer;

    protected DungeonCharacter() {
    }

    protected DungeonCharacter(final int theHitPoints) {
        myHitPoints = theHitPoints;
    }

    protected DungeonCharacter(int hitPoints, int speed, int attackMinimum, int attackMaximum, String name, double hitChance) {
        myHitPoints = hitPoints;
        attackSpeed = speed;
        attackMin = attackMinimum;
        attackMax = attackMaximum;
        characterName = name;
        chanceToHit = hitChance;
    }

    public int getHitPoints() {
        return myHitPoints;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public String getName() {
        return characterName;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void takeDamage(final int theAmount) {
        myHitPoints -= theAmount;
        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }

    public void attack(DungeonCharacter target) {
        //Used for attack chance and attack damage
        Random attackMod = new Random();

        if (attackMod.nextInt() < chanceToHit) {
            target.takeDamage(attackMod.nextInt(attackMin,attackMax));
        }
    }

    protected void setPlayer(boolean thePlayerStatus) {
        isPlayer = thePlayerStatus;
    }
}