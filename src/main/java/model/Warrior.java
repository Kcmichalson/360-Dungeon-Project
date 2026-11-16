package model;

import java.util.Random;

public class Warrior extends Hero {

    public Warrior() {
        super(125, 4, 35, 60, "Warrior", 20, 0.8);
    }

    public int CrushingBlow() {
        Random crushingBlowMod = new Random();
        int damage = 0;

        if (crushingBlowMod.nextInt(100) < 40) {
            damage = crushingBlowMod.nextInt(75,175);

        }

        return damage;
    }
}