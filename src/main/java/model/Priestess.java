package model;

import java.util.Random;

public class Priestess extends Hero {
    public Priestess() {
        super(74, 5, 25, 45, "Priestess", 30, 0.7);
    }



    public int heal() {
        Random healChance = new Random();
        return healChance.nextInt();
    }
}
