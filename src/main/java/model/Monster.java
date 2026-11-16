package model;

import java.util.Random;

public abstract class Monster extends DungeonCharacter {

    private final int myHealMin;
    private final int myHealMax;
    private final double myHealChance;

    public Monster(final int theHitPoints, final int theSpeed, final int theAttackMinimum, final int theAttackMaximum, final String theName, final double theHitChance, final int theHealMinimum, final int theHealMaximum, final double theHealChancePara) {
        super(theHitPoints, theSpeed, theAttackMinimum, theAttackMaximum, theName, theHitChance);
        myHealMin = theHealMinimum;
        myHealMax = theHealMaximum;
        myHealChance = theHealChancePara;
        setPlayer(false);
    }

    public void heal() {
        final Random healMod = new Random();

        if (healMod.nextDouble(1.0) < myHealChance) {
            heal(healMod.nextInt(myHealMin, myHealMax));
        }
    }
}