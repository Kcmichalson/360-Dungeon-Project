package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Stores the hero and dungeon together as one saved game.
 */
public final class GameState implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Hero myHero;
    private final Dungeon myDungeon;

    public GameState(final Hero theHero, final Dungeon theDungeon) {
        if (theHero == null || theDungeon == null) {
            throw new IllegalArgumentException(
                    "Hero and dungeon are required.");
        }

        myHero = theHero;
        myDungeon = theDungeon;
    }

    public Hero getHero() {
        return myHero;
    }

    public Dungeon getDungeon() {
        return myDungeon;
    }
}
