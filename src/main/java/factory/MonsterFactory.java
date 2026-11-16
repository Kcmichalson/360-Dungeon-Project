package factory;

import java.util.Objects;
import java.util.Random;

import model.Gremlin;
import model.Monster;
import model.Ogre;
import model.Skeleton;

public class MonsterFactory {

    public Monster createOrdinaryMonster(final Random theRandom) {
        Objects.requireNonNull(theRandom);

        return theRandom.nextBoolean()
                ? new Gremlin()
                : new Skeleton();
    }

    public Monster createPillarGuard() {
        return new Ogre();
    }
}
