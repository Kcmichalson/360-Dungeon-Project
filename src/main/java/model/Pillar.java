package model;

// Add the 'implements Collectible' to fix the hero pickup error
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

    // Add this method to fix the getName() error in the controller
    public String getName() {
        return myType.toString() + " Pillar";
    }
}