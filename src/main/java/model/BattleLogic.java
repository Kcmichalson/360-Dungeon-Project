package model;

import java.util.Random;
import java.util.Scanner;

public class BattleLogic {

    private static final int ENEMY_ACTION_BOUND = 3;
    private static final String CMD_ATTACK = "ATTACK";
    private static final String CMD_ITEM = "ITEM";
    private static final String CMD_SKILL = "SKILL";

    private final DungeonCharacter[] myTurnOrder;
    private final Hero myHero;
    private final Monster myOpponent;

    public BattleLogic(final Hero thePlayer, final Monster theEnemy) {
        if (thePlayer == null || theEnemy == null) {
            throw new IllegalArgumentException("Characters cannot be null.");
        }

        myHero = thePlayer;
        myOpponent = theEnemy;

        if (thePlayer.getAttackSpeed() > theEnemy.getAttackSpeed()) {
            if (thePlayer.getAttackSpeed() >= theEnemy.getAttackSpeed() * 2) {
                myTurnOrder = new DungeonCharacter[]{thePlayer, theEnemy, thePlayer};
            } else {
                myTurnOrder = new DungeonCharacter[]{thePlayer, theEnemy};
            }
        } else if (theEnemy.getAttackSpeed() >= thePlayer.getAttackSpeed() * 2) {
            myTurnOrder = new DungeonCharacter[]{theEnemy, thePlayer, theEnemy};
        } else {
            myTurnOrder = new DungeonCharacter[]{theEnemy, thePlayer};
        }
    }

    public void battleLoop() {
        final Scanner input = new Scanner(System.in);
        System.out.println("Battle Started against a " + myOpponent.getName() + "!");

        while (true) {
            for (final DungeonCharacter character : myTurnOrder) {
                // Stop combat immediately if someone dies mid-turn-order
                if (myHero.getHitPoints() == 0 || myOpponent.getHitPoints() == 0) {
                    break;
                }

                if (character.isPlayer()) {
                    actionSelector(input, myHero, myOpponent);
                } else {
//                    final Random actionSelector = new Random();
//                    final int command = actionSelector.nextInt(ENEMY_ACTION_BOUND);
//                    actionSelector(input, myHero, myOpponent, command);
                    actionSelector(input, myHero, myOpponent, 0);
                }
            }

            if ((myHero.getHitPoints() == 0) || (myOpponent.getHitPoints() == 0)) {
                break;
            }

            // Track healing feedback
            final int startHp = myOpponent.getHitPoints();
            myOpponent.heal();
            if (myOpponent.getHitPoints() > startHp) {
                System.out.println("The " + myOpponent.getName() + " passively healed for " +
                        (myOpponent.getHitPoints() - startHp) + " HP!");
            }
        }
    }

    private void actionSelector(final Scanner theInput, final Hero thePlayer, final Monster theMonster) {
        if (theInput == null || thePlayer == null || theMonster == null) {
            throw new IllegalArgumentException("Parameters cannot be null.");
        }

        boolean validTurn = false;

        // Trap the player in a loop until they type a valid combat command
        while (!validTurn) {
            System.out.println("\n--- Your Turn ---");
            System.out.println("Your HP: " + thePlayer.getHitPoints() + " | " + theMonster.getName() + " HP: " + theMonster.getHitPoints());
            System.out.println("Options: ATTACK, ITEM, SKILL");
            System.out.print("> ");

            final String command = theInput.nextLine().trim().toUpperCase();

            if (CMD_ATTACK.equals(command)) {
                final int startEnemyHp = theMonster.getHitPoints();
                thePlayer.attack(theMonster);
                final int damageDealt = startEnemyHp - theMonster.getHitPoints();

                if (damageDealt > 0) {
                    System.out.println("You strike the " + theMonster.getName() + " for " + damageDealt + " damage!");
                } else {
                    System.out.println("Your attack missed!");
                }
                validTurn = true;

            } else if (CMD_ITEM.equals(command)) {
                System.out.println("Combat inventory not implemented yet. Try attacking.");
            } else if (CMD_SKILL.equals(command)) {
                if (myHero.skill(myOpponent)) {
                    //Insert way to reboot turn for Thief here
                }
            } else {
                System.out.println("Invalid command. Please type ATTACK, ITEM, or SKILL.");
            }
        }
    }

    private void actionSelector(final Scanner theInput, final Hero thePlayer, final Monster theMonster, final int theSelector) {
        if (theInput == null || thePlayer == null || theMonster == null) {
            throw new IllegalArgumentException("Parameters cannot be null.");
        }
        if (theSelector < 0) {
            throw new IllegalArgumentException("Selector cannot be negative.");
        }

        System.out.println("\n--- Enemy Turn ---");

        if (theSelector == 0) {
            if (!thePlayer.blockAttempt()) {
                final int startHeroHp = thePlayer.getHitPoints();
                theMonster.attack(thePlayer);
                final int damageTaken = startHeroHp - thePlayer.getHitPoints();

                if (damageTaken > 0) {
                    System.out.println("The " + theMonster.getName() + " hits you for " + damageTaken + " damage!");
                } else {
                    System.out.println("The " + theMonster.getName() + "'s attack missed you!");
                }
            } else {
                System.out.println("You successfully blocked the " + theMonster.getName() + "'s attack!");
            }
        } else {
            System.out.println("The " + theMonster.getName() + " hesitates and misses its turn!");
        }
    }
}