import controller.DungeonController;
import java.util.Scanner;
import model.Dungeon;
import model.Hero;
import model.Priestess;
import model.Thief;
import model.Warrior;
import view.DungeonView;

public class Main {

    private static final String CLASS_WARRIOR = "1";
    private static final String CLASS_PRIESTESS = "2";
    private static final String CLASS_THIEF = "3";

    public static void main(final String[] theArgs) {
        final Scanner input = new Scanner(System.in);
        final DungeonView view = new DungeonView();

        view.displayMessage("Dungeon Adventure Project");
        view.displayMessage("Enter your hero's name:");
        final String heroName = input.nextLine().trim();

        final Hero hero = selectHeroClass(input, view);
        if (!heroName.isEmpty()) {
            hero.setName(heroName);
        }

        final Dungeon dungeon = new Dungeon();
        final DungeonController controller =
                new DungeonController(dungeon, view, hero);

        controller.startGame();
    }

    private static Hero selectHeroClass(final Scanner theInput,
                                        final DungeonView theView) {
        while (true) {
            theView.displayMessage("Choose your class:");
            theView.displayMessage("1) Warrior   (Tanky, High-damage skill)");
            theView.displayMessage("2) Priestess (Balanced, Self-healing skill)");
            theView.displayMessage("3) Thief     (Glass-cannon, Bonus-move skill)");

            final String choice = theInput.nextLine().trim().toUpperCase();
            switch (choice) {
                case CLASS_WARRIOR, "WARRIOR" -> {
                    return new Warrior();
                }
                case CLASS_PRIESTESS, "PRIESTESS" -> {
                    return new Priestess();
                }
                case CLASS_THIEF, "THIEF" -> {
                    return new Thief();
                }
                default -> theView.displayError(
                        "Invalid choice. Enter 1, 2 or 3.");
            }
        }
    }
}