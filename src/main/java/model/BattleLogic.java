package model;


import java.util.Random;
import java.util.Scanner;

//Note: basic idea of how battles go
//A battle is started somewhere by a controller with a constructor that has 2 DungeonCharacter arguments
//Get turn speed by getting the speed of all characters, if speed of one doubles the other, put them in the array twice
//You can select an action which is just 1, 2, or 3
//This can be moved somewhere else if needed
//FIXME: This class needs some work, please proofread and fix if you can
public class BattleLogic {
    public DungeonCharacter[] turnOrder;
    Hero hero;
    Monster opponent;

    public BattleLogic(Hero player, Monster enemy) {

        hero = player;
        opponent = enemy;

        if (player.getAttackSpeed() > enemy.getAttackSpeed()) {
            if (player.getAttackSpeed() >= enemy.getAttackSpeed() * 2) {
                turnOrder = new DungeonCharacter[]{player, enemy, player};
            } else {
                turnOrder = new DungeonCharacter[]{player, enemy};
            }
        } else if (enemy.getAttackSpeed() >= player.getAttackSpeed() * 2) {
            turnOrder = new DungeonCharacter[]{enemy, player, enemy};
        } else {
            turnOrder = new DungeonCharacter[]{enemy, player};

        }
    }

    public void battleLoop() {

        final Scanner input = new Scanner(System.in);
        boolean playing = true;


        while (true) {
            for (int i = 0; i < turnOrder.length; i++) {
                if (turnOrder[i].isPlayer) {
                    actionSelector(input, hero, opponent);
                } else {
                    Random actionSelector = new Random();
                    int command = actionSelector.nextInt(3);
                    actionSelector(input, hero, opponent, command);

                }


            }
            if ((hero.getHitPoints() == 0) || (opponent.getHitPoints() == 0)) {
                break;
            }
            opponent.heal();
        }
        if (hero.getHitPoints() == 0) {
            //you win
        } else {
            //you lose
        }

    }

    private void actionSelector(final Scanner theInput, Hero player, Monster monster) {
            final String command = theInput.nextLine().trim().toUpperCase();
            if ("Attack".equals(command)) {
                player.attack(monster);
            } else if ("Item".equals(command)) {
                //FIXME: Please add ability to use item
            } else if ("Skill".equals(command)) {
                //FIXME: Please add ability to use skills
            }
    }

    private void actionSelector(final Scanner theInput, Hero player, Monster monster, int selector) {
            if (selector == 0) {
                if (!player.blockAttempt()) {
                    monster.attack(player);
                }
            }
    }


}








