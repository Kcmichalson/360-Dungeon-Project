package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero extends DungeonCharacter {

    private List<Collectible> inventory = new ArrayList<>();
    private int pillarsFound = 0;

    public void addCollectible(Collectible item) {
        this.inventory.add(item);
        if (item instanceof Pillar) {
            this.pillarsFound++;
        }
    }
}