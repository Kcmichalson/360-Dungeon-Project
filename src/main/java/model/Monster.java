package model;

import java.util.Random;

public abstract class Monster extends DungeonCharacter {

    public int healMin;
    public int healMax;
    public double healChance;

    public Monster(int hitPoints, int speed, int attackMinimum, int attackMaximum, String name, int hitChance, int healMinimum, int healMaximum, float healChancePara) {
        super(hitPoints, speed, attackMinimum, attackMaximum, name, hitChance);
        healMin = healMinimum;
        healMax = healMaximum;
        healChance = healChancePara;
    }


    public void heal() {

        Random healMod = new Random();

        if (healMod.nextDouble(1) < healChance) {
            myHitPoints += healMod.nextInt(healMin, healMax);
        }



    }
}