package controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import model.BattleLogic;
import model.Dungeon;
import model.GameState;
import model.HealingPotion;
import model.Hero;
import model.Monster;
import model.Pillar;
import model.Potion;
import model.Room;

import persistence.SaveManager;
import view.DungeonView;

/**
 * Controls game input and interactions between the model and view.
 *
 * @author Luke Frace
 * @version 11/15/2026
 */
public class DungeonController {

    private static final int PIT_DAMAGE = 10;
    private static final int REQUIRED_PILLARS = 4;

    private Dungeon myDungeon;
    private final DungeonView myDungeonView;
    private Hero myHero;
    private final SaveManager mySaveManager;

    public DungeonController(final Dungeon theDungeon,
                             final DungeonView theDungeonView,
                             final Hero theHero) {
        this(theDungeon, theDungeonView, theHero, new SaveManager());
    }

    public DungeonController(final Dungeon theDungeon,
                             final DungeonView theDungeonView,
                             final Hero theHero,
                             final SaveManager theSaveManager) {

        if (theDungeon == null
                || theDungeonView == null
                || theHero == null
                || theSaveManager == null) {
            throw new IllegalArgumentException(
                    "Game, view and save manager are required.");
        }

        myDungeon = theDungeon;
        myDungeonView = theDungeonView;
        myHero = theHero;
        mySaveManager = theSaveManager;
    }

    public void startGame() {
        final Scanner input = new Scanner(System.in);
        boolean playing = true;

        while (playing) {
            playTurnLoop(input);

            myDungeonView.displayMessage("Game Over!");
            myDungeonView.displayMessage("Final Dungeon Map:");
            myDungeonView.displayMessage(myDungeon.toString());

            myDungeonView.displayMessage(
                    "Would you like to play again? (Y/N)");

            final String response = input.nextLine().trim();

            if (!response.equalsIgnoreCase("Y")) {
                playing = false;
            } else {
                myDungeonView.displayMessage("Restarting game...");
                // TODO implement replay.
            }
        }

        input.close();
    }

    private void playTurnLoop(final Scanner theInput) {
        myDungeonView.displayMessage("Welcome to the Dungeon!");
        myDungeonView.displayMessage(
                "Find the 4 pillars and reach the exit to win.");
        myDungeonView.displayMessage(
                "Controls: NORTH, EAST, SOUTH, WEST to move. "
                        + "V to view full map. I for Inventory. "
                        + "SAVE to save. LOAD to load.");

        boolean gameOver = false;

        while (!gameOver) {
            myDungeonView.displayMessage("Enter your move:");

            final String command =
                    theInput.nextLine().trim().toUpperCase();

            switch (command) {
                case "V" ->
                        myDungeonView.displayMessage(
                                myDungeon.toString());

                case "I" -> handleInventory();

                case "SAVE" -> {
                    myDungeonView.displayMessage(
                            "Enter a new save name:");

                    saveGame(theInput.nextLine().trim());
                }

                case "LOAD" -> {
                    try {
                        myDungeonView.displayMessage(
                                "Saves: " + mySaveManager.listSaves());

                        myDungeonView.displayMessage(
                                "Enter a save name:");

                        loadGame(theInput.nextLine().trim());

                    } catch (final IOException exception) {
                        myDungeonView.displayError(
                                "Could not list saves: "
                                        + exception.getMessage());
                    }
                }

                case "NORTH", "EAST", "SOUTH", "WEST" -> {
                    moveHero(command);

                    if (myHero.getHitPoints() <= 0) {
                        myDungeonView.displayMessage(
                                "You have died. You lose!");
                        gameOver = true;

                    } else if (myDungeon.getHeroCurrentRoom().isExit()) {
                        if (myHero.getPillarsFound()
                                == REQUIRED_PILLARS) {

                            myDungeonView.displayMessage(
                                    "You found all 4 pillars "
                                            + "and escaped. You win!");
                            gameOver = true;

                        } else {
                            myDungeonView.displayMessage(
                                    "You found the exit, but you "
                                            + "need 4 pillars to leave!");
                        }
                    }
                }

                default ->
                        myDungeonView.displayMessage(
                                "Invalid command.");
            }
        }
    }

