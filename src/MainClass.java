import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 25;
        UserBoard board = new UserBoard(boardHeight,boardWidth);
        Board realBoard = new Board(boardHeight,boardWidth);

        ArrayList<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        //System.out.println(realBoard);
        realBoard.setNumbers();
        System.out.println(realBoard);

        System.out.println(board);

        System.out.println("Example of coordinates 3,6 where 3 means row and 6 means column");
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter coordinates: ");
            String coord = sc.nextLine();
            String[] uus = coord.split(",");
            System.out.println(Arrays.toString(uus));


        }

        //TODO place bombs with better algorithm --
        //TODO if there are more than one 0 then open everything - Laima
        //TODO speaking with player - Eva
    }
}
