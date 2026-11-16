package model;

import java.util.Random;

public class HealingPotion extends Potion {

    public HealingPotion() {
        super("Healing Potion");
    }

    public void use(Hero hero) {
        Random healAmount = new Random();
        hero.takeDamage(healAmount.nextInt(10,15));

    }
}
