package model;

import java.util.Objects;

public class Potion implements Collectible {

    public enum Type {
        HEALING,
        VISION
    }

    public String name;
    private Type myType;

    public Potion() {
        this(Type.HEALING);
    }

    public Potion(final Type theType) {
        myType = Objects.requireNonNull(theType);
        name = theType.toString() + " Potion";
    }

    public Potion(String potionName) {
        name = potionName;
        myType = Type.HEALING; // Default fallback for string constructor
    }

    public Type getType() {
        return myType;
    }

    public String getName() {
        return name;
    }

    public void use() { }
}