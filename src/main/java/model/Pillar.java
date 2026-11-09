package model;

import java.util.Objects;

public class Pillar {

    public enum Type {
        ABSTRACTION,
        ENCAPSULATION,
        INHERITANCE,
        POLYMORPHISM
    }

    private final Type myType;

    /**
     * Preserves compatibility with existing tests using new Pillar().
     */
    public Pillar() {
        this(Type.ABSTRACTION);
    }

    public Pillar(final Type theType) {
        myType = Objects.requireNonNull(theType);
    }

    public Type getType() {
        return myType;
    }
}
