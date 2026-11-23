package model;

import java.io.Serial;
import java.io.Serializable;

public class Pillar implements Collectible, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum Type {
        ENCAPSULATION,
        ABSTRACTION,
        INHERITANCE,
        POLYMORPHISM
    }

    private final Type myType;

    public Pillar(final Type theType) {
        myType = theType;
    }

    public String getName() {
        return myType.toString() + " Pillar";
    }

    public Type getType() {
        return myType;
    }
}