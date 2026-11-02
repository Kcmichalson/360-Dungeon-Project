package model;
import java.util.Random;

public abstract class DungeonCharacter {
    private int myHitPoints;
    private int attackSpeed;
    private int attackMin;
    private int attackMax;
    private String characterName;
    private int chanceToHit;

    public int getHitPoints() {
        return myHitPoints;
    }

    public int getAttackSpeed() {
        return attackSpeed;
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
    public int attack() {
        //Used for attack chance and attack damage
        Random attackMod = new Random();

        int damage = 0;

        if (attackMod.nextInt() < chanceToHit) {
            damage = attackMod.nextInt(attackMin,attackMax);
        }

        return damage;
    }
}