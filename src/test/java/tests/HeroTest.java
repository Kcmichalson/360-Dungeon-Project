package tests;

import model.Warrior;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HeroTest {

    @Test
    public void testTakePitDamage() {
        Warrior hero = new Warrior();
        int startingHP = hero.getHitPoints();
        int pitDamage = 10;

        hero.takeDamage(pitDamage);

        assertEquals(startingHP - pitDamage, hero.getHitPoints(), "Hero HP should decrease by exactly the pit damage amount.");
    }
}