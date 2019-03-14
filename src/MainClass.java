import java.util.ArrayList;
import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) {
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 25;
        Board board = new Board(boardHeight,boardWidth);
        Board realBoard = new Board(boardHeight,boardWidth);
        ArrayList<ArrayList> coordinates = new ArrayList<>();

        //System.out.println(board);
        //System.out.println("Hey");

        //Bombs bomb = new Bombs(boardHeight,boardWidth);
        //System.out.println(bomb.getLocation());

        //TODO have to be elsewhere (board class) and with recursion - Eva
        for (int i = 0; i < bombCount; i++) {
            Bombs bomb = new Bombs(boardHeight,boardWidth);
            if(!coordinates.contains(bomb.getLocation()))
            coordinates.add(bomb.getLocation());
            else {
                Bombs bomb1 = new Bombs(boardHeight,boardWidth);
                coordinates.add(bomb1.getLocation());
            }
        }
        System.out.println(coordinates.size());
        //System.out.println(coordinates);
        realBoard.setBomb(coordinates);
        System.out.println(realBoard);
        realBoard.setNumbers();
        System.out.println(realBoard);
        //TODO place bombs with better algorithm --
        //TODO if there are more than one 0 then open everything - Laima
        //TODO speaking with player - Eva
    }


}
