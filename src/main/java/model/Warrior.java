package model;

import java.util.Random;

public class Warrior extends Hero {

    public Warrior() {
        super(125, 4, 35, 60, "Warrior", 20, 0.8);
    }


    @Override
    boolean skill(DungeonCharacter enemy) {
        Random crushingBlowMod = new Random();

        if (crushingBlowMod.nextInt(100) < 40) {
            enemy.takeDamage(crushingBlowMod.nextInt(75,175));

        }

        return false;
    }
}