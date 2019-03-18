import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 25;
        Board board = new Board(boardHeight,boardWidth);
        Board realBoard = new Board(boardHeight,boardWidth);

        //System.out.println(board);
        //System.out.println("Hey");

        ArrayList<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        System.out.println(realBoard);
        realBoard.setNumbers();
        System.out.println(realBoard);

        //TODO place bombs with better algorithm --
        //TODO if there are more than one 0 then open everything - Laima
        //TODO speaking with player - Eva
    }
}
