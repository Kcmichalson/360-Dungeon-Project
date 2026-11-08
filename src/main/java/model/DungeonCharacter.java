package model;
import java.util.Random;

public abstract class DungeonCharacter {
    protected int myHitPoints;
    protected int attackSpeed;
    protected int attackMin;
    protected int attackMax;
    protected String characterName;
    protected double chanceToHit;

    public int getHitPoints() {
        return myHitPoints;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public String getName() {
        return characterName;
    }

    public void takeDamage(int amount) {
        myHitPoints -= amount;

        if (myHitPoints < 0) {
            myHitPoints = 0;
        }
    }




    //Returns a number
    //NOTE: Outside the function, if attacks can deal no damage, change function to signify an attack misses
    //instead of just doing no damage. If all attacks do at least some damage, then just account for a no damage attack
    //outside the function
    public void attack(DungeonCharacter target) {
        //Used for attack chance and attack damage
        Random attackMod = new Random();

        if (attackMod.nextInt() < chanceToHit) {
            target.takeDamage(attackMod.nextInt(attackMin,attackMax));
        }
    }

    protected DungeonCharacter(int hitPoints, int speed, int attackMinimum, int attackMaximum, String name, double hitChance) {
        myHitPoints = hitPoints;
        attackSpeed = speed;
        attackMin = attackMinimum;
        attackMax = attackMaximum;
        characterName = name;
        chanceToHit = hitChance;



    }
}