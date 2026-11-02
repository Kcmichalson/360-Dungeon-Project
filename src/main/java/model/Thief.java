package model;

import java.util.Random;

public class Thief extends Hero {
    public Thief() {
        super(75, 6, 20, 40, "Thief", 40, 0.8);
    }


    //FIXME: I don't know how turns work at the moment but when I do, make sure this refreshes the turn
    public int SupriseAttack() {

        Random surpriseAttackMod = new Random();
        int damage = 0;

        double surpriseChance = surpriseAttackMod.nextDouble();
        if (surpriseChance <= 60.00) {
            damage = surpriseAttackMod.nextInt(20,40);
        } else if (surpriseChance <= 20.00) {
            damage = surpriseAttackMod.nextInt(20,40);
        }

        return damage;


    }
}
