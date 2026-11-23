package model;

import java.io.Serial;
import java.io.Serializable;

public class Potion implements Collectible, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum Type {
        HEALING,
        VISION
    }

    private final String myName;
    private final Type myType;

    public Potion() {
        this(Type.HEALING);
    }

    public Potion(final Type theType) {
        if (theType == null) {
            throw new IllegalArgumentException("Potion type cannot be null.");
        }
        myType = theType;
        myName = theType + " Potion";
    }

    public Potion(final String thePotionName) {
        if (thePotionName == null || thePotionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Potion name cannot be null or empty.");
        }
        myName = thePotionName;
        myType = Type.HEALING;
    }

    public Type getType() {
        return myType;
    }

    public String getName() {
        return myName;
    }

    public void use() { }
}