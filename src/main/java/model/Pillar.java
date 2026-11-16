package model;

public class Pillar implements Collectible {

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