package controller;

import model.Hero;
import model.Room;
import model.Dungeon;
import view.DungeonView;

public class DungeonController {

    private Dungeon dungeon;
    private DungeonView dungeonView;

    public DungeonController(Dungeon dungeon, DungeonView dungeonView) {
        this.dungeon = dungeon;
        this.dungeonView = dungeonView;
    }

    public void moveHero(String direction) {
        // REQ-14
        if (dungeon.isValidDoor(direction)) {
            dungeon.move(direction);
            Room currentRoom = dungeon.getHeroCurrentRoom();

            processRoomItems(currentRoom);

            // REQ-15
            dungeonView.displayRoom(currentRoom);
        } else {
            // REQ-14
            dungeonView.displayError("Invalid move: No door in that direction.");
        }
    }




    private void processRoomItems(Room room) {
        Hero hero = dungeon.getHero();

        // REQ-16
        if (room.hasPotion()) {
            Potion potion = room.takePotion();
            hero.addCollectible(potion);
            dungeonView.displayMessage("You picked up a " + potion.getName() + "!");
        }

        // REQ-18
        if (room.hasPillar()) {
            Pillar pillar = room.takePillar();
            hero.addCollectible(pillar);
            dungeonView.displayMessage("You found a Pillar: " + pillar.getName() + "!");
        }

        // REQ-17: Pit damage
        if (room.hasPit()) {
            int pitDamage = 10; // Placeholder value
            hero.takeDamage(pitDamage);
            dungeonView.displayMessage("Watch out! You fell into a pit and took " + pitDamage + " damage.");
        }
    }
}
