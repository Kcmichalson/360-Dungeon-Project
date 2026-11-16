package tests;

import model.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the Hero classes.
 *
 * @author Kevin Michalson
 * @version 11/08/2026
 */
public class HeroTest {

    /** The amount of damage taken from a pit trap. */
    private static final int PIT_DAMAGE = 10;

    /** A hero for testing. */
    private Warrior myHero;

    /**
     * Sets up the test before each test.
     */
    @BeforeEach
    public void setUp() {
        myHero = new Warrior();
    }

    /**
     * Tests that taking damage correctly decreases hit points.
     */
    @Test
    public void testTakePitDamage() {
        final int startingHP = myHero.getHitPoints();
        myHero.takeDamage(PIT_DAMAGE);

        assertEquals(startingHP - PIT_DAMAGE, myHero.getHitPoints(),
                "Hero HP should decrease by exactly the pit damage amount.");
    }

    /**
     * Tests the loss condition.
     */
    @Test
    public void testLossConditionWhenHpZero() {
        final int fatalDamage = myHero.getHitPoints();
        myHero.takeDamage(fatalDamage);

        assertTrue(myHero.getHitPoints() <= 0,
                "Hero should be dead (loss condition) when HP reaches 0.");
    }
}