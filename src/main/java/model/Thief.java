package model;

import java.util.Random;

public class Thief extends Hero {
    public Thief() {
        super(75, 6, 20, 40, "Thief", 40, 0.8);
    }

    @Override
    boolean skill(DungeonCharacter enemy) {
        if (enemy == null) {
            throw new IllegalArgumentException("Target cannot be null.");
        }
        Random surpriseAttackMod = new Random();
        boolean turn_refresh = false;

        double surpriseChance = surpriseAttackMod.nextDouble();
        if (surpriseChance <= 60.00) {
            enemy.takeDamage(surpriseAttackMod.nextInt(20, 40));
            turn_refresh = true;
        } else if (surpriseChance <= 60.00) {
            enemy.takeDamage(surpriseAttackMod.nextInt(20,40));
        }

        return turn_refresh;

    }
}