    public boolean saveGame(final String theName) {
        try {
            mySaveManager.save(
                    theName, new GameState(myHero, myDungeon));

            myDungeonView.displayMessage(
                    "Saved game as " + theName + ".");

            return true;

        } catch (final IOException | IllegalArgumentException exception) {
            myDungeonView.displayError(
                    "Could not save game: " + exception.getMessage());

            return false;
        }
    }

    public boolean loadGame(final String theName) {
        try {
            final GameState loaded = mySaveManager.load(theName);

            myHero = loaded.getHero();
            myDungeon = loaded.getDungeon();

            myDungeonView.displayMessage(
                    "Loaded game " + theName + ".");

            return true;

        } catch (final IOException | IllegalArgumentException exception) {
            myDungeonView.displayError(
                    "Could not load game: " + exception.getMessage());

            return false;
        }
    }

    public Hero getHero() {
        return myHero;
    }

    public Dungeon getDungeon() {
        return myDungeon;
    }

    public void moveHero(final String theDirection) {
        if (myDungeon.isValidDoor(theDirection)) {
            myDungeon.move(theDirection);

            final Room currentRoom = myDungeon.getHeroCurrentRoom();

            processRoomItems(currentRoom);
            processCombat(currentRoom);

            myDungeonView.displayRoom(currentRoom);

            System.out.print("You are in a room. Doors lead: ");

            if (currentRoom.hasDoor("NORTH")) {
                System.out.print("NORTH ");
            }

            if (currentRoom.hasDoor("EAST")) {
                System.out.print("EAST ");
            }

            if (currentRoom.hasDoor("SOUTH")) {
                System.out.print("SOUTH ");
            }

            if (currentRoom.hasDoor("WEST")) {
                System.out.print("WEST ");
            }

            System.out.println();

        } else {
            myDungeonView.displayError(
                    "Invalid move: No door in that direction.");
        }
    }

    private void processCombat(final Room theRoom) {
        final Monster currentEnemy = theRoom.getMonster();

        if (currentEnemy != null
                && currentEnemy.getHitPoints() > 0) {

            myDungeonView.displayMessage(
                    "\n*** ENCOUNTER! A "
                            + currentEnemy.getName()
                            + " blocks your path! ***");

            final BattleLogic battle =
                    new BattleLogic(myHero, currentEnemy);

            battle.battleLoop();

            if (myHero.getHitPoints() > 0) {
                myDungeonView.displayMessage(
                        "You defeated the "
                                + currentEnemy.getName() + "!");

                theRoom.setMonster(null);
            }
        }
    }

    private void handleInventory() {
        boolean healed = false;
        final List<?> inventory = myHero.getInventory();

        for (final Object item : inventory) {
            if (item instanceof HealingPotion potion) {
                myHero.usePotion(potion);

                myDungeonView.displayMessage(
                        "You drank a " + potion.getName()
                                + "! Current HP: "
                                + myHero.getHitPoints());

                healed = true;
                break;
            }
        }

        if (!healed) {
            myDungeonView.displayMessage(
                    "You don't have any Healing Potions "
                            + "in your inventory!");
        }
    }

    private void processRoomItems(final Room theRoom) {
        if (theRoom.hasPotions()) {
            final List<Potion> potions = theRoom.takePotions();

            for (final Potion potion : potions) {
                myHero.addCollectible(potion);

                myDungeonView.displayMessage(
                        "You picked up a "
                                + potion.getName() + "!");
            }
        }

        if (theRoom.hasPillar()) {
            final Pillar pillar = theRoom.takePillar();
            myHero.addCollectible(pillar);

            myDungeonView.displayMessage(
                    "You found a Pillar: "
                            + pillar.getName() + "!");
        }

        if (theRoom.hasPit()) {
            myHero.takeDamage(PIT_DAMAGE);

            myDungeonView.displayMessage(
                    "Watch out! You fell into a pit and took "
                            + PIT_DAMAGE + " damage.");

            myHero.takeDamage(10);
        }
    }
}
