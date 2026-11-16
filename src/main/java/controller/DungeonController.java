package controller;

import java.util.List;
import java.util.Scanner;
import model.BattleLogic;
import model.Dungeon;
import model.HealingPotion;
import model.Hero;
import model.Monster;
import model.Pillar;
import model.Potion;
import model.Room;
import view.DungeonView;

/**
 * Controls main game loop user input and interactions between model and view.
 *
 * @author Kevin Michalson
 * @version 11/15/2026
 */
public class DungeonController {

    /** Amount of damage taken from walking into a pit. */
    private static final int PIT_DAMAGE = 10;

    /** Number of pillars required to win the game. */
    private static final int REQUIRED_PILLARS = 4;

    /** Dungeon model. */
    private final Dungeon myDungeon;

    /** View for displaying output. */
    private final DungeonView myDungeonView;

    /** Hero character. */
    private final Hero myHero;

    /**
     * Constructs a DungeonController.
     *
     * @param theDungeon the dungeon model
     * @param theDungeonView the view
     * @param theHero the hero character
     */
    public DungeonController(final Dungeon theDungeon, final DungeonView theDungeonView, final Hero theHero) {
        myDungeon = theDungeon;
        myDungeonView = theDungeonView;
        myHero = theHero;
    }

    /**
     * Starts the game application and handles the replay loop.
     */
    public void startGame() {
        final Scanner input = new Scanner(System.in);
        boolean playing = true;

        while (playing) {
            playTurnLoop(input);

            myDungeonView.displayMessage("Game Over!");
            myDungeonView.displayMessage("Final Dungeon Map:");
            myDungeonView.displayMessage(myDungeon.toString());

            myDungeonView.displayMessage("Would you like to play again? (Y/N)");
            final String response = input.nextLine().trim();
            if (!response.equalsIgnoreCase("Y")) {
                playing = false;
            } else {
                myDungeonView.displayMessage("Restarting game...");
                // TODO make logic for replay
            }
        }
        input.close();
    }

    /**
     * Runs the individual turn loop until a win or loss is met.
     *
     * @param theInput the scanner for user input
     */
    private void playTurnLoop(final Scanner theInput) {
        myDungeonView.displayMessage("Welcome to the Dungeon!");
        myDungeonView.displayMessage("Find the 4 pillars and reach the exit to win.");
        myDungeonView.displayMessage("Controls: NORTH, EAST, SOUTH, WEST to move. V to view full map. I for Inventory.");

        boolean gameOver = false;

        while (!gameOver) {
            myDungeonView.displayMessage("Enter your move:");
            final String command = theInput.nextLine().trim().toUpperCase();

            if ("V".equals(command)) {
                myDungeonView.displayMessage(myDungeon.toString());
            } else if ("I".equals(command)) {
                handleInventory();
            } else if ("NORTH".equals(command) || "EAST".equals(command)
                    || "SOUTH".equals(command) || "WEST".equals(command)) {

                moveHero(command);

                if (myHero.getHitPoints() <= 0) {
                    myDungeonView.displayMessage("You have died. You lose!");
                    gameOver = true;
                } else if (myDungeon.getHeroCurrentRoom().isExit()) {
                    if (myHero.getPillarsFound() == REQUIRED_PILLARS) {
                        myDungeonView.displayMessage("You found all 4 pillars and escaped. You win!");
                        gameOver = true;
                    } else {
                        myDungeonView.displayMessage("You found the exit, but you need 4 pillars to leave!");
                    }
                }
            } else {
                myDungeonView.displayMessage("Invalid command.");
            }
        }
    }

    /**
     * Move the hero in the direction.
     *
     * @param theDirection the cardinal direction to move
     */
    public void moveHero(final String theDirection) {
        if (myDungeon.isValidDoor(theDirection)) {
            myDungeon.move(theDirection);
            final Room currentRoom = myDungeon.getHeroCurrentRoom();

            processRoomItems(currentRoom);
            processCombat(currentRoom);

            myDungeonView.displayRoom(currentRoom);
        } else {
            myDungeonView.displayError("Invalid move: No door in that direction.");
        }
    }

    /**
     * Trigger combat if the room contains a monster.
     *
     * @param theRoom the room the hero just entered
     */
    private void processCombat(final Room theRoom) {
        final Monster currentEnemy = theRoom.getMonster();

        if (currentEnemy != null && currentEnemy.getHitPoints() > 0) {
            myDungeonView.displayMessage("\n*** ENCOUNTER! A " + currentEnemy.getName() + " blocks your path! ***");

            final BattleLogic battle = new BattleLogic(myHero, currentEnemy);
            battle.battleLoop();

            if (myHero.getHitPoints() > 0) {
                myDungeonView.displayMessage("You defeated the " + currentEnemy.getName() + "!");
                theRoom.setMonster(null); // Clear the monster so it doesn't fight again
            }
        }
    }

    /**
     * Attempt to use a Healing Potion from the inventory.
     */
    private void handleInventory() {
        boolean healed = false;
        // Assuming Hero has a getInventory() method returning a List of Collectibles/Potions
        final List<?> inventory = myHero.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) instanceof HealingPotion) {
                final HealingPotion potion = (HealingPotion) inventory.get(i);
                myHero.usePotion(potion);
                myDungeonView.displayMessage("You drank a " + potion.getName() + "! Current HP: " + myHero.getHitPoints());
                healed = true;
                break; // Only drink one per turn
            }
        }

        if (!healed) {
            myDungeonView.displayMessage("You don't have any Healing Potions in your inventory!");
        }
    }

    /**
     * Identify any items, collectibles, or traps in the room.
     *
     * @param theRoom the room the hero just entered
     */
    private void processRoomItems(final Room theRoom) {
        if (theRoom.hasPotions()) {
            final List<Potion> potions = theRoom.takePotions();
            for (final Potion potion : potions) {
                myHero.addCollectible(potion);
                myDungeonView.displayMessage("You picked up a " + potion.getName() + "!");
            }
        }

        if (theRoom.hasPillar()) {
            final Pillar pillar = theRoom.takePillar();
            myHero.addCollectible(pillar);
            myDungeonView.displayMessage("You found a Pillar: " + pillar.getName() + "!");
        }

        if (theRoom.hasPit()) {
            myHero.takeDamage(PIT_DAMAGE);
            myDungeonView.displayMessage("Watch out! You fell into a pit and took " + PIT_DAMAGE + " damage.");
        }
    }
}