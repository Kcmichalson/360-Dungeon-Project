package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Hero extends DungeonCharacter {
    private double blockChance = 20.00;


    private List<Collectible> inventory = new ArrayList<>();
    private int pillarsFound = 0;

    public Hero(int hitPoints, int speed, int attackMinimum, int attackMaximum, String name, int block, double hitChance) {
        super(hitPoints, speed, attackMinimum, attackMaximum, name, hitChance);
        blockChance = block;

    }

    public void addCollectible(Collectible item) {
        this.inventory.add(item);
        if (item instanceof Pillar) {
            this.pillarsFound++;
        }
    }

    //blockAttempt is here to potentially block incoming damage, but takes in damage as an argument
    //to pass on to the take damage function
    public void blockAttempt(int damage) {
        Random blockAttempt = new Random();

        //Only checks for FAILING the block, not for succeeding, this can be changed later
        if (blockAttempt.nextDouble() < blockChance) {
            takeDamage(damage);
        }
    }
}