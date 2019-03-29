import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainClass {


    public static void main(String[] args) {
        int boardHeight = 8;
        int boardWidth = 10;
        int bombCount = 15;
        UserBoard userBoard = new UserBoard(boardHeight, boardWidth);
        Board realBoard = new Board(boardHeight, boardWidth);
        List<List<Integer>> checked = new ArrayList<>();
        int bombsRightNow = 0;
        int openedSquaresNow = 0;
        int allFreeSpaces = boardHeight*boardWidth-bombCount;

        ArrayList<ArrayList> coordinates = realBoard.setBombCoordinates(bombCount);

        realBoard.setBomb(coordinates);
        realBoard.setNumbers();

        M_abi.printM_ind(" ", realBoard.getBoard()); //TODO same here
        System.out.println();
        M_abi.printM_ind(" ", userBoard.getBoard());

        System.out.println("Example of coordinates 3,6 where 3 means row and 6 means column");
        System.out.println("f - flag a bomb, o - open area");
        System.out.println("Example: 3,6,f");
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter coordinates and f or o: ");
            String coord = sc.nextLine();
            String[] uus = coord.split(",");

            int x = Integer.parseInt(uus[0]);
            int y = Integer.parseInt(uus[1]);
            String symbol = uus[2];

            if (symbol.equals("f")) {
                if (userBoard.getBoard()[x][y] == -5) {
                    userBoard.getBoard()[x][y] = -7;
                } else userBoard.getBoard()[x][y] = -5;

                if (realBoard.getBoard()[x][y] == -1) {
                    bombsRightNow += 1;
                }

            } else if (symbol.equals("o")) {
                userBoard.getBoard()[x][y] = realBoard.getBoard()[x][y];
                if (realBoard.getBoard()[x][y] == -1) {
                    System.out.println("Game Over");
                    break;
                } else if (realBoard.getBoard()[x][y] == 0) {
                    openAround(x, y, userBoard, realBoard, checked);
                }
                openedSquaresNow=userBoard.countOpened(); //TODO check
            } else System.out.println("Wrong input");

            if (openedSquaresNow==allFreeSpaces) { //if there is the same number of flags as originally planned
                System.out.println("You won!");
                break;
            }

            M_abi.printM_ind(" ", realBoard.getBoard()); //TODO better way? Showing author?
            System.out.println();
            M_abi.printM_ind(" ", userBoard.getBoard());


        }

    }

    public static void openAround(int x, int y, UserBoard board, Board realBoard, List<List<Integer>> checked) {
    //TODO should we leave it here?
        int[] numbers = realBoard.getNumbersAround(x, y);
        //System.out.println(Arrays.toString(numbers));

        List<Integer> uus = new ArrayList<>();
        uus.add(x);
        uus.add(y);
        if (!checked.contains(uus)) checked.add(uus);


        if (numbers[0] != -9)
            board.getBoard()[x - 1][y - 1] = realBoard.getBoard()[x - 1][y - 1]; //TODO can be a function
        if (numbers[1] != -9) board.getBoard()[x - 1][y] = realBoard.getBoard()[x - 1][y];
        if (numbers[2] != -9) board.getBoard()[x - 1][y + 1] = realBoard.getBoard()[x - 1][y + 1];
        if (numbers[3] != -9) board.getBoard()[x][y + 1] = realBoard.getBoard()[x][y + 1];
        if (numbers[4] != -9) board.getBoard()[x + 1][y + 1] = realBoard.getBoard()[x + 1][y + 1];
        if (numbers[5] != -9) board.getBoard()[x + 1][y] = realBoard.getBoard()[x + 1][y];
        if (numbers[6] != -9) board.getBoard()[x + 1][y - 1] = realBoard.getBoard()[x + 1][y - 1];
        if (numbers[7] != -9) board.getBoard()[x][y - 1] = realBoard.getBoard()[x][y - 1];

        List<Integer> uus2 = new ArrayList<>();
        uus2.add(x - 1);
        uus2.add(y - 1);
        if (numbers[0] == 0 && !checked.contains(uus2)) openAround(x - 1, y - 1, board, realBoard, checked);
        List<Integer> uus3 = new ArrayList<>();
        uus3.add(x - 1);
        uus3.add(y);
        if (numbers[1] == 0 && !checked.contains(uus3)) openAround(x - 1, y, board, realBoard, checked);
        List<Integer> uus4 = new ArrayList<>();
        uus4.add(x - 1);
        uus4.add(y + 1);
        if (numbers[2] == 0 && !checked.contains(uus4)) openAround(x - 1, y + 1, board, realBoard, checked);
        List<Integer> uus5 = new ArrayList<>();
        uus5.add(x);
        uus5.add(y + 1);
        if (numbers[3] == 0 && !checked.contains(uus5)) openAround(x, y + 1, board, realBoard, checked);
        List<Integer> uus6 = new ArrayList<>();
        uus6.add(x + 1);
        uus6.add(y + 1);
        if (numbers[4] == 0 && !checked.contains(uus6)) openAround(x + 1, y + 1, board, realBoard, checked);
        List<Integer> uus7 = new ArrayList<>();
        uus7.add(x + 1);
        uus7.add(y);
        if (numbers[5] == 0 && !checked.contains(uus7)) openAround(x + 1, y, board, realBoard, checked);
        List<Integer> uus8 = new ArrayList<>();
        uus8.add(x + 1);
        uus8.add(y - 1);
        if (numbers[6] == 0 && !checked.contains(uus8)) openAround(x + 1, y - 1, board, realBoard, checked);
        List<Integer> uus9 = new ArrayList<>();
        uus9.add(x);
        uus9.add(y - 1);
        if (numbers[7] == 0 && !checked.contains(uus9)) openAround(x, y - 1, board, realBoard, checked);


    }
}
