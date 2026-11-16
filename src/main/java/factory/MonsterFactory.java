package factory;

import java.util.Random;

import model.Gremlin;
import model.Monster;
import model.Ogre;
import model.Skeleton;

public class MonsterFactory {

    public Monster createOrdinaryMonster(final Random theRandom) {
        if (theRandom == null) {
            throw new IllegalArgumentException("Random generator cannot be null.");
        }

        return theRandom.nextBoolean()
                ? new Gremlin()
                : new Skeleton();
    }

    public Monster createPillarGuard() {
        return new Ogre();
    }
}