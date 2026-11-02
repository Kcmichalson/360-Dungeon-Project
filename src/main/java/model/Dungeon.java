package model;

public class Dungeon {

    private Room[][] myMaze;
    private int myHeroRow;
    private int myHeroCol;

    public boolean isValidDoor(String direction) {
        Room currentRoom = getHeroCurrentRoom();
        if (currentRoom != null) {
            return currentRoom.hasDoor(direction);
        }
        return false;
    }

    public Room getHeroCurrentRoom() {
        if (myMaze != null) {
            return myMaze[myHeroRow][myHeroCol];
        }
        return null;
    }

    public void move(String direction) {
        if (direction == null) return;

        switch (direction.toUpperCase()) {
            case "NORTH" -> myHeroRow--;
            case "EAST"  -> myHeroCol++;
            case "SOUTH" -> myHeroRow++;
            case "WEST"  -> myHeroCol--;
        }
    }

    // tests
    public int getHeroRow() { return myHeroRow; }
    public int getHeroCol() { return myHeroCol; }
}
