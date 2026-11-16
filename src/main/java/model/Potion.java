package model;

import java.util.Objects;

public class Potion implements Collectible {

    public enum Type {
        HEALING,
        VISION
    }

    private final Type myType;

    public Potion() {
        this(Type.HEALING);
    }

    public Potion(final Type theType) {
        myType = Objects.requireNonNull(theType);
    }

    public Type getType() {
        return myType;
    }
}
