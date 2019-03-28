import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 15;
        UserBoard board = new UserBoard(boardHeight,boardWidth);
        Board realBoard = new Board(boardHeight,boardWidth);

        ArrayList<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();

        M_abi.printM_ind(" ",realBoard.getBoard()); //TODO same here
        M_abi.printM_ind(" ",board.getBoard());

        System.out.println("Example of coordinates 3,6 where 3 means row and 6 means column");
        System.out.println("f - flag a bomb, o - open area");
        System.out.println("Example: 3,6,f");
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter coordinates and f or o: ");
            String coord = sc.nextLine();
            String[] uus = coord.split(",");

            int x = Integer.parseInt(uus[0]);
            int y = Integer.parseInt(uus[1]);
            String symbol = uus[2];

            if(symbol.equals("f")){
                if(board.getBoard()[x][y]==-5){
                    board.getBoard()[x][y]=9;
                }else board.getBoard()[x][y]=-5;
            }else if (symbol.equals("o")){
                board.getBoard()[x][y]=realBoard.getBoard()[x][y];
                if(realBoard.getBoard()[x][y]==-1) {
                    System.out.println("Game Over");
                    break;
                }

            }else System.out.println("Wrong input");


            M_abi.printM_ind(" ",realBoard.getBoard()); //TODO better way? Showing author?
            M_abi.printM_ind(" ",board.getBoard());

        }

        //TODO place bombs with better algorithm --
        //TODO if there are more than one 0 then open everything - Laima
        //TODO speaking with player - Eva
    }
}
