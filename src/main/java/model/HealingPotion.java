package model;

import java.util.Random;

public class HealingPotion extends Potion {

    private static final String POTION_NAME = "Healing Potion";
    private static final int MIN_HEAL = 10;
    private static final int MAX_HEAL = 15;

    public HealingPotion() {
        super(POTION_NAME);
    }

    public void use(final Hero theHero) {
        if (theHero == null) {
            throw new IllegalArgumentException("Hero cannot be null.");
        }

        final Random healAmount = new Random();
        theHero.heal(healAmount.nextInt(MIN_HEAL, MAX_HEAL));
    }
}