import controller.DungeonController;
import model.Dungeon;
import model.Warrior; // Or whichever specific Hero subclass you want to use
import view.DungeonView;

public class Main {
    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon();

        Warrior hero = new Warrior();

        DungeonView view = new DungeonView();

        DungeonController controller = new DungeonController(dungeon, view, hero);

        controller.startGame();
    }
}